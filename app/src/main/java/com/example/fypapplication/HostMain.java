package com.example.fypapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HostMain extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private NavigationBarView bottomNav;
    private final String TAG = "TAG";
    private Intent fromLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_main);
        getSupportActionBar().setTitle("Host Home Screen");

        mAuth = FirebaseAuth.getInstance();
        fromLogin = getIntent();

        String role = fromLogin.getStringExtra("role");
        Log.d(TAG, "Role: " + role);

        Bundle bundle = new Bundle();
        bundle.putString("role", role);

        Fragment startingFragment = new CurrentReservationsFragment();
        startingFragment.setArguments(bundle);


        getSupportFragmentManager().beginTransaction().replace(R.id.hostFragmentContainer,startingFragment).commit();
        bottomNav = findViewById(R.id.hostBottomNav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch(menuItem.getItemId()){
                    case(R.id.currentReservations):
                        fragment = new CurrentReservationsFragment();
                        fragment.setArguments(bundle);
                        break;
                    case(R.id.allReservations):
                        fragment = new AllReservationsFragment();
                        fragment.setArguments(bundle);
                        break;
                    case(R.id.addReservation):
                        fragment = new AddReservationFragment();
                        fragment.setArguments(bundle);
                        break;
                    case(R.id.waitingList):
                        fragment = new WaitingListFragment();
                        fragment.setArguments(bundle);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.hostFragmentContainer,fragment).commit();
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
            Intent i = new Intent(HostMain.this, MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}