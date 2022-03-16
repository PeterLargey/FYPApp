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


public class DessertsFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView dessertRecycler;
    private addMenuItemAdapter adapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dessertsView = inflater.inflate(R.layout.fragment_desserts, container, false);
        db = FirebaseFirestore.getInstance();
        dessertRecycler = dessertsView.findViewById(R.id.newOrderDessertRecycler);
        dessertRecycler.addItemDecoration(new DividerItemDecoration(dessertsView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler();

        return dessertsView;
    }

    private void setUpRecycler(){
        Query query = db.collection("Menu").whereEqualTo("type", "Dessert").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        adapter = new addMenuItemAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        dessertRecycler.setLayoutManager(staggeredGridLayoutManager);
        dessertRecycler.setAdapter(adapter);
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