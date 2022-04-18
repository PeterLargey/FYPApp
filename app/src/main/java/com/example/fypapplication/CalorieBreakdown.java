package com.example.fypapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CalorieBreakdown extends AppCompatActivity {

    private Intent data;
    private RequestQueue mQueue;
    private TextView name, calories, desc, sugars, proteins, fats, carbohydrates;
    private final String TAG = "TAG";
    private String customerName;
    private ArrayList<Ingredients> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_breakdown);
        mQueue = Volley.newRequestQueue(this);

        data = getIntent();
        name = findViewById(R.id.itemName);
        desc = findViewById(R.id.calorieBreakdownDescription);
        calories = findViewById(R.id.itemCalories);
        sugars = findViewById(R.id.itemSugar);
        proteins = findViewById(R.id.itemProtein);
        fats = findViewById(R.id.itemFat);
        carbohydrates = findViewById(R.id.itemCarbs);

        customerName = data.getStringExtra("customerName");
        Log.d(TAG, "Customer Name" + customerName);


        String itemName = data.getStringExtra("name");
        String itemDesc = data.getStringExtra("desc");
        ingredients = data.getParcelableArrayListExtra("ingredients");
        name.setText(itemName);
        desc.setText(itemDesc);

        String apiKey = "1T1UW7WJL7YB9qxTGgC30Q==qxkq7426qvRwyUWt";
//        if(itemName.equalsIgnoreCase("Surf & Turf")){
//            getItemBreakdown(apiKey, itemDesc);
//        } else{
//            getItemBreakdown(apiKey, itemName);
//        }
        StringBuilder ingredientString = new StringBuilder("");
        for(Ingredients i : ingredients){
            String formattedIngredient = i.getAmount() + " " + i.getName() + " ";
            ingredientString.append(formattedIngredient);
        }

        Log.d(TAG, "Ingredient String: " + ingredientString.toString());

        getItemBreakdown(apiKey, ingredientString.toString());

    }

    private void getItemBreakdown(String apiKey, String name){
        String url = "https://api.calorieninjas.com/v1/nutrition?query="+name;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray jsonArray = response.getJSONArray("items");
                    double totalCalories = 0;
                    double sugar = 0;
                    double protein = 0;
                    double fat = 0;
                    double carbs = 0;
                    //JSONObject itemDetails = jsonArray.getJSONObject(0);
                    for(int i = 0; i < jsonArray.length(); i ++){
                        JSONObject itemDetails = jsonArray.getJSONObject(i);
                        String itemCalories = itemDetails.getString("calories");
                        Log.d(TAG, "Item Calories: " + itemCalories);
                        String itemSugar = itemDetails.getString("sugar_g");
                        Log.d(TAG, "Item Sugar: " + itemSugar);
                        String itemProtein = itemDetails.getString("protein_g");
                        Log.d(TAG, "Item Protein: " + itemProtein);
                        String itemFat = itemDetails.getString("fat_total_g");
                        Log.d(TAG, "Item Fat: " + itemFat);
                        String itemCarbs = itemDetails.getString("carbohydrates_total_g");
                        Log.d(TAG, "Item Carbs: " + itemCarbs);
                        double itemCaloriesDouble = Double.parseDouble(itemCalories);
                        double itemSugarDouble = Double.parseDouble(itemSugar);
                        double itemProteinDouble = Double.parseDouble(itemProtein);
                        double itemFatDouble = Double.parseDouble(itemFat);
                        double itemCarbsDouble = Double.parseDouble(itemCarbs);
                        totalCalories = totalCalories + itemCaloriesDouble;
                        sugar = sugar + itemSugarDouble;
                        protein = protein + itemProteinDouble;
                        fat = fat + itemFatDouble;
                        carbs = carbs + itemCarbsDouble;
                    }
                    DecimalFormat df = new DecimalFormat("###.##");
                    String totalCaloriesString = String.valueOf(df.format(totalCalories)) + "kcal";
                    String sugarString = df.format(sugar) + "g";
                    String proteinString = df.format(protein) + "g";
                    String fatString = df.format(fat) + "g";
                    String carbsString = df.format(carbs) + "g";
                    calories.setText(totalCaloriesString);
                    sugars.setText(sugarString);
                    proteins.setText(proteinString);
                    fats.setText(fatString);
                    carbohydrates.setText(carbsString);
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Api-Key", apiKey);
                return params;
            }
        };
        mQueue.add(request);
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
            Intent i = new Intent(CalorieBreakdown.this, UserMain.class);
            i.putExtra("user", customerName);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}