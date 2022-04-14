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
    private EditText username, role, password;
    private Button button;
    private Intent data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff);
        getSupportActionBar().setTitle("Edit Staff Info");

        db = FirebaseFirestore.getInstance();
        data = getIntent();

        username = findViewById(R.id.editStaffName);
        role = findViewById(R.id.editStaffRole);
        password = findViewById(R.id.editStaffPassword);

        String usernameString = data.getStringExtra("username");
        String roleString = data.getStringExtra("role");
        String passwordString = data.getStringExtra("password");
        String wage = data.getStringExtra("wage");
        Log.d(TAG, "Wage: " + wage);

        String docId = data.getStringExtra("docId");

        username.setText(usernameString);
        role.setText(roleString);
        password.setText(passwordString);

        button = findViewById(R.id.updateStaff);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedUsername = username.getText().toString();
                String updatedRole = role.getText().toString();
                String updatedPassword = password.getText().toString();

                if(updatedUsername.isEmpty() || updatedRole.isEmpty() || updatedPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else if(updatedPassword.length() < 6){
                    password.setError("Please enter at least 6 characters");
                } else {
                    DocumentReference docRef = db.collection("Staff").document(docId);
                    Map<String, Object> edit = new HashMap<>();
                    edit.put("username", updatedUsername);
                    edit.put("role", updatedRole);
                    edit.put("password", updatedPassword);
                    edit.put("wage", wage);
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