package com.example.fypapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditReservation extends AppCompatActivity {

    private Intent data;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private TextView dateTxt, timeTxt;
    private EditText nameTxt, guests;
    private Button editReservation;
    private String docId;
    private int hour, minute;
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;
    private final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);
        getSupportActionBar().setTitle("Host Home Screen");
        db = FirebaseFirestore.getInstance();
        data = getIntent();

        docId = data.getStringExtra("docId");
        String name = data.getStringExtra("name");
        String date = data.getStringExtra("date");
        String time = data.getStringExtra("time");
        String numberOfGuests = data.getStringExtra("numberOfGuests");

        dateTxt = findViewById(R.id.editResoDate);
        timeTxt = findViewById(R.id.editResoTime);
        nameTxt = findViewById(R.id.editResoName);
        guests = findViewById(R.id.editResoNumberOfGuests);
        editReservation = findViewById(R.id.editReservation);

        dateTxt.setText(date);
        timeTxt.setText(time);
        nameTxt.setText(name);
        guests.setText(numberOfGuests);

        initDatePicker();
        initTimePicker();

        dateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });

        timeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.show();
            }
        });

        editReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String updatedName = nameTxt.getText().toString();
                String updatedTime = timeTxt.getText().toString();
                String updatedDate = dateTxt.getText().toString();
                String updatedNumberOfGuests = guests.getText().toString();

                Toast.makeText(view.getContext(), "Reservation Updated", Toast.LENGTH_LONG).show();
                updateReservationData(updatedName, updatedDate, updatedTime, updatedNumberOfGuests);
                Intent i = new Intent(EditReservation.this, HostMain.class);
                startActivity(i);
            }
        });


    }

    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                String edittedResoTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                Log.d(TAG, edittedResoTime);
                timeTxt.setText(edittedResoTime);
                timeTxt.setTextColor(Color.BLACK);
            }
        };

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        timePicker = new TimePickerDialog(this, style, timeSetListener, hour, minute, true);
        timePicker.setTitle("Select Time");
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String edittedResoDate = makeDateString(year, month, day);
                Log.d(TAG, edittedResoDate);
                dateTxt.setText(edittedResoDate);
                dateTxt.setTextColor(Color.BLACK);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePicker.setTitle("Update Date");

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

    private void updateReservationData(String name, String date, String time, String guests) {
        DocumentReference docRef = db.collection("Reservations").document(docId);
        Map<String, Object> updatedReso = new HashMap<>();
        updatedReso.put("name", name);
        updatedReso.put("date", date);
        updatedReso.put("time", time);
        updatedReso.put("numberOfGuests", guests);
        docRef.set(updatedReso).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Success, reservation was updated in the database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed, reservation failed to update in the database");
            }
        });
    }
}