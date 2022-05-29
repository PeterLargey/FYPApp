package com.example.fypapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class inventoryAdapter extends FirestoreRecyclerAdapter<InventoryItem, inventoryAdapter.InventoryViewHolder> {

    private String role;
    private AlertDialog.Builder builder;

    public inventoryAdapter(@NonNull FirestoreRecyclerOptions<InventoryItem> options, String role) {
        super(options);
        this.role = role;
    }

    @Override
    protected void onBindViewHolder(@NonNull InventoryViewHolder holder, int position, @NonNull InventoryItem model) {
        ImageView deleteButton = holder.itemView.findViewById(R.id.deleteInventoryItem);

        holder.name.setText(model.getName());
        holder.desc.setText(model.getDesc());
        holder.units.setText(model.getUnits());
        holder.expiry.setText(model.getExpiry());

        int numberOfUnits = Integer.parseInt(model.getUnits());

        if(numberOfUnits <= 5){
            holder.status.setText("Low");
        } else if(numberOfUnits <= 10){
            holder.status.setText("Medium");
        } else {
            holder.status.setText("High");
        }

        String docId = getSnapshots().getSnapshot(position).getId();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Would you like to delete this inventory item?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                DocumentReference docRef = db.collection("Inventory").document(docId);
                                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("TAG", "Item deleted from the Inventory database");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "Item failed to delete from the Inventory database");
                                    }
                                });

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditInventoryItem.class);
                i.putExtra("name", model.getName());
                i.putExtra("docId", docId);
                i.putExtra("desc", model.getDesc());
                i.putExtra("expiry", model.getExpiry());
                i.putExtra("units", model.getUnits());
                i.putExtra("role", role);
                view.getContext().startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_inventory_layout, parent, false);
        return new InventoryViewHolder(v);
    }

    public class InventoryViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView units;
        private TextView expiry;
        private TextView status;
        private TextView desc;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.inventoryItemName);
            units = itemView.findViewById(R.id.inventoryItemUnits);
            expiry = itemView.findViewById(R.id.inventoryItemExpiry);
            status = itemView.findViewById(R.id.inventoryItemStatus);
            desc = itemView.findViewById(R.id.inventoryItemDesc);
        }
    }

}
