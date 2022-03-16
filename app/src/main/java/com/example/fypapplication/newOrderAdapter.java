package com.example.fypapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class newOrderAdapter extends FirestoreRecyclerAdapter<MenuItem, newOrderAdapter.NewOrderViewHolder> {

    private String cartTotals = "";
    private TextView total;
    private String previousValue;

    public newOrderAdapter(@NonNull FirestoreRecyclerOptions<MenuItem> options, TextView total, String previousTotal ){
        super(options);
        this.total = total;
        this.previousValue = previousTotal;
    }

    @Override
    protected void onBindViewHolder(@NonNull NewOrderViewHolder holder, int position, @NonNull MenuItem model) {

        ImageView deleteButton = holder.itemView.findViewById(R.id.deleteItem);

        String euroSymbol = "€";

        holder.name.setText(model.getName());
        holder.type.setText(model.getType());
        holder.price.setText(euroSymbol + model.getPrice());

        String docId = getSnapshots().getSnapshot(position).getId();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TAG = "TAG";
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                DocumentReference docRef = db.collection("Cart").document(docId);
                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Cart Item Deleted");
                        Toast.makeText(view.getContext(), "Cart Item Deleted", Toast.LENGTH_LONG).show();
                        ArrayList<String> totals = new ArrayList<>();

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

                                        cartTotals = updateCart(totals, previousValue);
                                        Log.d(TAG, cartTotals);
                                        total.setText(cartTotals);


                                    }else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Cart Item Failed To Deleted");
                        Toast.makeText(view.getContext(), "Cart Item Failed To Deleted", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    @NonNull
    @Override
    public NewOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_new_order_layout, parent, false);
        return new NewOrderViewHolder(v);
    }

    public class NewOrderViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView type;
        private TextView price;

        public NewOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.newOrderItemName);
            type = itemView.findViewById(R.id.newOrderItemType);
            price = itemView.findViewById(R.id.newOrderItemPrice);

        }
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
    }



        private String updateCart(ArrayList<String> totals, String previousValue){
        double totalDouble = Double.parseDouble(previousValue);
        for(String s : totals){
            double itemDouble = Double.parseDouble(s);
            totalDouble = totalDouble + itemDouble;
        }
        String totalFormatted = String.format("%.2f", totalDouble);
        String euroSymbol = "€";
        String total = euroSymbol + totalFormatted;
        return total;
    }
}
