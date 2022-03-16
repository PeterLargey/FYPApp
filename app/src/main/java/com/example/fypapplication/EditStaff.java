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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditStaff extends AppCompatActivity {

    private static final String TAG = "TAG";
    private FirebaseFirestore db;
    private TextView username, role, password;
    private EditText eUsername, eRole, ePassword;
    private Button button;
    private Intent data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff);
        getSupportActionBar().setTitle("Edit Staff Info");

        db = FirebaseFirestore.getInstance();
        data = getIntent();

        eUsername = findViewById(R.id.editStaffName);
        eRole = findViewById(R.id.editStaffRole);
        ePassword = findViewById(R.id.editStaffPassword);

        username = findViewById(R.id.staffCurrentUsername);
        role = findViewById(R.id.staffCurrentRole);
        password = findViewById(R.id.staffCurrentPassword);

        String cUsername = data.getStringExtra("username");
        String cRole = data.getStringExtra("role");
        String cPassword = data.getStringExtra("password");

        String docId = data.getStringExtra("docId");

        username.setText(cUsername);
        role.setText(cRole);
        password.setText(cPassword);

        button = findViewById(R.id.updateStaff);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uUsername = eUsername.getText().toString();
                String uRole = eRole.getText().toString();
                String uPassword = ePassword.getText().toString();

                if(uUsername.isEmpty() || uRole.isEmpty() || uPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else if(uPassword.length() < 6){
                    ePassword.setError("Please enter at least 6 characters");
                } else {
                    DocumentReference docRef = db.collection("Staff").document(docId);
                    Map<String, Object> edit = new HashMap<>();
                    edit.put("username", uUsername);
                    edit.put("role", uRole);
                    edit.put("password", uPassword);
                    docRef.set(edit).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: staff profile has been edited " + docId);
                            Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(EditStaff.this, ManagerMain.class);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Failed: staff profile has not been edited. Check document: " + docId);
                            Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
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
            Intent i = new Intent(EditStaff.this, ManagerMain.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}