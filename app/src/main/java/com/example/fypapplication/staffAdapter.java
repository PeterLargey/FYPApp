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

public class staffAdapter extends FirestoreRecyclerAdapter<Staff, staffAdapter.StaffViewHolder>{

    public staffAdapter(@NonNull FirestoreRecyclerOptions<Staff> options){
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull StaffViewHolder holder, int position, @NonNull Staff model) {
        ImageView editButton = holder.itemView.findViewById(R.id.editStaffMember);

        holder.username.setText(model.getUsername());
        holder.role.setText(model.getRole());

        String docId = getSnapshots().getSnapshot(position).getId();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditStaff.class);
                i.putExtra("docId", docId);
                i.putExtra("username", model.getUsername());
                i.putExtra("role", model.getRole());
                i.putExtra("password", model.getPassword());

                view.getContext().startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_view_staff_layout, parent, false);
        return new StaffViewHolder(v);
    }


    public class StaffViewHolder extends RecyclerView.ViewHolder{

        private TextView username;
        private TextView role;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.staffUsername);
            role = itemView.findViewById(R.id.staffRole);
        }
    }



}
