package com.example.fypapplication;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AverageRatingsFragment extends Fragment {

    private TextView average;
    private FirebaseFirestore db;
    private ArrayList<String> ratings = new ArrayList<>();
    private String averageRating;
    private final String TAG = "TAG";
    private PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View averageRatingsView = inflater.inflate(R.layout.fragment_average_ratings, container, false);
        db = FirebaseFirestore.getInstance();

        average = averageRatingsView.findViewById(R.id.averageRatingValue);
        pieChart = averageRatingsView.findViewById(R.id.ratingsPieChart);
        setUpPieChart();


        db.collection("Reviews").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc: task.getResult()){
                        Map<String, Object> data = doc.getData();
                        String value = (String) data.get("rating");
                        Log.d(TAG, value);
                        ratings.add(value);
                    }
                    averageRating = calculateAverage(ratings);
                    Log.d(TAG, averageRating);
                    average.setText(averageRating + " / 5");
                    loadPieChartData(ratings);

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        return averageRatingsView;

    }

    private void setUpPieChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Star Ratings by Value");
        pieChart.setCenterTextSize(24f);
        pieChart.getDescription().setEnabled(false);

         Legend l = pieChart.getLegend();
         l.setEnabled(false);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);


    }

    private String calculateAverage(ArrayList<String> ratings){

        double total = 0;
        int length = ratings.size();
        Log.d(TAG, String.valueOf(length));

        for(String s: ratings){
            total = total + Double.parseDouble(s);
        }

        double average = total / length;

        return String.valueOf(average);

    }

    private void loadPieChartData(ArrayList<String> ratings){
        int totalFrequency = ratings.size();
        Log.d(TAG, "Total Frequency " + String.valueOf(totalFrequency));
        int pointFiveFrequencyCount = 0;
        int oneFrequencyCount = 0;
        int onePointFiveFrequencyCount = 0;
        int twoFrequencyCount = 0;
        int twoPointFiveFrequencyCount = 0;
        int threeFrequencyCount = 0;
        int threePointFiveFrequencyCount = 0;
        int fourFrequencyCount = 0;
        int fourPointFiveFrequencyCount = 0;
        int fiveFrequencyCount = 0;

        for(String s: ratings){
            if(s.equalsIgnoreCase("0.5")){
                pointFiveFrequencyCount ++;

            }
            if(s.equalsIgnoreCase("1.0")){
                oneFrequencyCount ++;
            }
            if(s.equalsIgnoreCase("1.5")){
                onePointFiveFrequencyCount ++;
            }
            if(s.equalsIgnoreCase("2.0")){
                twoFrequencyCount ++;
            }
            if(s.equalsIgnoreCase("2.5")){
                twoPointFiveFrequencyCount ++;
            }
            if(s.equalsIgnoreCase("3.0")){
                threeFrequencyCount ++;
            }
            if(s.equalsIgnoreCase("3.5")){
                threePointFiveFrequencyCount ++;
            }
            if(s.equalsIgnoreCase("4.0")){
                fourFrequencyCount ++;
            }
            if(s.equalsIgnoreCase("4.5")){
                fourPointFiveFrequencyCount ++;
            }
            if(s.equalsIgnoreCase("5.0")){
                fiveFrequencyCount ++;
            }
        }

        Log.d(TAG, "Point Five Frequency " + pointFiveFrequencyCount);
        Log.d(TAG, "One Frequency " + oneFrequencyCount);
        Log.d(TAG, "One Point Five Frequency " + onePointFiveFrequencyCount);
        Log.d(TAG, "Two Frequency " + twoFrequencyCount);
        Log.d(TAG, "Two Point Five Frequency " + twoPointFiveFrequencyCount);
        Log.d(TAG, "Three Frequency " + threeFrequencyCount);
        Log.d(TAG, "Three Point Five Frequency " + threePointFiveFrequencyCount);
        Log.d(TAG, "Four Frequency " + fourFrequencyCount);
        Log.d(TAG, "Four Point FIve Frequency " + fourPointFiveFrequencyCount);
        Log.d(TAG, "Five Frequency " + fiveFrequencyCount);

        float pointFiveStars = calculatePercentage(pointFiveFrequencyCount, totalFrequency);
        Log.d(TAG, "Point Five Floating Number " + pointFiveStars);
        float oneStars = calculatePercentage(oneFrequencyCount, totalFrequency);
        Log.d(TAG, "One Floating Number " + oneStars);
        float onePointFiveStars = calculatePercentage(onePointFiveFrequencyCount, totalFrequency);
        Log.d(TAG, "One Point Five Floating Number " + onePointFiveStars);
        float twoStars = calculatePercentage(twoFrequencyCount, totalFrequency);
        Log.d(TAG, "Two Floating Number " + twoStars);
        float twoPointFiveStars = calculatePercentage(twoPointFiveFrequencyCount, totalFrequency);
        Log.d(TAG, "Two Point Five Floating Number " + twoPointFiveStars);
        float threeStars = calculatePercentage(threeFrequencyCount, totalFrequency);
        Log.d(TAG, "Three Floating Number " + threeStars);
        float threePointFiveStars = calculatePercentage(threePointFiveFrequencyCount, totalFrequency);
        Log.d(TAG, "Three Point Five Floating Number " + threePointFiveStars);
        float fourStars = calculatePercentage(fourFrequencyCount, totalFrequency);
        Log.d(TAG, "Four Floating Number " + fourStars);
        float fourPointFiveStars = calculatePercentage(fourPointFiveFrequencyCount, totalFrequency);
        Log.d(TAG, "Four Point Five Floating Number " + fourPointFiveStars);
        float fiveStars = calculatePercentage(fiveFrequencyCount, totalFrequency);
        Log.d(TAG, "Five Floating Number " + fiveStars);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(pointFiveStars, "0.5 Stars"));
        entries.add(new PieEntry(oneStars, "1 Stars"));
        entries.add(new PieEntry(onePointFiveStars, "1.5 Stars"));
        entries.add(new PieEntry(twoStars, "2 Stars"));
        entries.add(new PieEntry(twoPointFiveStars, "2.5 Stars"));
        entries.add(new PieEntry(threeStars, "3 Stars"));
        entries.add(new PieEntry(threePointFiveStars, "3.5 Stars"));
        entries.add(new PieEntry(fourStars, "4 Stars"));
        entries.add(new PieEntry(fourPointFiveStars, "4.5 Stars"));
        entries.add(new PieEntry(fiveStars, "5 Stars"));

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        for(int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        for(int color: ColorTemplate.COLORFUL_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Star Ratings");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    private float calculatePercentage(int frequency, int totalFrequency) {
        return (float) frequency / totalFrequency;
    }

}