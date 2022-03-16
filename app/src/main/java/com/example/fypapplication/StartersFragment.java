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


public class StartersFragment extends Fragment {

    private FirebaseFirestore db;
    private addMenuItemAdapter adapter;
    private RecyclerView startersRecycler;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View startersView = inflater.inflate(R.layout.fragment_starters, container, false);
        db = FirebaseFirestore.getInstance();
        startersRecycler = startersView.findViewById(R.id.newOrderStartersRecycler);
        startersRecycler.addItemDecoration(new DividerItemDecoration(startersView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler();
        return startersView;
    }

    private void setUpRecycler(){
        Query query = db.collection("Menu").whereEqualTo("type", "Starter").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        adapter = new addMenuItemAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        startersRecycler.setLayoutManager(staggeredGridLayoutManager);
        startersRecycler.setAdapter(adapter);
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