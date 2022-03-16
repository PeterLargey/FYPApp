package com.example.fypapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class staffDeleteAdapter extends FirestoreRecyclerAdapter<Staff, staffDeleteAdapter.staffDeleteViewHolder> {

    private AlertDialog.Builder builder;

    public staffDeleteAdapter(@NonNull FirestoreRecyclerOptions<Staff> options){super(options);}

    @Override
    protected void onBindViewHolder(@NonNull staffDeleteViewHolder holder, int position, @NonNull Staff model) {
        ImageView deleteButton = holder.itemView.findViewById(R.id.deleteStaffMember);

        holder.name.setText(model.getUsername());

        String docId = getSnapshots().getSnapshot(position).getId();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TAG = "TAG";
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
                                        Log.d(TAG, "Staff Account " + docId + " Deleted");
                                        Toast.makeText(view.getContext(), "Staff Account Deleted", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Staff Account " + docId + " failed to delete");
                                        Toast.makeText(view.getContext(), "Staff Account failed to Delete", Toast.LENGTH_LONG).show();
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
    public staffDeleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_delete_staff_layout, parent, false);
        return new staffDeleteViewHolder(v);
    }

    public class staffDeleteViewHolder extends RecyclerView.ViewHolder {

        private TextView name;

        public staffDeleteViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.staffName);
        }
    }

}
