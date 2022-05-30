package com.example.fypapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ServerOrderDetails extends AppCompatActivity {

    private Intent data;
    private TabLayout tabLayout;
    private final String TAG = "TAG";
    private String staffMember, role, staffName;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        getSupportActionBar().setTitle("Edit Order");
        db = FirebaseFirestore.getInstance();
        data = getIntent();
        List<MenuItem> items;
        items = new ArrayList<>();
        String docId = data.getStringExtra("docId");
        String total = data.getStringExtra("total");
        String tableNumber = data.getStringExtra("tableNo");
        staffMember = data.getStringExtra("staffMember");
        role = data.getStringExtra("role");
        staffName = data.getStringExtra("staffName");

        Log.d(TAG, "Staff Member " + staffMember);
        Log.d(TAG, "Role " + role);

        items = (ArrayList) data.getParcelableArrayListExtra("items");
        String note = data.getStringExtra("note");
        Log.d(TAG, items.toString());
        Bundle bundle = new Bundle();
        bundle.putString("docId", docId);
        bundle.putString("total", total);
        bundle.putString("tableNo", tableNumber);
        bundle.putString("staffMember", staffMember);
        bundle.putParcelableArrayList("items", data.getParcelableArrayListExtra("items"));
        bundle.putString("note", note);
        bundle.putString("role",role);
        bundle.putString("staffName", staffName);

        tabLayout = findViewById(R.id.orderDetailsTabLayout);
        Fragment startingFragment = new OrderInfoFragment();
        startingFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.orderDetailsFragmentContainer, startingFragment).commit();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch(tab.getPosition()){
                    case(0):
                        fragment = new OrderInfoFragment();
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
                getSupportFragmentManager().beginTransaction().replace(R.id.orderDetailsFragmentContainer, fragment).commit();
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
            db.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "Cart cleared");
                        for(QueryDocumentSnapshot snap: task.getResult()){
                            snap.getReference().delete();
                        }
                    } else {
                        Log.d(TAG, "Cart failed to clear");
                    }
                }
            });
            Intent i = new Intent(ServerOrderDetails.this, ServerMain.class);
            i.putExtra("staffMember", staffMember);
            i.putExtra("role", role);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}