package com.example.fypapplication;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;


public class orderAdapter extends FirestoreRecyclerAdapter<Order, orderAdapter.OrderViewHolder> {

    private String role;
    private String staffMember;

    public orderAdapter(@NonNull FirestoreRecyclerOptions<Order> options, String role, String staffMember){
        super(options);
        this.role = role;
        this.staffMember = staffMember;
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Order model) {

        holder.tableNum.setText(model.getTableNo());
        holder.timestamp.setText(model.getTimestamp());

        String docId = getSnapshots().getSnapshot(position).getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<MenuItem> itemList;
                itemList = model.getItems();
                Intent i = new Intent(view.getContext(), ServerOrderDetails.class);
                i.putExtra("docId", docId);
                i.putExtra("tableNo", model.getTableNo());
                i.putExtra("total", model.getTotal());
                i.putParcelableArrayListExtra("items", (ArrayList) itemList);
                i.putExtra("note", model.getNote());
                i.putExtra("role", role);
                i.putExtra("staffMember", staffMember);
                i.putExtra("staffName", model.getStaffName());

                view.getContext().startActivity(i);
            }
        });

    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_server_order_layout, parent, false);
        return new OrderViewHolder(v);
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{

        private TextView tableNum;
        private TextView timestamp;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tableNum = itemView.findViewById(R.id.orderTableNo);
            timestamp = itemView.findViewById(R.id.orderTimestamp);
        }
    }

}
