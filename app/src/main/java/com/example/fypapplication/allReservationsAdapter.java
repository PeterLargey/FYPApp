package com.example.fypapplication;

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


public class allReservationsAdapter extends FirestoreRecyclerAdapter<Reservation, allReservationsAdapter.AllResosViewHolder> {

    public allReservationsAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options){super(options);}

    @Override
    protected void onBindViewHolder(@NonNull AllResosViewHolder holder, int position, @NonNull Reservation model) {

        ImageView deleteButton = holder.itemView.findViewById(R.id.deleteReservationAllLayout);
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
        holder.name.setText(model.getName());
        holder.numberOfGuests.setText(model.getNumberOfGuests());

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
        });
    }

    @NonNull
    @Override
    public AllResosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_all_reservations_layout, parent, false);
        return new AllResosViewHolder(v);
    }

    public class AllResosViewHolder extends RecyclerView.ViewHolder{

        private TextView date;
        private TextView time;
        private TextView name;
        private TextView numberOfGuests;

        public AllResosViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.allResosDate);
            time = itemView.findViewById(R.id.allResosTime);
            name = itemView.findViewById(R.id.allResosName);
            numberOfGuests = itemView.findViewById(R.id.allResosGuests);
        }
    }
}
