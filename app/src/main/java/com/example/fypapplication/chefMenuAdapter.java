package com.example.fypapplication;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class chefMenuAdapter extends FirestoreRecyclerAdapter<MenuItem, chefMenuAdapter.ChefMenuViewHolder> {

    public chefMenuAdapter(@NonNull FirestoreRecyclerOptions<MenuItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChefMenuViewHolder holder, int position, @NonNull MenuItem model) {
        ImageView editButton = holder.itemView.findViewById(R.id.chefEditMenuItem);

        holder.name.setText(model.getName());

        String docId = getSnapshots().getSnapshot(position).getId();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), UpdateMenuItemIngredients.class);
                i.putExtra("docId", docId);
                i.putExtra("name", model.getName());
                i.putExtra("type", model.getType());
                i.putExtra("desc", model.getDesc());
                i.putExtra("price", model.getPrice());
                i.putParcelableArrayListExtra("ingredients", (ArrayList<Ingredients>) model.getIngredients());
                i.putExtra("cost", model.getCostPerUnit());
                view.getContext().startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public ChefMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_view_chef_menu_layout, parent, false);
        return new ChefMenuViewHolder(v);
    }

    public class ChefMenuViewHolder extends RecyclerView.ViewHolder{

        private TextView name;

        public ChefMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.chefMenuItemName);
        }
    }
}
