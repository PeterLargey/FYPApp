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

public class allOrdersAdapter extends FirestoreRecyclerAdapter<Order, allOrdersAdapter.AllOrdersViewHolder> {

    private String role;
    private String staffMember;

    public allOrdersAdapter(@NonNull FirestoreRecyclerOptions<Order> options, String role, String staffMember){
        super(options);
        this.role = role;
        this.staffMember = staffMember;
    }

    @Override
    protected void onBindViewHolder(@NonNull AllOrdersViewHolder holder, int position, @NonNull Order model) {
        holder.tableNo.setText("Table No " + model.getTableNo());
        //holder.orderId.setText(model.getOrderId());
        holder.timestamp.setText(model.getTimestamp().toString());
        holder.serverName.setText(model.getStaffMember());

        String docId = getSnapshots().getSnapshot(position).getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(role.equalsIgnoreCase("server")){
                    Intent i = new Intent(view.getContext(), ServerOrderDetails.class);
                    List<MenuItem> itemList;
                    itemList = model.getItems();
                    i.putExtra("docId", docId);
                    i.putExtra("tableNo", model.getTableNo());
                    i.putExtra("total", model.getTotal());
                    i.putExtra("role", role);
                    i.putExtra("staffMember", staffMember);
                    i.putParcelableArrayListExtra("items", (ArrayList) itemList);
                    //i.putExtra("items", (Parcelable) model.getItems());
                    i.putExtra("staffMember", model.getStaffMember());
                    i.putExtra("note", model.getNote());

                    view.getContext().startActivity(i);

                }else if(role.equalsIgnoreCase("manager")){
                    Intent i = new Intent(view.getContext(), ManagerOrderDetails.class);
                    List<MenuItem> itemList;
                    itemList = model.getItems();
                    i.putExtra("docId", docId);
                    i.putExtra("tableNo", model.getTableNo());
                    i.putExtra("total", model.getTotal());
                    i.putParcelableArrayListExtra("items", (ArrayList) itemList);
                    //i.putExtra("items", (Parcelable) model.getItems());
                    i.putExtra("staffMember", model.getStaffMember());
                    i.putExtra("role", role);
                    i.putExtra("note", model.getNote());

                    view.getContext().startActivity(i);
                }
            }
        });

    }

    @NonNull
    @Override
    public AllOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_manager_order_layout, parent, false);
        return new AllOrdersViewHolder(v);
    }


    public class AllOrdersViewHolder extends RecyclerView.ViewHolder{

        private TextView tableNo;
        //private TextView orderId;
        private TextView timestamp;
        private TextView serverName;
        public AllOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            tableNo = itemView.findViewById(R.id.allOrderTableNo);
            //orderId = itemView.findViewById(R.id.allOrderId);
            timestamp = itemView.findViewById(R.id.allOrderTimestamp);
            serverName = itemView.findViewById(R.id.serverName);
        }
    }
}
