package com.example.fypapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrderInfoFragment extends Fragment {

    private TextView tableNo, updateOrderTotal;
    private RecyclerView currentOrder, newItems;
    private EditText updateNote;
    private Button updateOrder, proceedToPayment;
    private FirebaseFirestore db;
    private String docId, totalFromBundle, formattedTotal, tableNumber, staffMember, cartTotal, note, role;
    private List<MenuItem> items;
    private List<MenuItem> additionalItems = new ArrayList<>();
    private List<MenuItem> mergedItems = new ArrayList<>();
    private currentOrderAdapter currentOrderAdapter;
    private newOrderAdapter newOrderAdapter;
    private StaggeredGridLayoutManager currentRecyclerStaggeredGridLayoutManager, newItemsStaggeredGridLayoutManager;
    private ArrayList<String> totals = new ArrayList<>();
    private final String TAG = "TAG";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View orderInfoView = inflater.inflate(R.layout.fragment_order_info, container, false);
        db = FirebaseFirestore.getInstance();

        if(getArguments() != null){
            docId = getArguments().getString("docId");
            totalFromBundle = getArguments().getString("total");
            tableNumber = getArguments().getString("tableNo");
            staffMember = getArguments().getString("staffMember");
            role = getArguments().getString("role");
            items = getArguments().getParcelableArrayList("items");
            note = getArguments().getString("note");
        }
        String[] splitTotal = totalFromBundle.split("€");
        formattedTotal = splitTotal[1];
        Log.d(TAG,formattedTotal);

        tableNo = orderInfoView.findViewById(R.id.orderInfoTableNo);
        updateOrderTotal = orderInfoView.findViewById(R.id.updatedOrderTotal);

        tableNo.setText(tableNumber);
        updateOrderTotal.setText(totalFromBundle);

        currentOrder = orderInfoView.findViewById(R.id.currentOrderRecycler);
        currentOrder.addItemDecoration(new DividerItemDecoration(orderInfoView.getContext(), DividerItemDecoration.VERTICAL));
        currentOrderAdapter = new currentOrderAdapter(items);
        currentRecyclerStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        currentOrder.setLayoutManager(currentRecyclerStaggeredGridLayoutManager);
        currentOrder.setAdapter(currentOrderAdapter);

        newItems = orderInfoView.findViewById(R.id.newItemsRecycler);
        newItems.addItemDecoration(new DividerItemDecoration(orderInfoView.getContext(), DividerItemDecoration.VERTICAL));
        setUpNewItemsRecycler(updateOrderTotal, formattedTotal);
        getCartTotal();

        updateNote = orderInfoView.findViewById(R.id.updateOrderNote);
        updateNote.setText(note);

        updateOrder = orderInfoView.findViewById(R.id.submitUpdatedOrder);
        proceedToPayment = orderInfoView.findViewById(R.id.serverPaymentGateway);



        updateOrder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String timestamp = getCurrentDateAndTime();

                db.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot doc : task.getResult()){
                                additionalItems.add(doc.toObject(MenuItem.class));
                            }
                            mergedItems.addAll(items);
                            mergedItems.addAll(additionalItems);

                            for(MenuItem m : mergedItems){
                                Log.d(TAG, m.getType());
                                Log.d(TAG,m.getName());
                                Log.d(TAG,m.getPrice());
                            }

                            if(additionalItems.size() == 0){
                                Toast.makeText(view.getContext(), "No Items in Order", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(view.getContext(), "Order Updated", Toast.LENGTH_LONG).show();
                                updateOrderData(tableNumber, timestamp, staffMember, mergedItems, updateOrderTotal.getText().toString());
                                db.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            Log.d(TAG, "Cart cleared");
                                            for(QueryDocumentSnapshot snap: task.getResult()){
                                                snap.getReference().delete();
                                            }
                                        } else {
                                            Log.d(TAG, "Cart failed to clear");
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

                Intent i = new Intent(orderInfoView.getContext(), ServerMain.class);
                i.putExtra("staffMember", staffMember);
                i.putExtra("role", role);
                startActivity(i);
            }
        });

        proceedToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(orderInfoView.getContext(), ProcessPayment.class);
                i.putExtra("total", updateOrderTotal.getText().toString());
                i.putExtra("staffMember", staffMember);
                i.putParcelableArrayListExtra("items", (ArrayList) mergedItems);
                i.putExtra("docId", docId);

                startActivity(i);
            }
        });


        return orderInfoView;
    }


    private void setUpNewItemsRecycler(TextView total, String previousTotal){
        Query query = db.collection("Cart").orderBy("type", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        newOrderAdapter = new newOrderAdapter(options, total, previousTotal);
        newItemsStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        newItems.setLayoutManager(newItemsStaggeredGridLayoutManager);
        newItems.setAdapter(newOrderAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCurrentDateAndTime(){
        String timeAndDate = "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        Log.d(TAG,dtf.format(now));
        timeAndDate = dtf.format(now);
        return timeAndDate;
    }

    private void getCartTotal(){
        db.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Log.d(TAG, doc.getId() + " => " + doc.getData());
                        Map<String, Object> data = doc.getData();
                        totals.add((String) data.get("price"));
                        Log.d(TAG, (String) data.get("price"));

                    }
                    cartTotal = calculateTotal(totals, formattedTotal);
                    Log.d(TAG, cartTotal);
                    updateOrderTotal.setText(cartTotal);

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

    private String calculateTotal(ArrayList<String> totals, String previousValue){
        double totalDouble = Double.parseDouble(previousValue);
        //double totalDouble = 0.00;
        for(String s : totals){
            double itemDouble = Double.parseDouble(s);
            totalDouble = totalDouble + itemDouble;
        }
        String totalFormatted = String.format("%.2f", totalDouble);
        String euroSymbol = "€";
        String total = euroSymbol + totalFormatted;
        return total;
    }

    private void updateOrderData(String tableNo, String timestamp, String username, List<MenuItem> items, String total){
        DocumentReference docRef = db.collection("Orders").document(docId);
        Map<String, Object> updateOrder = new HashMap<>();
        updateOrder.put("tableNo", tableNo);
        updateOrder.put("timestamp", timestamp);
        updateOrder.put("staffMember", username);
        updateOrder.put("items", items);
        updateOrder.put("total", total);
        docRef.set(updateOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Success, order " + docId + " was updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed, order " + docId + " was not updated");
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if(newOrderAdapter != null){
            newOrderAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(newOrderAdapter != null){
            newOrderAdapter.stopListening();
        }
    }
}