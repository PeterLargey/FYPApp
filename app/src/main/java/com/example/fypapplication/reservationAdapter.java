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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class reservationAdapter extends FirestoreRecyclerAdapter<Reservation, reservationAdapter.ResoViewHolder> {

    private AlertDialog.Builder builder;

    public reservationAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options){super(options);}

    @Override
    protected void onBindViewHolder(@NonNull ResoViewHolder holder, int position, @NonNull Reservation model) {

        ImageView deleteButton = holder.itemView.findViewById(R.id.deleteReservation);

        holder.name.setText(model.getName());
        holder.numberOfGuests.setText(model.getNumberOfGuests());
        holder.time.setText(model.getTime());

        String docId = getSnapshots().getSnapshot(position).getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditReservation.class);
                i.putExtra("docId", docId);
                i.putExtra("name", model.getName());
                i.putExtra("date", model.getDate());
                i.putExtra("time", model.getTime());
                i.putExtra("numberOfGuests", model.getNumberOfGuests());
                view.getContext().startActivity(i);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TAG = "TAG";
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Would you like to delete this reservation?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DocumentReference docRef = db.collection("Reservations").document(docId);
                                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "Reservation Deleted");
                                        Toast.makeText(view.getContext(), "Reservation Deleted", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Reservation failed to Delete");
                                        Toast.makeText(view.getContext(), "Reservation failed to Delete", Toast.LENGTH_LONG).show();
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
    public ResoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_reservation_layout, parent, false);
        return new ResoViewHolder(v);
    }


    public class ResoViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView numberOfGuests;
        private TextView time;

        public ResoViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.reservationName);
            numberOfGuests = itemView.findViewById(R.id.reservationNoOfGuests);
            time = itemView.findViewById(R.id.reservationTime);
        }
    }

}
