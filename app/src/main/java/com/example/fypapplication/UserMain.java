package com.example.fypapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class UserMain extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private NavigationBarView bottomNav;
    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        data = getIntent();
        String customerName = data.getStringExtra("user");
        getSupportActionBar().setTitle("Welcome " + customerName);
        mAuth = FirebaseAuth.getInstance();

        Bundle bundle = new Bundle();
        bundle.putString("customerName", customerName);

        Fragment startingFragment = new UserMenuFragment();
        startingFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.userFragmentContainer, startingFragment).commit();
        bottomNav = findViewById(R.id.userBottomNav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch(menuItem.getItemId()){
                    case(R.id.menu):
                        fragment = new UserMenuFragment();
                        fragment.setArguments(bundle);
                        break;
                    case(R.id.kidsMenu):
                        fragment = new KidsMenuFragment();
                        fragment.setArguments(bundle);
                        break;
                    case(R.id.reviews):
                        fragment = new UserReviewsFragment();
                        break;
                    case(R.id.addReview):
                        fragment = new UserAddReviewFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.userFragmentContainer,fragment).commit();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.userLogout){
            mAuth.signOut();
            finish();
            Intent i = new Intent(UserMain.this, MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}