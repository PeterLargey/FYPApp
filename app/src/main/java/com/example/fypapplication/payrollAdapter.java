package com.example.fypapplication;

import android.annotation.SuppressLint;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class payrollAdapter extends FirestoreRecyclerAdapter<Staff, payrollAdapter.PayrollViewHolder> {

    private AlertDialog.Builder builder;

    public payrollAdapter(@NonNull FirestoreRecyclerOptions<Staff> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PayrollViewHolder holder, int position, @NonNull Staff model) {
        ImageView deleteButton = holder.itemView.findViewById(R.id.ownerDeleteStaffMember);

        holder.name.setText(model.getFullName());
        String role = model.getRole();
        String firstLetter = role.substring(0,1);
        firstLetter = firstLetter.toUpperCase();
        String remainingLetters = role.substring(1, role.length());
        role = firstLetter + remainingLetters;
        holder.role.setText(role);
        holder.wage.setText("€" + model.getWage());

        String docId = getSnapshots().getSnapshot(position).getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), OwnerEditStaff.class);
                i.putExtra("docId", docId);
                i.putExtra("username", model.getUsername());
                i.putExtra("role", model.getRole());
                i.putExtra("wage", model.getWage());
                i.putExtra("password", model.getPassword());
                i.putExtra("phoneNum", model.getPhoneNum());
                i.putExtra("fullName", model.getFullName());

                view.getContext().startActivity(i);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
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
    public PayrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_payroll_layout, parent, false);
        return new PayrollViewHolder(v);
    }

    public class PayrollViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView role;
        private TextView wage;

        public PayrollViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ownerStaffName);
            role = itemView.findViewById(R.id.ownerStaffRole);
            wage = itemView.findViewById(R.id.ownerStaffWage);
        }
    }
}
