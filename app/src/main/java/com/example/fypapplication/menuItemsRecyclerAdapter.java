package com.example.fypapplication;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class menuItemsRecyclerAdapter extends RecyclerView.Adapter<menuItemsRecyclerAdapter.ViewHolder> {

    List<MenuItem> items;
    String result;

    public menuItemsRecyclerAdapter(List<MenuItem> items){
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_manager_menu_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageView editButton = holder.itemView.findViewById(R.id.editMenuItem);

        MenuItem item = items.get(position);
        String name = item.getName();
        String desc = item.getDesc();
        String price = item.getPrice();
        String type = item.getType();

        holder.itemName.setText(name);
        holder.itemDesc.setText(desc);
        holder.itemPrice.setText(price);

        String docId = findDocId(name);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditMenuItem.class);
                i.putExtra("docId", docId);
                i.putExtra("name", name);
                i.putExtra("desc", desc);
                i.putExtra("price", price);
                i.putExtra("type", type);

                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView itemName;
        private TextView itemDesc;
        private TextView itemPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.managerMenuItemName);
            itemDesc = itemView.findViewById(R.id.managerMenuItemDesc);
            itemPrice = itemView.findViewById(R.id.managerMenuItemPrice);
        }
    }


    private String findDocId(String name){
        String TAG = "TAG";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Menu").whereEqualTo("name", name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    //String holder = "";
                    for(QueryDocumentSnapshot doc: task.getResult()){
                        result = doc.getId();
                    }
                    //result = holder;

                }else{
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        return result;
    }
}
