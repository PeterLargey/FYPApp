package com.example.fypapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class tableAdapter extends FirestoreRecyclerAdapter<Table, tableAdapter.TableViewHolder> {

    private String role;
    private String staffMember;
    private FirebaseFirestore db;

    public tableAdapter(@NonNull FirestoreRecyclerOptions<Table> options, String role, String staffMember){
        super(options);
        this.role = role;
        this.staffMember = staffMember;
    }

    @Override
    protected void onBindViewHolder(@NonNull TableViewHolder holder, int position, @NonNull Table model) {
        db = FirebaseFirestore.getInstance();
        ImageView tableIcon = holder.itemView.findViewById(R.id.tableIcon);

        String tableNoHolder = String.valueOf(model.getTableNo());
        holder.tableNo.setText(tableNoHolder);
        String docId = getSnapshots().getSnapshot(position).getId();

        db.collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Map<String, Object> docData = doc.getData();
                        String tableNo = (String) docData.get("tableNo");
                        if(holder.tableNo.getText().toString().equalsIgnoreCase(tableNo)) {
                            tableIcon.setColorFilter(Color.RED);
                            holder.itemView.setEnabled(false);
                        }
                    }
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CreateOrder.class);
                i.putExtra("tableNo", tableNoHolder);
                i.putExtra("docId", docId);
                i.putExtra("role", role);
                i.putExtra("staffMember", staffMember);

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
