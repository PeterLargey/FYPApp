package com.example.fypapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;


public class UserAddReviewFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userId;
    private final String TAG = "TAG";
    private String userName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View addReviewView = inflater.inflate(R.layout.fragment_user_add_review, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getUid();
        db.collection("Customers").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                Log.d(TAG, "Retrieved data map");
                Log.d(TAG, String.valueOf(data));
                userName = (String) data.get("name");
                Log.d(TAG, userName);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to retrieve customer name");
            }
        });

        Button button = addReviewView.findViewById(R.id.createReview);
        EditText title = addReviewView.findViewById(R.id.newReviewTitle);
        EditText desc = addReviewView.findViewById(R.id.newReviewDesc);
        RatingBar rating = addReviewView.findViewById(R.id.reviewRatingBar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleString = title.getText().toString();
                String descString = desc.getText().toString();
                String ratingString = String.valueOf(rating.getRating());

                if(titleString.isEmpty() || descString.isEmpty() || ratingString.isEmpty()){
                    Toast.makeText(view.getContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else {
                    inputDataIntoReview(titleString, ratingString, descString, userName);
                    Toast.makeText(view.getContext(), "New review created", Toast.LENGTH_LONG).show();
                }
            }
        });

        return addReviewView;
    }

    public void inputDataIntoReview(String title, String rating, String desc, String userName){
        String TAG = "TAG";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Reviews").document();
        Map<String, Object> newReview = new HashMap<>();
        newReview.put("title", title);
        newReview.put("rating", rating);
        newReview.put("desc", desc);
        newReview.put("user", userName);

        docRef.set(newReview).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Success, new review added to the database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed, new review was not added to the database");
            }
        });

    }
}