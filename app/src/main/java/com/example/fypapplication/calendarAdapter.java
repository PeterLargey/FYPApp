package com.example.fypapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class calendarAdapter extends RecyclerView.Adapter<calendarAdapter.CalendarViewHolder> {

    private ArrayList<String> days;
    private LocalDate selectedDate;

    public calendarAdapter(ArrayList<String> days, LocalDate selectedDate) {
        this.days = days;
        this.selectedDate = selectedDate;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.1666666);
        return new CalendarViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.dayOfMonth.setText(days.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String date = holder.dayOfMonth.getText().toString() + " " + monthYearFromDate(selectedDate);
                String message = "Selected Date " + date;
                Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();
                Intent i = new Intent(view.getContext(), CurrentRoster.class);
                i.putExtra("date", date);
                view.getContext().startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return days.size();
    }


    public class CalendarViewHolder extends RecyclerView.ViewHolder {

        private TextView dayOfMonth;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }


}
