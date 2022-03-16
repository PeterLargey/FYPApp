package com.example.fypapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ServerTablesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private tableAdapter adapter;
    private final String TAG = "TAG";
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private String role;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View tablesView = inflater.inflate(R.layout.fragment_server_tables, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        if(getArguments() != null){
            role = getArguments().getString("role");
        }
        mRecyclerView = tablesView.findViewById(R.id.tablesRecycler);
        setUpRecycler(role);
        return tablesView;
    }

    private void setUpRecycler(String role){
        Query query = db.collection("Tables").orderBy("tableNo", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Table> options = new FirestoreRecyclerOptions.Builder<Table>().setQuery(query, Table.class).build();
        adapter = new tableAdapter(options, role);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(adapter);
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