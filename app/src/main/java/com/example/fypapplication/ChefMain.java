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

public class ChefMain extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private NavigationBarView bottomNav;
    private final String TAG = "TAG";
    private Intent fromLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_main);
        getSupportActionBar().setTitle("Chef Home Screen");

        mAuth = FirebaseAuth.getInstance();
        fromLogin = getIntent();

        String role = fromLogin.getStringExtra("role");
        Log.d(TAG, "Role: " + role);

        Bundle bundle = new Bundle();
        bundle.putString("role", role);

        Fragment startingFragment = new ChefMenuFragment();
        startingFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.chefFragmentContainer, startingFragment).commit();
        bottomNav = findViewById(R.id.chefBottomNav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch(menuItem.getItemId()){
                    case(R.id.chefMenu):
                        fragment = new ChefMenuFragment();
                        fragment.setArguments(bundle);
                    case(R.id.inventory):
                        fragment = new CurrentInventoryFragment();
                        fragment.setArguments(bundle);
                    case(R.id.delivery):
                        fragment = new DeliveryIntakeFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.chefFragmentContainer, fragment).commit();
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
            Intent i = new Intent(ChefMain.this, MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}