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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateMenuItemIngredients extends AppCompatActivity {

    private FirebaseFirestore db;
    private Intent data;
    private String docId, name, price, type, desc, cost, role;
    private List<Ingredients> ingredientsList = new ArrayList<>();
    private TextView menuItemName;
    private EditText ingredientsInput, updateCost;
    private Button updateIngredients;
    private final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_menu_item_ingredients);
        getSupportActionBar().setTitle("Update Ingredients Screen");

        db = FirebaseFirestore.getInstance();
        data = getIntent();

        role = data.getStringExtra("role");
        docId = data.getStringExtra("docId");
        name = data.getStringExtra("name");
        price = data.getStringExtra("price");
        type = data.getStringExtra("type");
        desc = data.getStringExtra("desc");
        cost = data.getStringExtra("cost");
        ingredientsList = data.getParcelableArrayListExtra("ingredients");


        menuItemName = findViewById(R.id.ingredientsMenuItemName);
        menuItemName.setText(name);

        ingredientsInput = findViewById(R.id.ingredientsList);
        StringBuilder list = new StringBuilder("");
        if(ingredientsList != null){
            for(Ingredients i : ingredientsList){
                list.append(i.getName() + ": " + i.getAmount() + "\n");
            }
            ingredientsInput.setText(list);
        } else {
            ingredientsInput.setText("");
        }

        updateCost = findViewById(R.id.menuItemCost);
        updateCost.setText(cost);

        updateIngredients = findViewById(R.id.updateIngredients);
        updateIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String costString = updateCost.getText().toString();
                String ingredientsString = ingredientsInput.getText().toString();
                Log.d(TAG, ingredientsString);
                ArrayList<Ingredients> itemIngredients = new ArrayList<>();
                if(costString.isEmpty() || ingredientsString.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else {
                    String[] firstSplit = ingredientsString.split("\\R");
                    Log.d(TAG, "First Split outside loop: " + firstSplit[0]);
                    for(String s : firstSplit){
                        Log.d(TAG, "First Split: " + s);
                        String[] splitIntoIngredientAndAmount = s.split(": ");
                        String ingredient = splitIntoIngredientAndAmount[0];
                        Log.d(TAG, "Ingredient: " + ingredient);
                        String amount = splitIntoIngredientAndAmount[1];
                        Log.d(TAG, "Amount: " + amount);
                        Ingredients item = new Ingredients(ingredient, amount);
                        itemIngredients.add(item);
                    }
                    inputMenuItemData(name, price, type, desc, costString, itemIngredients);
                    Intent i = new Intent(UpdateMenuItemIngredients.this, ChefMain.class);
                    i.putExtra("role", role);
                    startActivity(i);
                }
            }
        });

    }

    private void inputMenuItemData(String name, String price, String type, String desc, String costString, ArrayList<Ingredients> itemIngredients) {
        DocumentReference docRef = db.collection("Menu").document(docId);
        Map<String, Object> update = new HashMap<>();
        update.put("type", type);
        update.put("name", name);
        update.put("price", price);
        update.put("desc", desc);
        update.put("costPerUnit", costString);
        update.put("ingredients", itemIngredients);
        docRef.set(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: menu item has been edited " + docId);
                Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed: menu item has not been edited. Check document: " + docId);
                Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_LONG).show();
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
            Intent i = new Intent(UpdateMenuItemIngredients.this, ChefMain.class);
            i.putExtra("role", role);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}