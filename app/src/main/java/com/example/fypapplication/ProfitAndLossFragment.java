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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProfitAndLossFragment extends Fragment {

    private FirebaseFirestore db;
    private TextView date, totalCosts, totalSales, profitOrLoss, profitAndLossResult, staffCosts, mealCosts;
    private PieChart profitAndLossChart;
    private DatePickerDialog datePicker;
    private String selectedDate;
    private final String TAG = "TAG";
    private ArrayList<String> orderItemCosts;
    private ArrayList<String> sales;
    private ArrayList<String> staffWages;
    private ArrayList<StaffTimeSheet> rosterInfo;
    private ArrayList<StaffInfo> wageInfo;
    private ArrayList<MenuItem> items;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View profitAndLossView = inflater.inflate(R.layout.fragment_profit_and_loss, container, false);
        db = FirebaseFirestore.getInstance();

        date = profitAndLossView.findViewById(R.id.profitAndLossDate);
        staffCosts = profitAndLossView.findViewById(R.id.profitAndLossStaffCosts);
        mealCosts = profitAndLossView.findViewById(R.id.profitAndLossMealCosts);
        totalCosts = profitAndLossView.findViewById(R.id.profitAndLossTotalCosts);
        totalSales = profitAndLossView.findViewById(R.id.profitAndLossTotalSales);
        profitOrLoss = profitAndLossView.findViewById(R.id.profitOrLossTextView);
        profitAndLossResult = profitAndLossView.findViewById(R.id.profitAndLossResult);
        profitAndLossChart = profitAndLossView.findViewById(R.id.profitAndLossPieChart);
        initDatePicker();
        setUpPieChart();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });
        return profitAndLossView;
    }

    private void setUpPieChart() {
        profitAndLossChart.setDrawHoleEnabled(true);
        profitAndLossChart.setUsePercentValues(true);
        profitAndLossChart.setEntryLabelTextSize(12f);
        profitAndLossChart.setEntryLabelColor(Color.BLACK);
        profitAndLossChart.getDescription().setEnabled(false);

        Legend l = profitAndLossChart.getLegend();
        l.setEnabled(false);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String dateString = makeDateString(year, month, day);
                selectedDate = formatDate(dateString);
                Log.d(TAG, "Formatted selected Date " + selectedDate);
                date.setText(selectedDate);
                date.setTextColor(Color.BLACK);

                String formatDateForRoster = formatDateForRoster(selectedDate);

                db.collection("Roster").whereEqualTo("date", formatDateForRoster).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            rosterInfo = new ArrayList<>();
                            for(QueryDocumentSnapshot doc: task.getResult()){
                                Map<String, Object> docData = doc.getData();
                                //String date = (String) docData.get("date");
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

                            calculateStaffWages(rosterInfo);
                        }
                    }
                });


                db.collection("Sales").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            sales = new ArrayList<>();
                            orderItemCosts = new ArrayList<>();
                            items = new ArrayList<>();
                            for(QueryDocumentSnapshot doc: task.getResult()){
                                Map<String, Object> docData = doc.getData();
                                Log.d(TAG, docData.toString());
                                String timestamp = (String) docData.get("timestamp");
                                Log.d(TAG, "TimeStamp: " + timestamp);
                                String[] split = timestamp.split(" ");
                                String date = split[1];
                                Log.d(TAG, "Date: " + date);
                                if(selectedDate.equalsIgnoreCase(date)){
                                    sales.add((String) docData.get("total"));
                                    List<HashMap<String, Object>> orderItems = (List<HashMap<String, Object>>) docData.get("items");
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
                                orderItemCosts.add(i.getCostPerUnit());
                            }

                            setTotalMealCosts(orderItemCosts);
                            setSalesTotal(sales);
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

    private void loadPieChartData(String totalCost, String totalSales, String selectedDate) {

        profitAndLossChart.setCenterText(selectedDate);
        profitAndLossChart.setCenterTextSize(24f);
        String[] costSplit = totalCost.split("€");
        String[] saleSplit = totalSales.split("€");
        double cost = Double.parseDouble(costSplit[1]);
        double sales = Double.parseDouble(saleSplit[1]);
        double costAndSalesCombined = cost + sales;

        double result = sales - cost;
        profitAndLossResult.setText("€" + String.valueOf(result));
        profitAndLossResult.setTextColor(Color.BLACK);
        if(result < 0){
            profitOrLoss.setText("Loss: ");
        } else {
            profitOrLoss.setText("Profit: ");
        }

        float costPercentage = calculatePercentage(cost, costAndSalesCombined);
        float salesPercentage = calculatePercentage(sales, costAndSalesCombined);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(costPercentage, "Costs"));
        entries.add(new PieEntry(salesPercentage, "Sales"));

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Profit and Loss");
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(profitAndLossChart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        profitAndLossChart.setData(pieData);
        profitAndLossChart.invalidate();

    }

    private float calculatePercentage(double amount, double costAndSalesCombined) {
        return (float) ((amount / costAndSalesCombined) * 100);
    }

    private void setTotalCosts(String staffCost, String mealCost) {
        double staffTotal = Double.parseDouble(staffCost);
        double mealTotal = Double.parseDouble(mealCost);

        double total = staffTotal + mealTotal;
        totalCosts.setText("€" + String.valueOf(total));
        totalCosts.setTextColor(Color.BLACK);

        loadPieChartData(totalCosts.getText().toString(), totalSales.getText().toString(), selectedDate);
    }

    private void setSalesTotal(ArrayList<String> sales) {
        double total = 0.00;
        for(String s : sales){
            String[] split = s.split("€");
            Log.d(TAG, "Euro Symbol Removed: " + split[1]);
            double amount = Double.parseDouble(split[1]);
            total = total + amount;
        }
        totalSales.setText("€" + String.valueOf(total));
        totalSales.setTextColor(Color.BLACK);
    }

    private void setTotalMealCosts(ArrayList<String> orderItemCosts) {
        double total = 0.00;
        for(String s: orderItemCosts){
            double amount = Double.parseDouble(s);
            total = total + amount;
        }
        Log.d(TAG, "Meal Costs: " + total);
        mealCosts.setText(String.valueOf(total));
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

    private void calculateStaffWages(ArrayList<StaffTimeSheet> rosterInfo) {
        db.collection("Staff").whereNotEqualTo("role", "owner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    wageInfo = new ArrayList<>();
                    staffWages = new ArrayList<>();
                    for(QueryDocumentSnapshot doc: task.getResult()){
                        Map<String, Object> docData = doc.getData();
                        String name = (String) docData.get("fullName");
                        String wage = (String) docData.get("wage");
                        StaffInfo staffInfo = new StaffInfo(name, wage);
                        wageInfo.add(staffInfo);
                    }

                    for(StaffInfo info: wageInfo){
                        for(StaffTimeSheet sheet: rosterInfo){
                            if(info.getName().equalsIgnoreCase(sheet.getName())){
                                double wage = Double.parseDouble(info.getWage());
                                double hours = Double.parseDouble(sheet.getTime());
                                double total = wage * hours;
                                staffWages.add(String.valueOf(total));
                            }
                        }
                    }

                    setStaffTotal(staffWages);

                }
            }
        });
    }

    private void setStaffTotal(ArrayList<String> staffWages) {
        double total = 0.00;
        for(String s : staffWages){
            double amount = Double.parseDouble(s);
            total = total + amount;
        }
        Log.d(TAG, "Staff Costs: " + total);
        staffCosts.setText(String.valueOf(total));

        setTotalCosts(staffCosts.getText().toString(), mealCosts.getText().toString());
    }
}