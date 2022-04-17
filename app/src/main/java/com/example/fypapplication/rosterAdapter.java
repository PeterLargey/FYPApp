package com.example.fypapplication;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class rosterAdapter extends FirestoreRecyclerAdapter<Roster, rosterAdapter.RosterViewHolder> {

    public rosterAdapter(@NonNull FirestoreRecyclerOptions<Roster> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RosterViewHolder holder, int position, @NonNull Roster model) {
        ImageView deleteButton = holder.itemView.findViewById(R.id.removeFromRoster);

        holder.name.setText(model.getName());
        holder.role.setText(model.getRole());
        holder.time.setText(model.getTime());

        String docId = getSnapshots().getSnapshot(position).getId();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String TAG = "TAG";
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Roster").document(docId);
                docRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Document " + docId + " removed from roster");
                        } else {
                            Log.d(TAG, "Document " + docId + "was not removed from roster");
                        }
                    }
                });

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditRosterItem.class);
                i.putExtra("name", model.getName());
                i.putExtra("role", model.getRole());
                i.putExtra("time", model.getTime());
                i.putExtra("date", model.getDate());
                i.putExtra("docId", docId);
                view.getContext().startActivity(i);
            }
        });

    }

    @NonNull
    @Override
    public RosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_roster_layout, parent, false);
        return new RosterViewHolder(v);
    }

    public class RosterViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView role;
        private TextView time;

        public RosterViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rosterStaffName);
            role = itemView.findViewById(R.id.rosterStaffRole);
            time = itemView.findViewById(R.id.rosteredTime);
        }
    }
}
