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


public class KidsFragment extends Fragment {

    private FirebaseFirestore db;
    private addMenuItemAdapter adapter;
    private RecyclerView kidRecycler;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View kidView = inflater.inflate(R.layout.fragment_kids, container, false);
        db = FirebaseFirestore.getInstance();
        kidRecycler = kidView.findViewById(R.id.newOrderKidsRecycler);
        kidRecycler.addItemDecoration(new DividerItemDecoration(kidView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler();

        return kidView;
    }

    private void setUpRecycler(){
        Query query = db.collection("Menu").whereEqualTo("type", "Kid").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        adapter = new addMenuItemAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        kidRecycler.setLayoutManager(staggeredGridLayoutManager);
        kidRecycler.setAdapter(adapter);
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