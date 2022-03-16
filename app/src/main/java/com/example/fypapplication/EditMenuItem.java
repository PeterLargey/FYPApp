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

public class EditMenuItem extends AppCompatActivity {

    private Intent data;
    private static final String TAG = "TAG";
    private FirebaseFirestore db;
    private EditText name, desc, price;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu_item);
        getSupportActionBar().setTitle("Edit Menu Item Info");

        db = FirebaseFirestore.getInstance();
        data = getIntent();

        name = findViewById(R.id.editMenuItemName);
        desc = findViewById(R.id.editMenuItemDesc);
        price = findViewById(R.id.editMenuItemPrice);

        button = findViewById(R.id.updateMenuItem);

        String itemName = data.getStringExtra("name");
        String itemDesc = data.getStringExtra("desc");
        String itemPrice = data.getStringExtra("price");
        String itemType = data.getStringExtra("type");

        String docId = data.getStringExtra("docId");

        name.setText(itemName);
        desc.setText(itemDesc);
        price.setText(itemPrice);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputName = name.getText().toString();
                String inputDesc = desc.getText().toString();
                String inputPrice = price.getText().toString();

                if(inputName.isEmpty() || inputDesc.isEmpty() || inputPrice.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else {
                    DocumentReference docRef = db.collection("Menu").document(docId);
                    Map<String, Object> edit = new HashMap<>();
                    edit.put("type", itemType);
                    edit.put("name", inputName);
                    edit.put("desc", inputDesc);
                    edit.put("price", inputPrice);
                    docRef.set(edit).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: menu item has been edited " + docId);
                            Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(EditMenuItem.this, ManagerMain.class);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Failed: menu item has not been edited. Check document: " + docId);
                            Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_LONG).show();
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
            Intent i = new Intent(EditMenuItem.this, ManagerMain.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}