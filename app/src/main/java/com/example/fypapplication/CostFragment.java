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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class CostFragment extends Fragment {

    private FirebaseFirestore db;
    private TextView date, totalCosts, foodCosts, drinkCosts, staffCosts;
    private PieChart costsChart;
    private DatePickerDialog datePicker;
    private String selectedDate;
    private final String TAG = "TAG";
    private ArrayList<String> foodTotals;
    private ArrayList<String> drinkTotals;
    private ArrayList<String> staffTotals;
    private ArrayList<StaffTimeSheet> rosterInfo;
    private ArrayList<StaffInfo> wageInfo;
    private List<MenuItem> items;


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

    private void setTotalCostsTextView(String foodCosts, String drinkCosts, String staffCosts) {
        String[] foodSplit = foodCosts.split("€");
        double foodTotal = Double.parseDouble(foodSplit[1]);
        Log.d(TAG, "Food Costs: " + foodTotal);
        String[] drinkSplit = drinkCosts.split("€");
        double drinksTotal = Double.parseDouble(drinkSplit[1]);
        Log.d(TAG, "Drink Costs: " + foodTotal);
        String[] staffSplit = staffCosts.split("€");
        double staffTotal = Double.parseDouble(staffSplit[1]);
        Log.d(TAG, "Staff Costs: " + foodTotal);

        double total = foodTotal + drinksTotal + staffTotal;

        totalCosts.setText("€" + String.valueOf(total));
        totalCosts.setTextColor(Color.BLACK);
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
                            items = new ArrayList<>();
                            for(QueryDocumentSnapshot doc: task.getResult()){
                                Map<String, Object> docMap = doc.getData();
                                Log.d(TAG, docMap.toString());
                                String timestamp = (String) docMap.get("timestamp");
                                Log.d(TAG, "TimeStamp: " + timestamp);
                                String[] split = timestamp.split(" ");
                                String date = split[1];
                                Log.d(TAG, "Date: " + date);
                                if(selectedDate.equalsIgnoreCase(date)){
                                    List<HashMap<String, Object>> orderItems = (List<HashMap<String, Object>>) docMap.get("items");
                                    Log.d(TAG, "String of Order Items: " + String.valueOf(orderItems));
                                    for(HashMap<String, Object> item: orderItems){

                                        String name = (String) item.get("name");
                                        Log.d(TAG, "Name: " + name);
                                        String cost = (String) item.get("costPerUnit");
                                        Log.d(TAG, "Cost: " + cost);
                                        String price = (String) item.get("price");
                                        Log.d(TAG, "Price: " + price);
                                        String type = (String) item.get("type");
                                        Log.d(TAG, "Type: " + type);
                                        MenuItem menuItem = new MenuItem(type, name, null, price, null, cost);
                                        items.add(menuItem);

                                    }
                                }
                            }

                            for(MenuItem i : items){
                                if(i.getType().equalsIgnoreCase("Drink")){
                                    drinkTotals.add(i.getCostPerUnit());
                                } else {
                                    foodTotals.add(i.getCostPerUnit());
                                }
                            }

                            getDrinkCosts(drinkTotals);
                            getFoodCosts(foodTotals);

                        }
                    }
                });
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
        datePicker.setTitle("Select Date");
    }

    private void loadPieChartData(String foodCosts, String drinkCosts, String staffCosts, String total, String selectedDate) {
        costsChart.setCenterText(selectedDate);
        costsChart.setCenterTextSize(24f);

        String[] foodSplit = foodCosts.split("€");
        String[] drinkSplit = drinkCosts.split("€");
        String[] staffSplit = staffCosts.split("€");
        String[] totalSplit = total.split("€");

        double totalCost = Double.parseDouble(totalSplit[1]);
        double foodCost = Double.parseDouble(foodSplit[1]);
        double drinkCost = Double.parseDouble(drinkSplit[1]);
        double staffCost = Double.parseDouble(staffSplit[1]);
        
        float foodPercentage = calculatePercentage(foodCost, totalCost);
        float drinkPercentage = calculatePercentage(drinkCost, totalCost);
        float staffPercentage = calculatePercentage(staffCost, totalCost);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(foodPercentage, "Food Costs"));
        entries.add(new PieEntry(drinkPercentage, "Drink Costs"));
        entries.add(new PieEntry(staffPercentage, "Staff Costs"));

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Costs");
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(costsChart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        costsChart.setData(pieData);
        costsChart.invalidate();

    }

    private float calculatePercentage(double amount, double totalCost) {
        return (float) ((amount / totalCost) * 100);
    }

    private void getFoodCosts(ArrayList<String> foodTotals) {
        double total = 0.00;
        for(String s: foodTotals){
            double itemAmount = Double.parseDouble(s);
            total = total + itemAmount;
        }
        Log.d(TAG, "Food Costs: "+ total);
        foodCosts.setText("€" + String.valueOf(total));
        foodCosts.setTextColor(Color.BLACK);
    }

    private void getDrinkCosts(ArrayList<String> drinkTotals) {
        double total = 0.00;
        for(String s: drinkTotals){
            double itemAmount = Double.parseDouble(s);
            total = total + itemAmount;
        }
        Log.d(TAG, "Drink Costs: "+ total);
        drinkCosts.setText("€" + String.valueOf(total));
        drinkCosts.setTextColor(Color.BLACK);
    }

    private void getStaffCosts(String selectedDate) {
        String formatDateForRoster = formatDateForRoster(selectedDate);
        Log.d(TAG, "Date for the Roster: " + formatDateForRoster);
        db.collection("Roster").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    rosterInfo = new ArrayList<>();
                    for(QueryDocumentSnapshot doc: task.getResult()){
                        Map<String, Object> docData = doc.getData();
                        String date = (String) docData.get("date");
                        Log.d(TAG, "Date from Roster: " + date);
                        if(formatDateForRoster.equalsIgnoreCase(date)){
                            String name = (String) docData.get("name");
                            String time = (String) docData.get("time");
                            String[] split = time.split(" - ");
                            int start = Integer.parseInt(split[0]);
                            int finish = Integer.parseInt(split[1]);
                            int totalHours = finish - start;
                            String hours = String.valueOf(totalHours);
                            StaffTimeSheet staffTimeSheet = new StaffTimeSheet(name, hours);
                            rosterInfo.add(staffTimeSheet);
                        }
                    }

                    calculateStaffWages(rosterInfo);
                }
            }
        });
    }

    private void calculateStaffWages(ArrayList<StaffTimeSheet> rosterInfo) {
        db.collection("Staff").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    wageInfo = new ArrayList<>();
                    staffTotals = new ArrayList<>();
                    for(QueryDocumentSnapshot doc: task.getResult()){
                        Map<String, Object> docData = doc.getData();
                        String username = (String) docData.get("username");
                        Log.d(TAG, "UserName: " + username);
                        String wage = (String) docData.get("wage");
                        Log.d(TAG, "Wage: " + wage);
                        StaffInfo staffInfo = new StaffInfo(username, wage);
                        wageInfo.add(staffInfo);
                    }

                    for(StaffInfo info: wageInfo){
                        for(StaffTimeSheet sheet: rosterInfo){
                            if(info.getName().equalsIgnoreCase(sheet.getName())){
                                double wage = Double.parseDouble(info.getWage());
                                double hours = Double.parseDouble(sheet.getTime());
                                Log.d(TAG, "Hours: " + hours);
                                double total = wage * hours;
                                Log.d(TAG, "Total: " + total);
                                staffTotals.add(String.valueOf(total));
                            }
                        }
                    }

                    setStaffTotals(staffTotals);
                }
            }
        });
    }

    private void setStaffTotals(ArrayList<String> staffTotals) {
        double total = 0.00;
        for(String s : staffTotals){
            double amount = Double.parseDouble(s);
            total = total + amount;
        }
        Log.d(TAG, "Staff Costs: " + total);
        staffCosts.setText("€" + String.valueOf(total));
        staffCosts.setTextColor(Color.BLACK);

        setTotalCostsTextView(foodCosts.getText().toString(), drinkCosts.getText().toString(), staffCosts.getText().toString());
        loadPieChartData(foodCosts.getText().toString(), drinkCosts.getText().toString(), staffCosts.getText().toString(), totalCosts.getText().toString(), selectedDate);
    }

    private String formatDateForRoster(String selectedDate) {
        String[] split = selectedDate.split("/");
        String day = formatDayForRoster(split[0]);
        String month = formatMonthForRoster(split[1]);
        String year = split[2];
        return day + " " +  month + " " + year;
    }

    private String formatMonthForRoster(String month) {
        if(month.equalsIgnoreCase("01")){
            return "January";
        }
        if(month.equalsIgnoreCase("02")){
            return "February";
        }
        if(month.equalsIgnoreCase("03")){
            return "March";
        }
        if(month.equalsIgnoreCase("04")){
            return "April";
        }
        if(month.equalsIgnoreCase("05")){
            return "May";
        }
        if(month.equalsIgnoreCase("06")){
            return "June";
        }
        if(month.equalsIgnoreCase("07")){
            return "July";
        }
        if(month.equalsIgnoreCase("08")){
            return "August";
        }
        if(month.equalsIgnoreCase("09")){
            return "September";
        }
        if(month.equalsIgnoreCase("10")){
            return "October";
        }
        if(month.equalsIgnoreCase("11")){
            return "November";
        }
        if(month.equalsIgnoreCase("12")){
            return "December";
        }

        return "January";
    }

    private String formatDayForRoster(String day) {
        if(day.equalsIgnoreCase("01")){
            return "1";
        }
        if(day.equalsIgnoreCase("02")){
            return "2";
        }
        if(day.equalsIgnoreCase("03")){
            return "3";
        }
        if(day.equalsIgnoreCase("04")){
            return "4";
        }
        if(day.equalsIgnoreCase("05")){
            return "5";
        }
        if(day.equalsIgnoreCase("06")){
            return "6";
        }
        if(day.equalsIgnoreCase("07")){
            return "7";
        }
        if(day.equalsIgnoreCase("08")){
            return "8";
        }
        if(day.equalsIgnoreCase("09")){
            return "9";
        }
        return day;
    }

    private String formatDate(String dateString) {
        String[] split = dateString.split("/");
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