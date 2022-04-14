package com.example.fypapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddToInventoryFragment extends Fragment {

    private String role;
    private FirebaseFirestore db;
    private EditText name, desc, expiry, units;
    private Button addItem;
    private final String TAG = "TAG";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View addToInventoryView = inflater.inflate(R.layout.fragment_add_to_inventory, container, false);
        db = FirebaseFirestore.getInstance();
        if(getArguments() != null){
            role = getArguments().getString("role");
        }

        name = addToInventoryView.findViewById(R.id.inputInventoryItemName);
        desc = addToInventoryView.findViewById(R.id.inputInventoryItemDesc);
        expiry = addToInventoryView.findViewById(R.id.inputInventoryItemExpiry);
        units = addToInventoryView.findViewById(R.id.inputInventoryItemUnits);

        addItem = addToInventoryView.findViewById(R.id.addInventoryItem);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameString = name.getText().toString();
                String descString = desc.getText().toString();
                String expiryString = expiry.getText().toString();
                String unitString = units.getText().toString();

                if(nameString.isEmpty() || descString.isEmpty() || expiryString.isEmpty() || unitString.isEmpty()){
                    Toast.makeText(view.getContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else {
                    inputInventoryItem(nameString, descString, expiryString, unitString);
                    Toast.makeText(addToInventoryView.getContext(), "Item Added to Inventory", Toast.LENGTH_LONG).show();
                    Intent i;
                    if(role.equalsIgnoreCase("chef")){
                        i = new Intent(addToInventoryView.getContext(), ChefMain.class);
                        i.putExtra("role", role);
                    } else {
                        i = new Intent(addToInventoryView.getContext(), OwnerMain.class);
                    }
                    startActivity(i);
                }
            }
        });


        return addToInventoryView;
    }

    private void inputInventoryItem(String name, String desc, String expiry, String unit) {
        DocumentReference docRef = db.collection("Inventory").document();
        Map<String, Object> addItem = new HashMap<>();
        addItem.put("name", name);
        addItem.put("desc", desc);
        addItem.put("expiry", expiry);
        addItem.put("units", unit);
        docRef.set(addItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Inventory Item successfully added to the database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Inventory Item could not be added to the database");
            }
        });

    }
}