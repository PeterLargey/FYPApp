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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditRosterItem extends AppCompatActivity {

    private final String TAG = "TAG";
    private FirebaseFirestore db;
    private AutoCompleteTextView name, role;
    private EditText date, time;
    private ArrayList<String> staffNames;
    private Button update;
    private String staffName, staffRole, rosteredDate, rosteredTime, docId;
    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_roster_item);
        db = FirebaseFirestore.getInstance();
        data = getIntent();
        staffName = data.getStringExtra("name");
        staffRole = data.getStringExtra("role");
        rosteredDate = data.getStringExtra("date");
        rosteredTime = data.getStringExtra("time");
        docId = data.getStringExtra("docId");

        name = findViewById(R.id.editRosteredName);
        role = findViewById(R.id.editRosteredRole);
        date = findViewById(R.id.editRosteredDate);
        time = findViewById(R.id.editRosteredTime);

        name.setText(staffName);
        role.setText(staffRole);
        date.setText(rosteredDate);
        time.setText(rosteredTime);

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

        update = findViewById(R.id.updateRosteredItem);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedName = name.getText().toString();
                String updatedRole = role.getText().toString();
                String updatedDate = date.getText().toString();
                String updatedTime = time.getText().toString();

                if(updatedName.isEmpty() || updatedRole.isEmpty() || updatedDate.isEmpty() || updatedTime.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else {
                    updateRosterData(updatedName, updatedRole, updatedDate, updatedTime);
                    Toast.makeText(getApplicationContext(), "Roster Updated", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(EditRosterItem.this, CurrentRoster.class);
                    i.putExtra("date", rosteredDate);
                    startActivity(i);
                }

            }
        });
    }

    private void getStaffNames() {
        db.collection("Staff").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                    name.setAdapter(nameAdapter);

                    name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private void updateRosterData(String name, String role, String date, String time) {
        DocumentReference docRef = db.collection("Roster").document(docId);
        Map<String, Object> edit = new HashMap<>();
        edit.put("name", name);
        edit.put("role", role);
        edit.put("date", date);
        edit.put("time", time);
        docRef.set(edit).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Roster document " + docId + " updated");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Roster document " + docId + " failed to update");
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
            Intent i = new Intent(EditRosterItem.this, CurrentRoster.class);
            i.putExtra("date", rosteredDate);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}