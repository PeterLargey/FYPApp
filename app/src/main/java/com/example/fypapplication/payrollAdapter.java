package com.example.fypapplication;

import android.annotation.SuppressLint;
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

public class payrollAdapter extends FirestoreRecyclerAdapter<Staff, payrollAdapter.PayrollViewHolder> {

    public payrollAdapter(@NonNull FirestoreRecyclerOptions<Staff> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull PayrollViewHolder holder, int position, @NonNull Staff model) {
        ImageView editButton = holder.itemView.findViewById(R.id.ownerEditStaffMember);

        holder.username.setText(model.getUsername());
        holder.role.setText(model.getRole());
        holder.wage.setText("€" + model.getWage());

        String docId = getSnapshots().getSnapshot(position).getId();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), OwnerEditStaff.class);
                i.putExtra("docId", docId);
                i.putExtra("username", model.getUsername());
                i.putExtra("role", model.getRole());
                i.putExtra("wage", model.getWage());
                i.putExtra("password", model.getPassword());

                view.getContext().startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public PayrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_payroll_layout, parent, false);
        return new PayrollViewHolder(v);
    }

    public class PayrollViewHolder extends RecyclerView.ViewHolder{

        private TextView username;
        private TextView role;
        private TextView wage;

        public PayrollViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.ownerStaffName);
            role = itemView.findViewById(R.id.ownerStaffRole);
            wage = itemView.findViewById(R.id.ownerStaffWage);
        }
    }
}
