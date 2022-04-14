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


public class CurrentInventoryFragment extends Fragment {

    private String role;
    private RecyclerView mRecyclerView;
    private inventoryAdapter adapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private FirebaseFirestore db;
    private final String TAG = "TAG";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View currentInventoryView = inflater.inflate(R.layout.fragment_current_inventory, container, false);
        db = FirebaseFirestore.getInstance();
        if(getArguments() != null){
            role = getArguments().getString("role");
        }
        mRecyclerView = currentInventoryView.findViewById(R.id.currentInventoryRecycler);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(currentInventoryView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler();

        return currentInventoryView;
    }

    private void setUpRecycler() {
        Query query = db.collection("Inventory").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<InventoryItem> options = new FirestoreRecyclerOptions.Builder<InventoryItem>().setQuery(query, InventoryItem.class).build();
        adapter = new inventoryAdapter(options, role);
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