package com.example.fypapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class currentOrderAdapter extends RecyclerView.Adapter<currentOrderAdapter.CurrentOrderViewHolder> {

    private List<MenuItem> itemList;

    public currentOrderAdapter(List<MenuItem> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public CurrentOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_current_order_layout, parent, false);
        return new CurrentOrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentOrderViewHolder holder, int position) {
        holder.name.setText(itemList.get(position).getName());
        holder.price.setText("€" + itemList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class CurrentOrderViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView price;

        public CurrentOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.currentOrderItemName);
            price = itemView.findViewById(R.id.currentOrderItemPrice);
        }
    }
}
