package com.example.fypapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CurrentRoster extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView mRecyclerView;
    private Button addToRoster;
    private rosterAdapter adapter;
    private Intent data;
    private String date;
    private final String TAG = "TAG";
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_roster);

        db = FirebaseFirestore.getInstance();
        data = getIntent();
        date = data.getStringExtra("date");
        Log.d(TAG, "Date: " + date);

        mRecyclerView = findViewById(R.id.currentRosterRecycler);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler();
        addToRoster = findViewById(R.id.addToRoster);

        addToRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CurrentRoster.this, AddToRoster.class);
                i.putExtra("date", date);
                startActivity(i);
            }
        });
    }

    private void setUpRecycler() {
        Query query = db.collection("Roster").whereEqualTo("date", date).orderBy("role", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Roster> options = new FirestoreRecyclerOptions.Builder<Roster>().setQuery(query, Roster.class).build();
        adapter = new rosterAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter != null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_staff_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.backToStaffMain){
            Intent i = new Intent(CurrentRoster.this, OwnerMain.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}