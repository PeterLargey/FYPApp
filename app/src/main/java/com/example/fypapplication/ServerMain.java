package com.example.fypapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class ServerMain extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private NavigationBarView bottomNav;
    private final String TAG = "TAG";
    private Intent fromLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_main);
        getSupportActionBar().setTitle("Server Home Screen");

        mAuth = FirebaseAuth.getInstance();
        fromLogin = getIntent();

        String staffMember = fromLogin.getStringExtra("staffMember");
        Log.d(TAG, "Server Main User: " + staffMember);

        String role = fromLogin.getStringExtra("role");
        Log.d(TAG, "Role: " + role);

        Bundle bundle = new Bundle();
        bundle.putString("staffMember", staffMember);
        bundle.putString("role", role);

        Fragment startingFragment = new ServerOrderFragment();
        startingFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.serverFragmentContainer,startingFragment).commit();
        bottomNav = findViewById(R.id.serverBottomNav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch(menuItem.getItemId()){
                    case(R.id.myOrders):
                        fragment = new ServerOrderFragment();
                        fragment.setArguments(bundle);
                        break;
                    case(R.id.tables):
                        fragment = new ServerTablesFragment();
                        fragment.setArguments(bundle);
                        break;
                    case(R.id.takePayment):
                        fragment = new AllOrdersFragment();
                        fragment.setArguments(bundle);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.serverFragmentContainer,fragment).commit();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.host_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.hostLogout){
            mAuth.signOut();
            finish();
            Intent i = new Intent(ServerMain.this, MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}