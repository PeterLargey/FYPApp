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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class addMenuItemAdapter extends FirestoreRecyclerAdapter<MenuItem, addMenuItemAdapter.AddItemViewHolder> {

    public addMenuItemAdapter(@NonNull FirestoreRecyclerOptions<MenuItem> options){super(options);}

    @Override
    protected void onBindViewHolder(@NonNull AddItemViewHolder holder, int position, @NonNull MenuItem model) {
        ImageView addButton = holder.itemView.findViewById(R.id.addMenuItem);

        String euroSymbol = "€";
        holder.name.setText(model.getName());
        holder.price.setText(euroSymbol + model.getPrice());
        holder.desc.setText(model.getDesc());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Item added to Cart", Toast.LENGTH_LONG).show();
                String price = String.valueOf(model.getPrice());
                inputDataIntoCart(model.getName(), price, model.getType(), model.getIngredients(), model.getCostPerUnit());
            }
        });
    }

    @NonNull
    @Override
    public AddItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_add_item_layout, parent, false);
        return new AddItemViewHolder(v);
    }

    public class AddItemViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView desc;
        private TextView price;

        public AddItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemName);
            desc = itemView.findViewById(R.id.itemDesc);
            price = itemView.findViewById(R.id.itemPrice);

        }
    }

    public void inputDataIntoCart(String name, String price, String type, List<Ingredients> ingredients, String costPerUnit){
        String TAG = "TAG";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Cart").document();
        Map<String, Object> order = new HashMap<>();
        order.put("type", type);
        order.put("name", name);
        order.put("price", price);
        order.put("costPerUnit", costPerUnit);
        order.put("ingredients", ingredients);

        docRef.set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: item has been added to an order");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: item has not been added to an order");
            }
        });

    }
}
