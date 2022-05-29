package com.example.fypapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class waitingListAdapter extends FirestoreRecyclerAdapter<WaitingList, waitingListAdapter.WaitingViewHolder> {

    private AlertDialog.Builder builder;
    private AlertDialog.Builder deleteButtonBuilder;
    private final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    public waitingListAdapter(@NonNull FirestoreRecyclerOptions<WaitingList> options){super(options);}

    @Override
    protected void onBindViewHolder(@NonNull WaitingViewHolder holder, int position, @NonNull WaitingList model) {

        ImageView deleteButton = holder.itemView.findViewById(R.id.deleteCustomerFromWaiting);

        holder.name.setText(model.getName());
        holder.guests.setText(model.getNumberOfGuests());
        holder.time.setText(model.getWaitTime());

        String docId = getSnapshots().getSnapshot(position).getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String TAG = "TAG";
                if(checkPermission(view.getContext(), Manifest.permission.SEND_SMS)){
                    Log.d(TAG, "Permission accepted");
                } else {
                    ActivityCompat.requestPermissions((Activity) view.getContext(),
                            new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
                }
                builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Would you like to notify this customer?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String message = "Your Table is ready!";

                                if(checkPermission(view.getContext(), Manifest.permission.SEND_SMS)){
                                    SmsManager smsManager = SmsManager.getDefault();
                                    String number = "+353877032726";
                                    Log.d(TAG, number);
                                    smsManager.sendTextMessage(model.getPhoneNumber(), null, message, null, null);
                                    Toast.makeText(view.getContext(), "Message Sent", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(view.getContext(), "Message Failed to send, Permission Denied ", Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TAG = "TAG";
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                deleteButtonBuilder = new AlertDialog.Builder(view.getContext());
                deleteButtonBuilder.setMessage("Do you want to delete this item from the Waiting List?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DocumentReference docRef = db.collection("WaitingList").document(docId);
                                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "Waiting List Item Deleted");
                                        Toast.makeText(view.getContext(), "Waiting List Item Deleted", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Waiting List item failed to Delete");
                                        Toast.makeText(view.getContext(), "Item failed to Delete", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = deleteButtonBuilder.create();
                alertDialog.show();

            }
        });


    }

    @NonNull
    @Override
    public WaitingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_waitinglist_layout, parent, false);
        return new WaitingViewHolder(v);
    }


    public class WaitingViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView guests;
        private TextView time;

        public WaitingViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.waitingName);
            guests = itemView.findViewById(R.id.waitingGuests);
            time = itemView.findViewById(R.id.waitingTime);
        }
    }

    private boolean checkPermission(Context context, String permission){
        int check = ContextCompat.checkSelfPermission(context, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

}
