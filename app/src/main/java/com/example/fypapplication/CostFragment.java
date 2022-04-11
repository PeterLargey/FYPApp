package com.example.fypapplication;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class CostFragment extends Fragment {

    private FirebaseFirestore db;
    private TextView date, totalCosts, foodCosts, drinkCosts, staffCosts;
    private PieChart costsChart;
    private DatePickerDialog datePicker;
    private String selectedDate;
    private final String TAG = "TAG";
    private ArrayList<String> foodTotals;
    private ArrayList<String> drinkTotals;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View costView = inflater.inflate(R.layout.fragment_cost, container, false);

        db = FirebaseFirestore.getInstance();
        date = costView.findViewById(R.id.costDate);
        totalCosts = costView.findViewById(R.id.totalCosts);
        foodCosts = costView.findViewById(R.id.foodCosts);
        drinkCosts = costView.findViewById(R.id.drinkCosts);
        staffCosts = costView.findViewById(R.id.staffCosts);
        costsChart = costView.findViewById(R.id.costsPieChart);
        initDatePicker();
        setUpPieChart();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });

        return costView;
    }

    private void setUpPieChart() {
        costsChart.setDrawHoleEnabled(true);
        costsChart.setUsePercentValues(true);
        costsChart.setEntryLabelTextSize(12f);
        costsChart.setEntryLabelColor(Color.BLACK);
        costsChart.getDescription().setEnabled(false);

        Legend l = costsChart.getLegend();
        l.setEnabled(false);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String dateString = makeDateString(year, month, day);
                selectedDate = formatDate(dateString);
                Log.d(TAG, selectedDate);
                date.setText(selectedDate);
                date.setTextColor(Color.BLACK);

                getStaffCosts(selectedDate);

                db.collection("Sales").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            foodTotals = new ArrayList<>();
                            drinkTotals = new ArrayList<>();
                            ArrayList<MenuItem> items = new ArrayList<>();
                            for(QueryDocumentSnapshot doc: task.getResult()){
                                Map<String, Object> docMap = new HashMap<>();
                                Log.d(TAG, docMap.toString());
                                String timestamp = (String) docMap.get("timestamp");
                                Log.d(TAG, "TimeStamp: " + timestamp);
                                String[] split = timestamp.split(" ");
                                String date = split[1];
                                Log.d(TAG, "Date: " + date);
                                if(selectedDate.equalsIgnoreCase(date)){
                                    items = (ArrayList<MenuItem>) docMap.get("items");
                                    for(MenuItem i : items){
                                        if(i.getType().equalsIgnoreCase("Drink")){
                                            drinkTotals.add(i.getCostPerUnit());
                                        } else{
                                            foodTotals.add(i.getCostPerUnit());
                                        }

                                    }
                                }
                            }

                            getDrinkCosts(drinkTotals);
                            getFoodCosts(foodTotals);

                        }
                    }
                });

                //Set Total Costs TextView
                loadPieChartData(foodCosts.getText().toString(), drinkCosts.getText().toString(), staffCosts.getText().toString());
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
        datePicker.setTitle("Select Date");
    }

    private void loadPieChartData(String foodCosts, String drinkCosts, String staffCosts) {

    }

    private void getFoodCosts(ArrayList<String> foodTotals) {
    }

    private void getDrinkCosts(ArrayList<String> drinkTotals) {
    }

    private void getStaffCosts(String selectedDate) {
        db.collection("Roster").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                }
            }
        });
    }

    private String formatDate(String dateString) {
        String[] split = selectedDate.split("/");
        String day = formatDay(split[0]);
        String month = formatMonth(split[1]);
        String year = split[2];
        return day + "/" + month + "/" + year;
    }

    private String formatMonth(String s) {
        if(s.equalsIgnoreCase("1")){
            return "01";
        }
        if(s.equalsIgnoreCase("2")){
            return "02";
        }
        if(s.equalsIgnoreCase("3")){
            return "03";
        }
        if(s.equalsIgnoreCase("4")){
            return "04";
        }
        if(s.equalsIgnoreCase("5")){
            return "05";
        }
        if(s.equalsIgnoreCase("6")){
            return "06";
        }
        if(s.equalsIgnoreCase("7")){
            return "07";
        }
        if(s.equalsIgnoreCase("8")){
            return "08";
        }
        if(s.equalsIgnoreCase("9")){
            return "09";
        }

        return s;
    }

    private String formatDay(String s) {
        if(s.equalsIgnoreCase("1")){
            return "01";
        }
        if(s.equalsIgnoreCase("2")){
            return "02";
        }
        if(s.equalsIgnoreCase("3")){
            return "03";
        }
        if(s.equalsIgnoreCase("4")){
            return "04";
        }
        if(s.equalsIgnoreCase("5")){
            return "05";
        }
        if(s.equalsIgnoreCase("6")){
            return "06";
        }
        if(s.equalsIgnoreCase("7")){
            return "07";
        }
        if(s.equalsIgnoreCase("8")){
            return "08";
        }
        if(s.equalsIgnoreCase("9")){
            return "09";
        }

        return s;
    }

    private String makeDateString(int year, int month, int day) {
        return day + "/" + month + "/" + year;
    }
}