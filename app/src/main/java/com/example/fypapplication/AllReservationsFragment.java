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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

public class AllReservationsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private allReservationsAdapter adapter;
    private FirebaseFirestore db;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private TextView emptyMessage;
    private Map<String, Object> data;
    private final String TAG = "TAG";
    private String role, staffMember;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View allResoView = inflater.inflate(R.layout.fragment_all_reservations, container, false);
        db = FirebaseFirestore.getInstance();
        if(getArguments() != null){
            role = getArguments().getString("role");
            staffMember = getArguments().getString("staffMember");
        }

        mRecyclerView = allResoView.findViewById(R.id.allReservationsRecycler);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(allResoView.getContext(), DividerItemDecoration.VERTICAL));
        emptyMessage = allResoView.findViewById(R.id.allReservationsEmptyMessage);
        clearOldResos();
        setUpRecycler(role, staffMember);
        return allResoView;
    }

    private void setUpRecycler(String role, String staffMember){
        Query query = db.collection("Reservations").orderBy("date", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty()){
                        emptyMessage.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        FirestoreRecyclerOptions<Reservation> options = new FirestoreRecyclerOptions.Builder<Reservation>().setQuery(query, Reservation.class).build();
        adapter = new allReservationsAdapter(options, role, staffMember);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void clearOldResos(){
        LocalDate now = LocalDate.now();
        db.collection("Reservations").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc: task.getResult()){
                        data = doc.getData();
                        Object[] values = data.values().toArray();
                        String date = values[0].toString();
                        Log.d(TAG, "Document snapshot data: " + data.values());
                        Log.d(TAG, "Date: " + date);
                        Log.d(TAG, "Re-Formatted Date: " + reformatDate(date));
                        LocalDate resoDate = reformatDate(date);
                        if(resoDate.isBefore(now)){
                            doc.getReference().delete();
                        }
                    }

                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDate reformatDate(String date){
        String[] dateSplit = date.split(" ");
        String day = dateSplit[0];
        String dayFormat = dayFormat(day);
        String month = dateSplit[1];
        String monthFormat = monthFormat(month);
        String dateToBeFormatted = dateSplit[2] + "-" + monthFormat + "-" + dayFormat;

        return LocalDate.parse(dateToBeFormatted);
    }

    private String dayFormat(String day) {
        if(day.equalsIgnoreCase("1")){
            return "01";
        }
        if(day.equalsIgnoreCase("2")){
            return "02";
        }
        if(day.equalsIgnoreCase("3")){
            return "03";
        }
        if(day.equalsIgnoreCase("4")){
            return "04";
        }
        if(day.equalsIgnoreCase("5")){
            return "05";
        }
        if(day.equalsIgnoreCase("6")){
            return "06";
        }
        if(day.equalsIgnoreCase("7")){
            return "07";
        }
        if(day.equalsIgnoreCase("8")){
            return "08";
        }
        if(day.equalsIgnoreCase("9")){
            return "09";
        }

        return day;
    }

    private String monthFormat(String data){
        if(data.equalsIgnoreCase("JAN")){
            return "01";
        }
        if(data.equalsIgnoreCase("FEB")){
            return "02";
        }
        if(data.equalsIgnoreCase("MAR")){
            return "03";
        }
        if(data.equalsIgnoreCase("APR")){
            return "04";
        }
        if(data.equalsIgnoreCase("MAY")){
            return "05";
        }
        if(data.equalsIgnoreCase("JUN")){
            return "06";
        }
        if(data.equalsIgnoreCase("JUL")){
            return "07";
        }
        if(data.equalsIgnoreCase("AUG")){
            return "08";
        }
        if(data.equalsIgnoreCase("SEP")){
            return "09";
        }
        if(data.equalsIgnoreCase("OCT")){
            return "10";
        }
        if(data.equalsIgnoreCase("NOV")){
            return "11";
        }
        if(data.equalsIgnoreCase("DEC")){
            return "12";
        }

        return "01";
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