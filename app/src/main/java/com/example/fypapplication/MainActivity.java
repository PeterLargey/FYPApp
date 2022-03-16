package com.example.fypapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private final String TAG = "TAG";
    private String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Menu Now");
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EditText email = findViewById(R.id.loginEmail);
        EditText password = findViewById(R.id.loginPassword);
        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.navigateToRegister);
        Button forgotPassword = findViewById(R.id.forgotPassword);

        final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                if(emailString.isEmpty() || passwordString.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                }else{
                    loadingDialog.startLoadingDialog();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAuth.signInWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        checkEmailVerification();
                                        loadingDialog.dismissLoadingDialog();
                                    }else {
                                        loadingDialog.dismissLoadingDialog();
                                        Toast.makeText(getApplicationContext(), "Login Failed, account doesn't exist", Toast.LENGTH_LONG).show();
                                        mAuth.signOut();
                                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                                        startActivity(i);
                                    }
                                }
                            });
                        }
                    }, 4000);

                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(i);
            }
        });

    }

    private void checkEmailVerification(){
        FirebaseUser customer = mAuth.getCurrentUser();

        if(customer != null){
            if(customer.isEmailVerified()){
                String userId = customer.getUid();

                db.collection("Customers").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> data = documentSnapshot.getData();
                        //Log.d(TAG, "Retrieved data map");
                        //Log.d(TAG, String.valueOf(data));
                        userName = (String) data.get("name");
                        Log.d(TAG, userName);
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(MainActivity.this, UserMain.class);
                        i.putExtra("user", userName);
                        startActivity(i);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failed to retrieve customer name");
                    }
                });

            }else {
                Toast.makeText(getApplicationContext(), "Verify your email address to login", Toast.LENGTH_LONG).show();
                mAuth.signOut();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Account could not be found", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.staffLogin){
            Intent i = new Intent(MainActivity.this, StaffLogin.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}