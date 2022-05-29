package com.example.fypapplication;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditInventoryItem extends AppCompatActivity {

    private FirebaseFirestore db;
    private String role, itemName, itemDesc, itemUnits, itemExpiry, docId;
    private EditText name, desc, expiry, units;
    private Button updateItem;
    private Intent data;
    private final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventory_item);
        getSupportActionBar().setTitle("Edit Inventory Item");
        db = FirebaseFirestore.getInstance();
        data = getIntent();

        role = data.getStringExtra("role");
        itemName = data.getStringExtra("name");
        itemDesc = data.getStringExtra("desc");
        itemUnits = data.getStringExtra("units");
        itemExpiry = data.getStringExtra("expiry");
        docId = data.getStringExtra("docId");

        name = findViewById(R.id.editInventoryItemName);
        desc = findViewById(R.id.editInventoryItemDesc);
        expiry = findViewById(R.id.editInventoryItemExpiry);
        units = findViewById(R.id.editInventoryItemUnits);

        name.setText(itemName);
        desc.setText(itemDesc);
        expiry.setText(itemExpiry);
        units.setText(itemUnits);

        updateItem = findViewById(R.id.updateInventoryItem);

        updateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameString = name.getText().toString();
                String descString = desc.getText().toString();
                String expiryString = expiry.getText().toString();
                String unitString = units.getText().toString();

                if(nameString.isEmpty() || descString.isEmpty() || expiryString.isEmpty() || unitString.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else {
                    updateInventoryItem(nameString, descString, expiryString, unitString);
                    Toast.makeText(getApplicationContext(), "Inventory Item Updated", Toast.LENGTH_LONG).show();
                    Intent i;
                    if(role.equalsIgnoreCase("chef")){
                        i = new Intent(EditInventoryItem.this, ChefMain.class);
                        i.putExtra("role", role);
                    } else {
                        i = new Intent(EditInventoryItem.this, OwnerMain.class);
                    }
                    startActivity(i);

                }
            }
        });

    }

    private void updateInventoryItem(String name, String desc, String expiry, String unit) {
        DocumentReference docRef = db.collection("Inventory").document(docId);
        Map<String, Object> updatedItem = new HashMap<>();
        updatedItem.put("name", name);
        updatedItem.put("desc", desc);
        updatedItem.put("expiry", expiry);
        updatedItem.put("units", unit);
        docRef.set(updatedItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Inventory Item successfully updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Inventory Item failed to update");
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
            if(role.equalsIgnoreCase("chef")){
                i = new Intent(EditInventoryItem.this, ChefMain.class);
                i.putExtra("role", role);
            } else {
                i = new Intent(EditInventoryItem.this, OwnerMain.class);
            }
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}