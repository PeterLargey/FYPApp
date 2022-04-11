package com.example.fypapplication;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class RosterFragment extends Fragment {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private Button previousMonth, nextMonth;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rosterView = inflater.inflate(R.layout.fragment_roster, container, false);

        monthYearText = rosterView.findViewById(R.id.rosterMonthYear);
        calendarRecyclerView = rosterView.findViewById(R.id.calendarRecyclerView);
        previousMonth = rosterView.findViewById(R.id.previous);
        nextMonth = rosterView.findViewById(R.id.next);
        selectedDate = LocalDate.now();
        setMonthView();

        previousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusMonths(1);
                setMonthView();
            }
        });

        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusMonths(1);
                setMonthView();
            }
        });


        return rosterView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        calendarAdapter adapter = new calendarAdapter(daysInMonth, selectedDate);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
        calendarRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        calendarRecyclerView.setAdapter(adapter);


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonth = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int days = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++){
            if(i <= dayOfWeek || i > days + dayOfWeek){
                daysInMonth.add("");
            }else {
                daysInMonth.add(String.valueOf(i - dayOfWeek));
            }
        }

        return daysInMonth;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

}