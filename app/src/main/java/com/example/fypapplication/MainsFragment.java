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


public class MainsFragment extends Fragment {

    private FirebaseFirestore db;
    private addMenuItemAdapter adapter;
    private RecyclerView mainsRecycler;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainsView = inflater.inflate(R.layout.fragment_mains, container, false);
        db = FirebaseFirestore.getInstance();
        mainsRecycler = mainsView.findViewById(R.id.newOrderMainsRecycler);
        mainsRecycler.addItemDecoration(new DividerItemDecoration(mainsView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler();
        return mainsView;
    }

    private void setUpRecycler(){
        Query query = db.collection("Menu").whereEqualTo("type", "Main").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        adapter = new addMenuItemAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mainsRecycler.setLayoutManager(staggeredGridLayoutManager);
        mainsRecycler.setAdapter(adapter);
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