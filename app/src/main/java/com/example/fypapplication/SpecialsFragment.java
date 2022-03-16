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


public class SpecialsFragment extends Fragment {

    private FirebaseFirestore db;
    private addMenuItemAdapter adapter;
    private RecyclerView specialsRecycler;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View specialsView = inflater.inflate(R.layout.fragment_specials, container, false);
        db = FirebaseFirestore.getInstance();
        specialsRecycler = specialsView.findViewById(R.id.newOrderSpecialsRecycler);
        specialsRecycler.addItemDecoration(new DividerItemDecoration(specialsView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler();
        return specialsView;
    }

    private void setUpRecycler(){
        Query query = db.collection("Menu").whereEqualTo("type", "Special").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        adapter = new addMenuItemAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        specialsRecycler.setLayoutManager(staggeredGridLayoutManager);
        specialsRecycler.setAdapter(adapter);
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