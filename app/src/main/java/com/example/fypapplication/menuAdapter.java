package com.example.fypapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class menuAdapter extends FirestoreRecyclerAdapter<MenuItem, menuAdapter.MenuViewHolder> {

    private String customerName;

    public menuAdapter(@NonNull FirestoreRecyclerOptions<MenuItem> options, String customerName){
        super(options);
        this.customerName = customerName;
    }

    @Override
    protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull MenuItem model) {

        String euroSymbol = "€";
        holder.name.setText(model.getName());
        holder.price.setText(euroSymbol + model.getPrice());
        holder.desc.setText(model.getDesc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CalorieBreakdown.class);
                i.putExtra("name", model.getName());
                i.putExtra("desc", model.getDesc());
                i.putExtra("customerName", customerName);
                view.getContext().startActivity(i);
            }
        });

    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_user_menu_layout, parent, false);
        return new MenuViewHolder(v);
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView desc;
        private TextView price;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.menuItemName);
            price = itemView.findViewById(R.id.menuItemPrice);
            desc = itemView.findViewById(R.id.menuItemDesc);

        }
    }


}
