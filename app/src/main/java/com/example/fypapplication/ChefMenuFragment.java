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


public class ChefMenuFragment extends Fragment {

    private String role;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private RecyclerView mRecyclerView;
    private FirebaseFirestore db;
    private final String TAG = "TAG";
    private chefMenuAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View chefMenuView = inflater.inflate(R.layout.fragment_chef_menu, container, false);
        db = FirebaseFirestore.getInstance();

        if(getArguments() != null){
            role = getArguments().getString("role");
        }
        mRecyclerView = chefMenuView.findViewById(R.id.chefMenuRecycler);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(chefMenuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler();

        return chefMenuView;
    }

    private void setUpRecycler() {
        Query query = db.collection("Menu").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        adapter = new chefMenuAdapter(options);
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