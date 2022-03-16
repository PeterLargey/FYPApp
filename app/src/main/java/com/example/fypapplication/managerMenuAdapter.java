package com.example.fypapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class managerMenuAdapter extends FirestoreRecyclerAdapter<MenuItem, managerMenuAdapter.ManagerMenuViewHolder> {

    public managerMenuAdapter (@NonNull FirestoreRecyclerOptions<MenuItem> options){super(options);}

    @Override
    protected void onBindViewHolder(@NonNull ManagerMenuViewHolder holder, int position, @NonNull MenuItem model) {
        ImageView editButton = holder.itemView.findViewById(R.id.editMenuItem);

        String euroSymbol = "€";
        holder.name.setText(model.getName());
        holder.price.setText(euroSymbol + model.getPrice());
        holder.desc.setText(model.getDesc());

        String docId = getSnapshots().getSnapshot(position).getId();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditMenuItem.class);
                i.putExtra("docId", docId);
                i.putExtra("name", model.getName());
                i.putExtra("desc", model.getDesc());
                i.putExtra("price", model.getPrice());
                i.putExtra("type", model.getType());

                view.getContext().startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public ManagerMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_manager_menu_layout, parent, false);
        return new ManagerMenuViewHolder(v);
    }

    public class ManagerMenuViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView desc;
        private TextView price;

        public ManagerMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.managerMenuItemName);
            desc = itemView.findViewById(R.id.managerMenuItemDesc);
            price = itemView.findViewById(R.id.managerMenuItemPrice);
        }
    }
}
