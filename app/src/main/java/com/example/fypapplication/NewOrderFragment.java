package com.example.fypapplication;

import static java.lang.String.format;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewOrderFragment extends Fragment {

    private String tableNumber;
    private String cartTotal;
    private String staffUserName, role;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView newOrderRecycler;
    private newOrderAdapter adapter;
    private Button submitOrder;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ArrayList<String> totals = new ArrayList<>();
    private List<MenuItem> items = new ArrayList<>();
    private Map<String, Object> data;
    private EditText orderNote;
    private TextView total;
    private final String TAG = "TAG";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newOrderView = inflater.inflate(R.layout.fragment_new_order, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if(getArguments() != null){
            tableNumber = getArguments().getString("tableNo");
            role = getArguments().getString("role");
            staffUserName = getArguments().getString("staffMember");
        }

        TextView tableNo = newOrderView.findViewById(R.id.newOrderTableNo);
        tableNo.setText(tableNumber);

        newOrderRecycler = newOrderView.findViewById(R.id.newOrderItemsRecycler);
        newOrderRecycler.addItemDecoration(new DividerItemDecoration(newOrderView.getContext(), DividerItemDecoration.VERTICAL));

        orderNote = newOrderView.findViewById(R.id.orderNote);
        total = newOrderView.findViewById(R.id.newOrderTotal);
        setUpRecycler(total);
        getCartTotal();


        submitOrder = newOrderView.findViewById(R.id.submitNewOrder);

        submitOrder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String timestamp = getCurrentDateAndTime();
                getServerName();
                String note = orderNote.getText().toString();

                //DocumentReference docRef = db.collection("Cart").document().collection()

                db.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot doc : task.getResult()){
                                items.add(doc.toObject(MenuItem.class));
                            }

                            for(MenuItem i : items){
                                Log.d(TAG, i.getName());
                                Log.d(TAG, i.getPrice());
                                Log.d(TAG, i.getType());

                            }

                            if(items.size() == 0){
                                Toast.makeText(view.getContext(), "No Items in Order", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(view.getContext(), "Order Created", Toast.LENGTH_LONG).show();
                                inputOrderData(tableNumber, timestamp, staffUserName, items, total.getText().toString(), note);
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

                if(role.equalsIgnoreCase("server")){
                    Intent i = new Intent(newOrderView.getContext(), ServerMain.class);
                    i.putExtra("staffMember", staffUserName);
                    i.putExtra("role", role);
                    startActivity(i);
                } else if(role.equalsIgnoreCase("manager")){
                    Intent i = new Intent(newOrderView.getContext(), ManagerMain.class);
                    i.putExtra("staffMember", staffUserName);
                    i.putExtra("role", role);
                    startActivity(i);
                }

            }
        });

        return newOrderView;
    }



    private void setUpRecycler(TextView total){
        Query query = db.collection("Cart").orderBy("type", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        adapter = new newOrderAdapter(options, total, "0.00");
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        newOrderRecycler.setLayoutManager(staggeredGridLayoutManager);
        newOrderRecycler.setAdapter(adapter);
    }

    public void getCartTotal(){
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
                    cartTotal = calculateTotal(totals);
                    Log.d(TAG, cartTotal);
                    total.setText(cartTotal);

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

    public String calculateTotal(ArrayList<String> totals){
        double totalDouble = 0.00;
        for(String s : totals){
            double itemDouble = Double.parseDouble(s);
            totalDouble = totalDouble + itemDouble;
        }
        String totalFormatted = String.format("%.2f", totalDouble);
        String euroSymbol = "€";
        String total = euroSymbol + totalFormatted;
        return total;
    }

    private void inputOrderData(String tableNo, String timestamp, String username, List<MenuItem> items, String total, String note){
        DocumentReference docRef = db.collection("Orders").document();
        Map<String, Object> newOrder = new HashMap<>();
        newOrder.put("tableNo", tableNo);
        newOrder.put("timestamp", timestamp);
        newOrder.put("staffMember", username);
        newOrder.put("items", items);
        newOrder.put("total", total);
        newOrder.put("note", note);
        docRef.set(newOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Success, new order was added to the database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed, new order was not added to the database");
            }
        });
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

    private void getServerName(){
        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("Staff").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
                        data = snapshot.getData();
                        Object[] values = data.values().toArray();
                        Log.d(TAG, "Document snapshot data: " + snapshot.getData());
                        Log.d(TAG, "Document snapshot data: " + Arrays.toString(values));
                        staffUserName = (String) values[2];
                    }
                }
            }
        });
        //String user = staffUserName;
        //return staffUserName;
    }


    @Override
    public void onStart() {
        super.onStart();

        if(adapter != null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }
}