package com.example.fypapplication;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;


public class SalesFragment extends Fragment {

    private FirebaseFirestore db;
    private TextView date, totalSales, before4pm, between4pmAnd7pm, after7pm;
    private PieChart salesChart;
    private DatePickerDialog datePicker;
    private String selectedDate;
    private final String TAG = "TAG";
    private ArrayList<String> totals;
    private ArrayList<SalesData> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View salesView = inflater.inflate(R.layout.fragment_sales, container, false);

        db = FirebaseFirestore.getInstance();
        date = salesView.findViewById(R.id.salesDate);
        totalSales = salesView.findViewById(R.id.totalSales);
        before4pm = salesView.findViewById(R.id.totalSalesBefore4pm);
        between4pmAnd7pm = salesView.findViewById(R.id.totalSalesBetween4pmAnd7pm);
        after7pm = salesView.findViewById(R.id.totalSalesAfter7pm);
        salesChart = salesView.findViewById(R.id.salesPieChart);
        initDatePicker();
        setUpPieChart();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });

        return salesView;
    }

    private void setUpPieChart() {
        salesChart.setDrawHoleEnabled(true);
        salesChart.setUsePercentValues(true);
        salesChart.setEntryLabelTextSize(12f);
        salesChart.setEntryLabelColor(Color.BLACK);
//        salesChart.setCenterText("Sales " + date.getText().toString());
//        salesChart.setCenterTextSize(24f);
        salesChart.getDescription().setEnabled(false);

        Legend l = salesChart.getLegend();
        l.setEnabled(false);
    }

    private void formatTotals(ArrayList<String> totals) {
        double total = 0.00;
        for(String s : totals){
            String[] split = s.split("€");
            double value = Double.parseDouble(split[1]);
            total = total + value;
        }
        totalSales.setText("€" + String.valueOf(total));
        totalSales.setTextColor(Color.BLACK);
    }

    private String formatDate(String selectedDate) {
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

                db.collection("Sales").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            totals = new ArrayList<>();
                            data = new ArrayList<>();
                            for(QueryDocumentSnapshot doc: task.getResult()){
                                Map<String, Object> docMap = doc.getData();
                                Log.d(TAG, docMap.toString());
                                String timestamp = (String) docMap.get("timestamp");
                                Log.d(TAG, "TimeStamp: " + timestamp);
                                String[] split = timestamp.split(" ");
                                String date = split[1];
                                String time = split[0];
                                Log.d(TAG, "Date: " + date);
                                Log.d(TAG, "Time: " + time);
                                if(selectedDate.equalsIgnoreCase(date)){
                                    totals.add((String) docMap.get("total"));
                                    SalesData sale = new SalesData(time, (String) docMap.get("total"));
                                    data.add(sale);
                                }
                            }

                            formatTotals(totals);
                            loadPieChartData(data, selectedDate);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadPieChartData(ArrayList<SalesData> data, String selectedDate) {

        salesChart.setCenterText(selectedDate);
        salesChart.setCenterTextSize(24f);

        int totalFrequency = data.size();
        Log.d(TAG, "Total Frequency: " + totalFrequency);
        int beforeFour = 0;
        int beforeSeven = 0;
        int afterSeven = 0;

        double totalBeforeFour = 0.00;
        double totalBeforeSeven = 0.00;
        double totalAfterSeven = 0.00;

        LocalTime saleTimeBeforeFour = LocalTime.of(16,0);
        LocalTime saleTimeBeforeSeven = LocalTime.of(19,0);


        for(SalesData s : data){
            String time = s.getTime();
            String[] split = time.split(":");
            String hourString = split[0];
            String minString = split[1];
            int hour = Integer.parseInt(hourString);
            int min = Integer.parseInt(minString);
            LocalTime saleTime = LocalTime.of(hour, min);
            String saleTotal = s.getTotal();
            String[] totalSplit = saleTotal.split("€");
            double totalDouble = Double.parseDouble(totalSplit[1]);

            if(saleTime.isBefore(saleTimeBeforeFour)){
                beforeFour ++;
                totalBeforeFour = totalBeforeFour + totalDouble;
            }

            if(saleTime.isAfter(saleTimeBeforeFour) && saleTime.isBefore(saleTimeBeforeSeven)){
                beforeSeven ++;
                totalBeforeSeven = totalBeforeSeven + totalDouble;
            }

            if(saleTime.isAfter(saleTimeBeforeSeven)){
                afterSeven ++;
                totalAfterSeven = totalAfterSeven + totalDouble;
            }

        }

        Log.d(TAG, "Before Four Frequency: " + beforeFour + " Before Four Total: " + totalBeforeFour);
        Log.d(TAG, "Before Seven Frequency: " + beforeSeven + " Before Seven Total: " + totalBeforeSeven);
        Log.d(TAG, "After Seven Frequency: " + afterSeven + " After Seven Total: " + totalAfterSeven);

        before4pm.setText("€" + String.valueOf(totalBeforeFour));
        before4pm.setTextColor(Color.BLACK);
        between4pmAnd7pm.setText("€" + String.valueOf(totalBeforeSeven));
        between4pmAnd7pm.setTextColor(Color.BLACK);
        after7pm.setText("€" + String.valueOf(totalAfterSeven));
        after7pm.setTextColor(Color.BLACK);

        float saleBeforeFour = calculatePercentage(beforeFour, totalFrequency);
        Log.d(TAG, "Sales Before Four Floating Point: " + saleBeforeFour);
        float saleBeforeSeven = calculatePercentage(beforeSeven, totalFrequency);
        Log.d(TAG, "Sales Between Four and Seven Floating Point: " + saleBeforeSeven);
        float saleAfterSeven = calculatePercentage(afterSeven, totalFrequency);
        Log.d(TAG, "Sales After Seven Floating Point: " + saleAfterSeven);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(saleBeforeFour, "Sales Before 4pm"));
        entries.add(new PieEntry(saleBeforeSeven, "Sales Between 4pm and 7pm"));
        entries.add(new PieEntry(saleAfterSeven, "Sales After 7pm"));

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Sales");
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(salesChart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        salesChart.setData(pieData);
        salesChart.invalidate();

    }

    private float calculatePercentage(int frequency, int totalFrequency) {
        return (float) frequency / totalFrequency;
    }

    private String makeDateString(int year, int month, int day) {
        return day + "/" + month + "/" + year;
    }
}