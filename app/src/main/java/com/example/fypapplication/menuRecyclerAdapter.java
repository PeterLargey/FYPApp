package com.example.fypapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class menuRecyclerAdapter  extends RecyclerView.Adapter<menuRecyclerAdapter.ViewHolder> {

    List<MenuSection> sectionList;

    public menuRecyclerAdapter(List<MenuSection> sectionList){
        this.sectionList = sectionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MenuSection section = sectionList.get(position);
        String sectionTitle = section.getSectionTitle();
        List<MenuItem> items = section.getMenuItems();

        holder.sectionTitle.setText(sectionTitle);
        menuItemsRecyclerAdapter childAdapter = new menuItemsRecyclerAdapter(items);
        holder.childRecyclerView.setAdapter(childAdapter);


    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView sectionTitle;
        private RecyclerView childRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionTitle = itemView.findViewById(R.id.sectionTitle);
            childRecyclerView = itemView.findViewById(R.id.sectionItems);
        }
    }
}
