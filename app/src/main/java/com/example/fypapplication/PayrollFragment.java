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


public class PayrollFragment extends Fragment {

    private FirebaseFirestore db;
    private payrollAdapter adapter;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View payrollView = inflater.inflate(R.layout.fragment_payroll, container, false);
        db = FirebaseFirestore.getInstance();
        mRecyclerView = payrollView.findViewById(R.id.payrollRecycler);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(payrollView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler();
        return payrollView;
    }

    private void setUpRecycler() {
        Query query = db.collection("Staff").whereNotEqualTo("role", "owner").orderBy("role", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Staff> options = new FirestoreRecyclerOptions.Builder<Staff>().setQuery(query, Staff.class).build();
        adapter = new payrollAdapter(options);
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