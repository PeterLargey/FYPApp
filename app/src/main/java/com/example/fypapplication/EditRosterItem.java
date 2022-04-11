package com.example.fypapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class EditRosterItem extends AppCompatActivity {

    private final String TAG = "TAG";
    private FirebaseFirestore db;
    private EditText name, role, date, time;
    private Button update;
    private String staffName, staffRole, rosteredDate, rosteredTime, userName, docId;
    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_roster_item);
        db = FirebaseFirestore.getInstance();
        data = getIntent();
        staffName = data.getStringExtra("name");
        staffRole = data.getStringExtra("role");
        rosteredDate = data.getStringExtra("date");
        rosteredTime = data.getStringExtra("time");
        userName = data.getStringExtra("userName");
        docId = data.getStringExtra("docId");

        name = findViewById(R.id.editRosteredName);
        role = findViewById(R.id.editRosteredRole);
        date = findViewById(R.id.editRosteredDate);
        time = findViewById(R.id.editRosteredTime);

        name.setText(staffName);
        role.setText(staffRole);
        date.setText(rosteredDate);
        time.setText(rosteredTime);

        update = findViewById(R.id.updateRosteredItem);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedName = name.getText().toString();
                String updatedRole = role.getText().toString();
                String updatedDate = date.getText().toString();
                String updatedTime = time.getText().toString();

                if(updatedName.isEmpty() || updatedRole.isEmpty() || updatedDate.isEmpty() || updatedTime.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else {
                    updateRosterData(updatedName, updatedRole, updatedDate, updatedTime, userName);
                    Toast.makeText(getApplicationContext(), "Roster Updated", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(EditRosterItem.this, CurrentRoster.class);
                    i.putExtra("date", rosteredDate);
                    startActivity(i);
                }

            }
        });
    }

    private void updateRosterData(String name, String role, String date, String time, String userName) {
        DocumentReference docRef = db.collection("Roster").document(docId);
        Map<String, Object> edit = new HashMap<>();
        edit.put("name", name);
        edit.put("role", role);
        edit.put("date", date);
        edit.put("time", time);
        edit.put("userName", userName);
        docRef.set(edit).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Roster document " + docId + " updated");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Roster document " + docId + " failed to update");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_staff_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.backToStaffMain){
            Intent i = new Intent(EditRosterItem.this, CurrentRoster.class);
            i.putExtra("date", rosteredDate);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}