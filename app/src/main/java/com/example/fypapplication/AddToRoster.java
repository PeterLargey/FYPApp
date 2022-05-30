package com.example.fypapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddToRoster extends AppCompatActivity {

    private FirebaseFirestore db;
    private AutoCompleteTextView staff, role;
    private EditText date, time;
    private Intent data;
    private String rosterDate;
    private Button addToRoster;
    private ArrayList<String> staffNames;
    private final String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_roster);
        getSupportActionBar().setTitle("Add Employee to Roster");
        db = FirebaseFirestore.getInstance();
        data = getIntent();
        rosterDate = data.getStringExtra("date");

        staff = findViewById(R.id.rosterStaffMember);
        role = findViewById(R.id.inputStaffMemberRole);
        date =  findViewById(R.id.inputRosteredDate);
        time = findViewById(R.id.inputRosteredTime);
        addToRoster = findViewById(R.id.inputRosteredItem);

        date.setText(rosterDate);

        String[] roleType = new String[4];
        roleType[0] = "Host";
        roleType[1] = "Server";
        roleType[2] = "Manager";
        roleType[3] = "Chef";

        ArrayAdapter<String> roleAdapter = new ArrayAdapter<String>(this, R.layout.itemtype_dropdown_item, roleType);
        role.setAdapter(roleAdapter);

        role.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(view.getContext(), "Role Selected " + item, Toast.LENGTH_LONG).show();
            }
        });

        getStaffNames();

        addToRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameString = staff.getText().toString();
                String roleString = role.getText().toString();
                String dateString = date.getText().toString();
                String timeString = time.getText().toString();

                if(nameString.isEmpty() || roleString.isEmpty() || dateString.isEmpty() || timeString.isEmpty()){
                    Toast.makeText(view.getContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else {
                    inputDataIntoRoster(nameString, roleString, dateString, timeString);
                    Toast.makeText(view.getContext(), "Staff added to Roster", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(view.getContext(), CurrentRoster.class);
                    i.putExtra("date", rosterDate);
                    view.getContext().startActivity(i);
                }
            }
        });




    }

    private void inputDataIntoRoster(String nameString, String roleString, String dateString, String timeString) {
        DocumentReference docRef = db.collection("Roster").document();
        Map<String, Object> newRosterItem = new HashMap<>();
        newRosterItem.put("name", nameString);
        newRosterItem.put("role", roleString);
        newRosterItem.put("date", dateString);
        newRosterItem.put("time", timeString);

        docRef.set(newRosterItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Item added to the Roster database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Item failed to be added to the Roster database");
            }
        });

    }

    private void getStaffNames() {
        db.collection("Staff").whereNotEqualTo("role", "owner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    staffNames = new ArrayList<>();
                    for(QueryDocumentSnapshot doc: task.getResult()){
                        Map<String, Object> docData = doc.getData();
                        String name = (String) docData.get("fullName");
                        staffNames.add(name);
                    }

                    String[] names = new String[staffNames.size()];
                    names = staffNames.toArray(names);
                    for(String s : names){
                        Log.d(TAG, "Names: " + s);
                    }

                    ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdown_names, names);
                    staff.setAdapter(nameAdapter);

                    staff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            String item = adapterView.getItemAtPosition(position).toString();
                            Toast.makeText(view.getContext(), "Staff Member Selected " + item, Toast.LENGTH_LONG).show();
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
            Intent i = new Intent(AddToRoster.this, CurrentRoster.class);
            i.putExtra("date", rosterDate);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}