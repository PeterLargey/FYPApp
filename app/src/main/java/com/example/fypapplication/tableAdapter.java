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

public class tableAdapter extends FirestoreRecyclerAdapter<Table, tableAdapter.TableViewHolder> {

    private String role;

    public tableAdapter(@NonNull FirestoreRecyclerOptions<Table> options, String role){
        super(options);
        this.role = role;
    }

    @Override
    protected void onBindViewHolder(@NonNull TableViewHolder holder, int position, @NonNull Table model) {

        String tableNoHolder = String.valueOf(model.getTableNo());

        holder.tableNo.setText(tableNoHolder);
        String docId = getSnapshots().getSnapshot(position).getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CreateOrder.class);
                i.putExtra("tableNo", tableNoHolder);
                i.putExtra("docId", docId);
                i.putExtra("role", role);

                view.getContext().startActivity(i);
            }
        });

    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_table_layout, parent, false);
        return new TableViewHolder(v);
    }

    public class TableViewHolder extends RecyclerView.ViewHolder{

        private TextView tableNo;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            tableNo = itemView.findViewById(R.id.tableNumber);
        }
    }



}
