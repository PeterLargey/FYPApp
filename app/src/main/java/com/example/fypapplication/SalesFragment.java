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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class SalesFragment extends Fragment {

    private FirebaseFirestore db;
    private TextView date, totalSales, before4pm, between4pmAnd7pm, after7pm, mostPopularItem, mostPopularDrink, mostPopularStarter, mostPopularMain, mostPopularDessert, mostPopularSpecial, mostPopularKid;
    private PieChart salesChart, itemChart, drinkChart, starterChart, mainCourseChart, dessertChart, specialChart, kidChart;
    private DatePickerDialog datePicker;
    private String selectedDate;
    private final String TAG = "TAG";
    private ArrayList<String> totals;
    private ArrayList<SalesData> data;
    private ArrayList<String> items;
    private ArrayList<String> drinks;
    private ArrayList<String> starters;
    private ArrayList<String> mains;
    private ArrayList<String> desserts;
    private ArrayList<String> specials;
    private ArrayList<String> kids;


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
        mostPopularItem = salesView.findViewById(R.id.mostPopularItem);
        mostPopularItem.setTextColor(Color.BLACK);
        mostPopularStarter = salesView.findViewById(R.id.mostPopularStarter);
        mostPopularStarter.setTextColor(Color.BLACK);
        mostPopularMain = salesView.findViewById(R.id.mostPopularMain);
        mostPopularMain.setTextColor(Color.BLACK);
        mostPopularDessert = salesView.findViewById(R.id.mostPopularDessert);
        mostPopularDessert.setTextColor(Color.BLACK);
        mostPopularDrink = salesView.findViewById(R.id.mostPopularDrink);
        mostPopularDrink.setTextColor(Color.BLACK);
        mostPopularSpecial = salesView.findViewById(R.id.mostPopularSpecial);
        mostPopularSpecial.setTextColor(Color.BLACK);
        mostPopularKid = salesView.findViewById(R.id.mostPopularKidsItem);
        mostPopularKid.setTextColor(Color.BLACK);
        salesChart = salesView.findViewById(R.id.salesPieChart);
        itemChart = salesView.findViewById(R.id.mostPopularItemPieChart);
        starterChart = salesView.findViewById(R.id.starterPieChart);
        mainCourseChart = salesView.findViewById(R.id.mainCoursePieChart);
        dessertChart = salesView.findViewById(R.id.dessertPieChart);
        specialChart = salesView.findViewById(R.id.specialPieChart);
        kidChart = salesView.findViewById(R.id.kidsPieChart);
        drinkChart = salesView.findViewById(R.id.mostPopularDrinkPieChart);
        initDatePicker();
        setUpPieChart();
        setUpItemPieChart();
        setUpStarterPieChart();
        setUpMainPieChart();
        setUpDessertPieChart();
        setUpDrinkPieChart();
        setUpSpecialPieChart();
        setUpKidPieChart();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });

        return salesView;
    }

    private void setUpSpecialPieChart() {
        specialChart.setDrawHoleEnabled(true);
        specialChart.setUsePercentValues(true);
        specialChart.setEntryLabelTextSize(12f);
        specialChart.setEntryLabelColor(Color.BLACK);
        specialChart.getDescription().setEnabled(false);

        Legend l = specialChart.getLegend();
        l.setEnabled(false);
    }

    private void setUpKidPieChart() {
        kidChart.setDrawHoleEnabled(true);
        kidChart.setUsePercentValues(true);
        kidChart.setEntryLabelTextSize(12f);
        kidChart.setEntryLabelColor(Color.BLACK);
        kidChart.getDescription().setEnabled(false);

        Legend l = kidChart.getLegend();
        l.setEnabled(false);
    }

    private void setUpMainPieChart() {
        mainCourseChart.setDrawHoleEnabled(true);
        mainCourseChart.setUsePercentValues(true);
        mainCourseChart.setEntryLabelTextSize(12f);
        mainCourseChart.setEntryLabelColor(Color.BLACK);
        mainCourseChart.getDescription().setEnabled(false);

        Legend l = mainCourseChart.getLegend();
        l.setEnabled(false);
    }

    private void setUpDessertPieChart() {
        dessertChart.setDrawHoleEnabled(true);
        dessertChart.setUsePercentValues(true);
        dessertChart.setEntryLabelTextSize(12f);
        dessertChart.setEntryLabelColor(Color.BLACK);
        dessertChart.getDescription().setEnabled(false);

        Legend l = dessertChart.getLegend();
        l.setEnabled(false);
    }

    private void setUpStarterPieChart() {
        starterChart.setDrawHoleEnabled(true);
        starterChart.setUsePercentValues(true);
        starterChart.setEntryLabelTextSize(12f);
        starterChart.setEntryLabelColor(Color.BLACK);
        starterChart.getDescription().setEnabled(false);

        Legend l = starterChart.getLegend();
        l.setEnabled(false);
    }

    private void setUpDrinkPieChart() {
        drinkChart.setDrawHoleEnabled(true);
        drinkChart.setUsePercentValues(true);
        drinkChart.setEntryLabelTextSize(12f);
        drinkChart.setEntryLabelColor(Color.BLACK);
        drinkChart.getDescription().setEnabled(false);

        Legend l = drinkChart.getLegend();
        l.setEnabled(false);
    }

    private void setUpItemPieChart() {
        itemChart.setDrawHoleEnabled(true);
        itemChart.setUsePercentValues(true);
        itemChart.setEntryLabelTextSize(12f);
        itemChart.setEntryLabelColor(Color.BLACK);
        itemChart.getDescription().setEnabled(false);

        Legend l = itemChart.getLegend();
        l.setEnabled(false);
    }

    private void setUpPieChart() {
        salesChart.setDrawHoleEnabled(true);
        salesChart.setUsePercentValues(true);
        salesChart.setEntryLabelTextSize(12f);
        salesChart.setEntryLabelColor(Color.BLACK);
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
                            items = new ArrayList<>();
                            starters = new ArrayList<>();
                            mains = new ArrayList<>();
                            desserts = new ArrayList<>();
                            specials = new ArrayList<>();
                            kids = new ArrayList<>();
                            drinks = new ArrayList<>();
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
                                    List<HashMap<String, Object>> saleItems = (List<HashMap<String, Object>>) docMap.get("items");
                                    for(HashMap<String, Object> item : saleItems){
                                        String itemType = (String) item.get("type");
                                        if(itemType.equalsIgnoreCase("Starter")){
                                            String starterName = (String) item.get("name");
                                            Log.d(TAG, "Name: " + starterName);
                                            starters.add(starterName);
                                            items.add(starterName);
                                        } else if(itemType.equalsIgnoreCase("Main")){
                                            String mainName = (String) item.get("name");
                                            Log.d(TAG, "Name: " + mainName);
                                            mains.add(mainName);
                                            items.add(mainName);
                                        } else if(itemType.equalsIgnoreCase("Special")){
                                            String specialName = (String) item.get("name");
                                            Log.d(TAG, "Name: " + specialName);
                                            specials.add(specialName);
                                            items.add(specialName);
                                        } else if(itemType.equalsIgnoreCase("Dessert")){
                                            String dessertName = (String) item.get("name");
                                            Log.d(TAG, "Name: " + dessertName);
                                            desserts.add(dessertName);
                                            items.add(dessertName);
                                        } else if(itemType.equalsIgnoreCase("Kid")){
                                            String kidName = (String) item.get("name");
                                            Log.d(TAG, "Name: " + kidName);
                                            kids.add(kidName);
                                            items.add(kidName);
                                        } else {
                                            String drinkName = (String) item.get("name");
                                            Log.d(TAG, "Name: " + drinkName);
                                            drinks.add(drinkName);
                                        }
                                    }
                                }
                            }

                            formatTotals(totals);
                            loadPieChartData(data);
                            loadItemPieChartData(items);
                            loadStartersPieChartData(starters);
                            loadMainsPieChartData(mains);
                            loadDessertPieChartData(desserts);
                            loadSpecialsPieChartData(specials);
                            loadKisPieChartData(kids);
                            loadDrinkPieChartData(drinks);

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

    private void loadKisPieChartData(ArrayList<String> kids) {
        kidChart.setCenterText("Kids");
        kidChart.setCenterTextSize(24f);

        int totalFrequency = kids.size();
        Log.d(TAG, "Total Kids Frequency: " + totalFrequency);
        ArrayList<ItemCount> uniqueKids = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<String>(kids);
        for(String s : hashSet){
            ItemCount kid = new ItemCount(s, 0);
            uniqueKids.add(kid);
        }

        for(String s : kids){
            for(ItemCount i : uniqueKids){
                if(s.equalsIgnoreCase(i.getName())){
                    int count = i.getCount();
                    count = count + 1;
                    i.setCount(count);
                }
            }
        }

        int maxCount = uniqueKids.get(0).getCount();
        mostPopularKid.setText(uniqueKids.get(0).getName());
        for(ItemCount i : uniqueKids){
            if(maxCount < i.getCount()){
                maxCount = i.getCount();
                mostPopularKid.setText(i.getName());
            }
        }

        ArrayList<PieEntry> entries = new ArrayList<>();

        for(ItemCount i : uniqueKids){
            float kidPercentage = calculatePercentage(i.getCount(), totalFrequency);
            entries.add(new PieEntry(kidPercentage, i.getName()));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Kids");
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(kidChart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        kidChart.setData(pieData);
        kidChart.invalidate();
    }

    private void loadSpecialsPieChartData(ArrayList<String> specials) {
        specialChart.setCenterText("Specials");
        specialChart.setCenterTextSize(24f);

        int totalFrequency = specials.size();
        Log.d(TAG, "Total Specials Frequency: " + totalFrequency);
        ArrayList<ItemCount> uniqueSpecials = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<String>(specials);
        for(String s : hashSet){
            ItemCount special = new ItemCount(s, 0);
            uniqueSpecials.add(special);
        }

        for(String s : specials){
            for(ItemCount i : uniqueSpecials){
                if(s.equalsIgnoreCase(i.getName())){
                    int count = i.getCount();
                    count = count + 1;
                    i.setCount(count);
                }
            }
        }

        int maxCount = uniqueSpecials.get(0).getCount();
        mostPopularSpecial.setText(uniqueSpecials.get(0).getName());
        for(ItemCount i : uniqueSpecials){
            if(maxCount < i.getCount()){
                maxCount = i.getCount();
                mostPopularSpecial.setText(i.getName());
            }
        }

        ArrayList<PieEntry> entries = new ArrayList<>();

        for(ItemCount i : uniqueSpecials){
            float specialPercentage = calculatePercentage(i.getCount(), totalFrequency);
            entries.add(new PieEntry(specialPercentage, i.getName()));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Specials");
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(specialChart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        specialChart.setData(pieData);
        specialChart.invalidate();
    }

    private void loadDessertPieChartData(ArrayList<String> desserts) {
        dessertChart.setCenterText("Desserts");
        dessertChart.setCenterTextSize(24f);

        int totalFrequency = desserts.size();
        Log.d(TAG, "Total Desserts Frequency: " + totalFrequency);
        ArrayList<ItemCount> uniqueDesserts = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<String>(desserts);
        for(String s : hashSet){
            ItemCount dessert = new ItemCount(s, 0);
            uniqueDesserts.add(dessert);
        }

        for(String s : desserts){
            for(ItemCount i : uniqueDesserts){
                if(s.equalsIgnoreCase(i.getName())){
                    int count = i.getCount();
                    count = count + 1;
                    i.setCount(count);
                }
            }
        }

        int maxCount = uniqueDesserts.get(0).getCount();
        mostPopularDessert.setText(uniqueDesserts.get(0).getName());
        for(ItemCount i : uniqueDesserts){
            if(maxCount < i.getCount()){
                maxCount = i.getCount();
                mostPopularDessert.setText(i.getName());
            }
        }

        ArrayList<PieEntry> entries = new ArrayList<>();

        for(ItemCount i : uniqueDesserts){
            float dessertPercentage = calculatePercentage(i.getCount(), totalFrequency);
            entries.add(new PieEntry(dessertPercentage, i.getName()));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Desserts");
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(dessertChart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        dessertChart.setData(pieData);
        dessertChart.invalidate();
    }

    private void loadMainsPieChartData(ArrayList<String> mains) {
        mainCourseChart.setCenterText("Mains");
        mainCourseChart.setCenterTextSize(24f);

        int totalFrequency = mains.size();
        Log.d(TAG, "Total Mains Frequency: " + totalFrequency);
        ArrayList<ItemCount> uniqueMains = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<String>(mains);
        for(String s : hashSet){
            ItemCount main = new ItemCount(s, 0);
            uniqueMains.add(main);
        }

        for(String s : mains){
            for(ItemCount i : uniqueMains){
                if(s.equalsIgnoreCase(i.getName())){
                    int count = i.getCount();
                    count = count + 1;
                    i.setCount(count);
                }
            }
        }

        int maxCount = uniqueMains.get(0).getCount();
        mostPopularMain.setText(uniqueMains.get(0).getName());
        for(ItemCount i : uniqueMains){
            if(maxCount < i.getCount()){
                maxCount = i.getCount();
                mostPopularMain.setText(i.getName());
            }
        }

        ArrayList<PieEntry> entries = new ArrayList<>();

        for(ItemCount i : uniqueMains){
            float mainPercentage = calculatePercentage(i.getCount(), totalFrequency);
            entries.add(new PieEntry(mainPercentage, i.getName()));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Mains");
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(mainCourseChart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        mainCourseChart.setData(pieData);
        mainCourseChart.invalidate();
    }

    private void loadStartersPieChartData(ArrayList<String> starters) {
        starterChart.setCenterText("Starters");
        starterChart.setCenterTextSize(24f);

        int totalFrequency = starters.size();
        Log.d(TAG, "Total Starters Frequency: " + totalFrequency);
        ArrayList<ItemCount> uniqueStarters = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<String>(starters);
        for(String s : hashSet){
            ItemCount starter = new ItemCount(s, 0);
            uniqueStarters.add(starter);
        }

        for(String s : starters){
            for(ItemCount i : uniqueStarters){
                if(s.equalsIgnoreCase(i.getName())){
                    int count = i.getCount();
                    count = count + 1;
                    i.setCount(count);
                }
            }
        }

        int maxCount = uniqueStarters.get(0).getCount();
        mostPopularStarter.setText(uniqueStarters.get(0).getName());
        for(ItemCount i : uniqueStarters){
            if(maxCount < i.getCount()){
                maxCount = i.getCount();
                mostPopularStarter.setText(i.getName());
            }
        }

        ArrayList<PieEntry> entries = new ArrayList<>();

        for(ItemCount i : uniqueStarters){
            float starterPercentage = calculatePercentage(i.getCount(), totalFrequency);
            entries.add(new PieEntry(starterPercentage, i.getName()));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Starters");
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(starterChart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        starterChart.setData(pieData);
        starterChart.invalidate();

    }

    private void loadDrinkPieChartData(ArrayList<String> drinks) {
        drinkChart.setCenterText("Drinks");
        drinkChart.setCenterTextSize(24f);

        int totalFrequency = drinks.size();
        Log.d(TAG, "Total Drinks Frequency: " + totalFrequency);
        ArrayList<ItemCount> uniqueDrinks = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<String>(drinks);
        for(String s : hashSet){
            ItemCount drink = new ItemCount(s, 0);
            uniqueDrinks.add(drink);
        }

        for(String s : drinks){
            for(ItemCount i : uniqueDrinks){
                if(s.equalsIgnoreCase(i.getName())){
                    int count = i.getCount();
                    count = count + 1;
                    i.setCount(count);
                }
            }
        }

        int maxCount = uniqueDrinks.get(0).getCount();
        mostPopularDrink.setText(uniqueDrinks.get(0).getName());
        for(ItemCount i : uniqueDrinks){
            if(maxCount < i.getCount()){
                maxCount = i.getCount();
                mostPopularDrink.setText(i.getName());
            }
        }

        ArrayList<PieEntry> entries = new ArrayList<>();

        for(ItemCount i : uniqueDrinks){
            float drinkPercentage = calculatePercentage(i.getCount(), totalFrequency);
            entries.add(new PieEntry(drinkPercentage, i.getName()));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Drinks");
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(drinkChart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        drinkChart.setData(pieData);
        drinkChart.invalidate();
    }

    private void loadItemPieChartData(ArrayList<String> items) {

        itemChart.setCenterText("Food Items");
        itemChart.setCenterTextSize(24f);

        int totalFrequency = items.size();
        Log.d(TAG, "Total Frequency: " + totalFrequency);
        ArrayList<ItemCount> uniqueItems = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<String>(items);
        for(String s: hashSet){
            ItemCount item = new ItemCount(s, 0);
            uniqueItems.add(item);
        }

        for(String s: items){
            for(ItemCount i : uniqueItems){
                if(s.equalsIgnoreCase(i.getName())){
                    int count = i.getCount();
                    count = count + 1;
                    i.setCount(count);
                }
            }
        }

        int maxCount = uniqueItems.get(0).getCount();
        mostPopularItem.setText(uniqueItems.get(0).getName());
        for(ItemCount i : uniqueItems){
            if(maxCount <= i.getCount()){
                maxCount = i.getCount();
                mostPopularItem.setText(i.getName());
            }
        }

        ArrayList<PieEntry> entries = new ArrayList<>();

        for(ItemCount i: uniqueItems){
            float itemPercentage = calculatePercentage(i.getCount(), totalFrequency);
            entries.add(new PieEntry(itemPercentage, i.getName()));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Items");
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(itemChart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        itemChart.setData(pieData);
        itemChart.invalidate();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadPieChartData(ArrayList<SalesData> data) {

        salesChart.setCenterText("Sales");
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