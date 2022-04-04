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
import com.google.android.material.tabs.TabLayout;

public class CreateOrder extends AppCompatActivity {

    private Intent data;
    private TabLayout tabLayout;
    private final String TAG = "TAG";
    private String role, staffMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        getSupportActionBar().setTitle("Create Order Screen");
        data = getIntent();

        String tableNumber = data.getStringExtra("tableNo");
        role = data.getStringExtra("role");
        staffMember = data.getStringExtra("staffMember");
        Log.d(TAG, "Role: "+ role);
        Bundle bundle = new Bundle();
        bundle.putString("tableNo", tableNumber);
        bundle.putString("role", role);
        bundle.putString("staffMember", staffMember);

        tabLayout = findViewById(R.id.tabLayout);
        Fragment startingFragment = new NewOrderFragment();
        startingFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.createOrderFragmentContainer, startingFragment).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch(tab.getPosition()){
                    case(0):
                        fragment = new NewOrderFragment();
                        fragment.setArguments(bundle);
                        break;
                    case(1):
                        fragment = new SpecialsFragment();
                        break;
                    case(2):
                        fragment = new StartersFragment();
                        break;
                    case(3):
                        fragment = new MainsFragment();
                        break;
                    case(4):
                        fragment = new DessertsFragment();
                        break;
                    case(5):
                        fragment = new DrinksFragment();
                        break;
                    case(6):
                        fragment = new KidsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.createOrderFragmentContainer,fragment).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
            Intent i;
            if(role.equalsIgnoreCase("server")){
                i = new Intent(CreateOrder.this, ServerMain.class);
            } else {
                i = new Intent(CreateOrder.this, ManagerMain.class);
            }
            i.putExtra("role", role);
            i.putExtra("staffMember", staffMember);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}