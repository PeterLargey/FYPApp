package com.example.fypapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DisplayStaffFragment extends Fragment {

    private FirebaseFirestore db;
    private staffAdapter adapter;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View displayView = inflater.inflate(R.layout.fragment_display_users, container,false);
        db = FirebaseFirestore.getInstance();
        mRecyclerView = displayView.findViewById(R.id.adminStaffRecycler);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(displayView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecyclerView();
        return displayView;
    }

    private void setUpRecyclerView(){
        Query query = db.collection("Staff").orderBy("role", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Staff> options = new FirestoreRecyclerOptions.Builder<Staff>().setQuery(query, Staff.class).build();
        adapter = new staffAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
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