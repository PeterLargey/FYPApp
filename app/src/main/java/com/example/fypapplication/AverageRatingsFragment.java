package com.example.fypapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View averageRatingsView = inflater.inflate(R.layout.fragment_average_ratings, container, false);
        db = FirebaseFirestore.getInstance();

        average = averageRatingsView.findViewById(R.id.averageRatingValue);

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

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        return averageRatingsView;
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

}