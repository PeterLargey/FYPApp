package com.example.fypapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class CreateMenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View createMenuView = inflater.inflate(R.layout.fragment_create_menu, container, false);

        String[] types = new String[5];
        types[0] = "Special";
        types[1] = "Starter";
        types[2] = "Main";
        types[3] = "Dessert";
        types[4] = "Kid";

        ArrayAdapter<String> adapter;

        AutoCompleteTextView itemType = createMenuView.findViewById(R.id.newMenuItemType);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.itemtype_dropdown_item, types);
        itemType.setAdapter(adapter);

        itemType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(view.getContext(), "Type Selected " + item, Toast.LENGTH_LONG).show();
            }
        });

        EditText itemName = createMenuView.findViewById(R.id.newMenuItemName);
        EditText itemDesc = createMenuView.findViewById(R.id.newMenuItemDesc);
        EditText itemPrice = createMenuView.findViewById(R.id.newMenuItemPrice);

        Button createItem = createMenuView.findViewById(R.id.createMenuItem);

        createItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = itemType.getText().toString();
                String name = itemName.getText().toString();
                String desc = itemDesc.getText().toString();
                String price = itemPrice.getText().toString();

                if(type.isEmpty() || name.isEmpty() || desc.isEmpty() || price.isEmpty()){
                    Toast.makeText(view.getContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else if(!price.matches("^[0-9]*\\.?[0-9]*$")) {
                    itemPrice.setError("Make sure the price includes a decimal point!");
                } else {
                    inputDataIntoMenu(type, name, desc, price);
                    Toast.makeText(view.getContext(), "New menu item created", Toast.LENGTH_LONG).show();
                }
            }
        });
        return createMenuView;
    }

    public void inputDataIntoMenu(String type, String name, String desc, String price){
        String TAG = "TAG";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Menu").document();
        Map<String, Object> newMenuItem = new HashMap<>();
        newMenuItem.put("type", type);
        newMenuItem.put("name", name);
        newMenuItem.put("desc", desc);
        newMenuItem.put("price", price);

        docRef.set(newMenuItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Success, new menu item was added to the database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed, new menu item was not added to the database");
            }
        });


    }
}