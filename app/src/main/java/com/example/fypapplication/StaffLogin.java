package com.example.fypapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Source;

import java.util.HashMap;
import java.util.Map;

public class StaffLogin extends AppCompatActivity {

    private static final String TAG = "TAG";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Map<String, Object> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);
        getSupportActionBar().setTitle("Staff Login");
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EditText username = findViewById(R.id.usernameLogin);
        EditText password = findViewById(R.id.passwordStaffLogin);

        Button login = findViewById(R.id.staffLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameString = username.getText().toString();
                String passwordString = password.getText().toString();
                if(usernameString.isEmpty() || passwordString.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                }else{
                    mAuth.signInWithEmailAndPassword(usernameString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String userId = mAuth.getCurrentUser().getUid();


                                Log.d(TAG, "Current user ID: " + userId);
                                DocumentReference docRef = db.collection("Staff").document(userId);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot snapshot = task.getResult();
                                            if(snapshot.exists()){
                                                data = snapshot.getData();
                                                Object[] values = data.values().toArray();
                                                String role = values[1].toString();
                                                String username = values[2].toString();
                                                Log.d(TAG, "Document snapshot data: " + snapshot.getData());
                                                Log.d(TAG, "Document snapshot data: " + data.values());
                                                Log.d(TAG, "Role: " + role);

                                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                                                //admin
                                                if(role.equalsIgnoreCase("manager")){
                                                    Intent i = new Intent(StaffLogin.this,ManagerMain.class );
                                                    i.putExtra("staffMember", username);
                                                    i.putExtra("role", role);
                                                    startActivity(i);
                                                    //server
                                                } else if(role.equalsIgnoreCase("server")){
                                                    Intent i = new Intent(StaffLogin.this, ServerMain.class);
                                                    i.putExtra("staffMember", username);
                                                    i.putExtra("role", role);
                                                    startActivity(i);
                                                    //host
                                                } else if(role.equalsIgnoreCase("host")){
                                                    Intent i = new Intent(StaffLogin.this, HostMain.class);
                                                    startActivity(i);
                                                    //owner
                                                } else if(role.equalsIgnoreCase("owner")){
                                                    Intent i = new Intent(StaffLogin.this, OwnerMain.class);
                                                    startActivity(i);
                                                    //manager
                                                } else if(role.equalsIgnoreCase("chef")){
                                                    Intent i = new Intent(StaffLogin.this, ChefMain.class);
                                                    i.putExtra("role", role);
                                                    startActivity(i);
                                                }
                                                Log.d(TAG, "onSuccess: staff profile has been logged in " + userId);

                                            } else {
                                                Log.d(TAG, "No such Doc");
                                            }
                                        } else {
                                            Log.d(TAG, "Failed with: " + task.getException());
                                        }
                                    }
                                });

                            }else {
                                Toast.makeText(getApplicationContext(), "Login Failed, check username!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stafflogin_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.returnHome){
            Intent i = new Intent(StaffLogin.this, MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

}