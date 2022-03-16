package com.example.fypapplication;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class CurrentReservationsFragment extends Fragment {

    private final String TAG = "TAG";
    private RecyclerView mRecyclerView;
    private TextView currentDate, emptyMessage;
    private reservationAdapter adapter;
    private FirebaseFirestore db;
    private String date, searchDate;
    private StringBuilder dateForDB;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View currentResoView = inflater.inflate(R.layout.fragment_current_reservations, container, false);
        db = FirebaseFirestore.getInstance();
        mRecyclerView = currentResoView.findViewById(R.id.currentReservationsRecycler);
        currentDate = currentResoView.findViewById(R.id.currentDate);
        emptyMessage = currentResoView.findViewById(R.id.emptyMessage);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(currentResoView.getContext(), DividerItemDecoration.VERTICAL));
        LocalDate now = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        //date = DateFormat.getDateInstance(DateFormat.SHORT).format(now);
        date = dtf.format(now);
        Log.d(TAG, "Current Date as date object: " + now);
        Log.d(TAG, "Current Date: " + date);
        String[] split = date.split("/");
        String day = split[0];
        Log.d(TAG, day);
        String month = split[1];
        Log.d(TAG, month);
        String year = split[2];
        int monthInt = Integer.parseInt(month);
        Log.d(TAG, year);
        String formattedMonth = formatMonth(monthInt);
        dateForDB = new StringBuilder();
        dateForDB.append(day + " " + formattedMonth + " " + year);
        searchDate = String.valueOf(dateForDB);
        Log.d(TAG, searchDate);
        currentDate.setText(searchDate);
        setUpRecycler();
        return currentResoView;
    }

    private String formatMonth(int month) {
        if(month == 1){
            return "JAN";
        }
        if(month == 2){
            return "FEB";
        }
        if(month == 3){
            return "MAR";
        }
        if(month == 4){
            return "APR";
        }
        if(month == 5){
            return "MAY";
        }
        if(month == 6){
            return "JUN";
        }
        if(month == 7){
            return "JUL";
        }
        if(month == 8){
            return "AUG";
        }
        if(month == 9){
            return "SEP";
        }
        if(month == 10){
            return "OCT";
        }
        if(month == 11){
            return "NOV";
        }
        if(month == 12){
            return "DEC";
        }
        return "JAN";
    }

    private void setUpRecycler(){
        Query query = db.collection("Reservations").whereEqualTo("date", searchDate).orderBy("time", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty()){
                        emptyMessage.setVisibility(View.VISIBLE);
                    }
                }else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        FirestoreRecyclerOptions<Reservation> options = new FirestoreRecyclerOptions.Builder<Reservation>().setQuery(query, Reservation.class).build();
        adapter = new reservationAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(adapter);
//        if(mRecyclerView.getChildCount() != 0){
//            emptyMessage.setVisibility(View.INVISIBLE);
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }
}