package com.example.fypapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddReservationFragment extends Fragment {
    private final String TAG = "TAG";
    private FirebaseFirestore db;
    private TextView date, time, name, numberOfGuests;
    private Button createReso;
    private String resoDate, resoTime;
    private int hour, minute;
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View addResoView = inflater.inflate(R.layout.fragment_add_reservation, container, false);
        db = FirebaseFirestore.getInstance();
        date = addResoView.findViewById(R.id.resoDate);
        time = addResoView.findViewById(R.id.resoTime);
        name = addResoView.findViewById(R.id.newResoName);
        numberOfGuests = addResoView.findViewById(R.id.newResoNumberOfGuests);
        createReso = addResoView.findViewById(R.id.createReservation);
        initDatePicker();
        initTimePicker();


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timePicker.show();
            }
        });

        createReso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String resoDate = date.getText().toString();
                String resoTime = time.getText().toString();
                String resoName = name .getText().toString();
                String resoGuests = numberOfGuests.getText().toString();

                if(resoDate.isEmpty() || resoTime.isEmpty() || resoName.isEmpty() || resoGuests.isEmpty()){
                    Toast.makeText(addResoView.getContext(), "All fields required", Toast.LENGTH_LONG).show();
                } else if (resoDate.equalsIgnoreCase("dd/mm/yyyy")){
                    Toast.makeText(addResoView.getContext(), "Enter a valid date", Toast.LENGTH_LONG).show();
                } else if(resoTime.equalsIgnoreCase("00:00")){
                    Toast.makeText(addResoView.getContext(), "Enter a valid time", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(view.getContext(), "Reservation Created", Toast.LENGTH_LONG).show();
                    inputDataIntoReservations(resoDate, resoTime, resoName, resoGuests);
                    Intent i = new Intent(addResoView.getContext(), HostMain.class);
                    startActivity(i);
                }

            }
        });


        return addResoView;
    }

    private void initTimePicker(){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                resoTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                Log.d(TAG, resoTime);
                time.setText(resoTime);
                time.setTextColor(Color.BLACK);
            }
        };

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        timePicker = new TimePickerDialog(getActivity(), style, onTimeSetListener, hour, minute, true);
        timePicker.setTitle("Select Time");
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;
                resoDate = makeDateString(year, month, day);
                Log.d(TAG, resoDate);
                date.setText(resoDate);
                date.setTextColor(Color.BLACK);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //int style = AlertDialog.THEME_HOLO_LIGHT;

        datePicker = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePicker.setTitle("Select Date");

    }

    private String makeDateString(int year, int month, int day) {
        return day +  " " +  getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month){
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

    private void inputDataIntoReservations(String date, String time, String name, String numberOfGuests){
        DocumentReference docRef = db.collection("Reservations").document();
        Map<String, Object> newReservation = new HashMap<>();
        newReservation.put("date", date);
        newReservation.put("time", time);
        newReservation.put("name", name);
        newReservation.put("numberOfGuests", numberOfGuests);
        docRef.set(newReservation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Success, new reservation was added to the database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed, new reservation was not added to the database");
            }
        });

    }
}