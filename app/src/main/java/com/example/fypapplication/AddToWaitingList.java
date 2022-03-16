package com.example.fypapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddToWaitingList extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText name, guests, phoneNumber, waitTime;
    private Button submit;
    private final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_waiting_list);
        db = FirebaseFirestore.getInstance();

        name = findViewById(R.id.waitingListName);
        guests = findViewById(R.id.waitingListGuests);
        phoneNumber = findViewById(R.id.waitingListNumber);
        waitTime = findViewById(R.id.waitingListWaitTime);

        submit = findViewById(R.id.addToWaitingList);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customerName = name.getText().toString();
                String numberOfGuests = guests.getText().toString();
                String number = phoneNumber.getText().toString();
                String estimateWaitTime = waitTime.getText().toString();

                if(customerName.isEmpty() || numberOfGuests.isEmpty() || number.isEmpty() || estimateWaitTime.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Item added to the Waiting List", Toast.LENGTH_LONG).show();
                    inputDataIntoWaitingList(customerName, numberOfGuests, number, estimateWaitTime);
                    Intent i = new Intent(AddToWaitingList.this, HostMain.class);
                    startActivity(i);
                }
            }
        });

    }

    private void inputDataIntoWaitingList(String name, String numberOfGuests, String phoneNumber, String waitTime){
        DocumentReference docRef = db.collection("WaitingList").document();
        Map<String, Object> newWaitingListItem = new HashMap<>();
        newWaitingListItem.put("name", name);
        newWaitingListItem.put("numberOfGuests", numberOfGuests);
        newWaitingListItem.put("phoneNumber", phoneNumber);
        newWaitingListItem.put("waitTime", waitTime);
        docRef.set(newWaitingListItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Success, new waiting list item was added to the database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed, new waiting list item was not added to the database");
            }
        });


    }
}