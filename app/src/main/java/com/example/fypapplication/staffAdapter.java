package com.example.fypapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class staffAdapter extends FirestoreRecyclerAdapter<Staff, staffAdapter.StaffViewHolder>{

    private AlertDialog.Builder builder;

    public staffAdapter(@NonNull FirestoreRecyclerOptions<Staff> options){
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull StaffViewHolder holder, int position, @NonNull Staff model) {
        ImageView deleteButton = holder.itemView.findViewById(R.id.deleteStaffMember);

        holder.name.setText(model.getFullName());
        holder.role.setText(model.getRole());

        String docId = getSnapshots().getSnapshot(position).getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditStaff.class);
                i.putExtra("docId", docId);
                i.putExtra("username", model.getUsername());
                i.putExtra("role", model.getRole());
                i.putExtra("password", model.getPassword());
                i.putExtra("wage", model.getWage());
                i.putExtra("phoneNum", model.getPhoneNum());
                i.putExtra("fullName", model.getFullName());

                view.getContext().startActivity(i);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Would you like to delete this staff account?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DocumentReference docRef = db.collection("Staff").document(docId);
                                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("TAG", "Staff Member Deleted");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "Staff Member failed to be Deleted");
                                    }
                                });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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

        private TextView name;
        private TextView role;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.staffName);
            role = itemView.findViewById(R.id.staffRole);
        }
    }



}
