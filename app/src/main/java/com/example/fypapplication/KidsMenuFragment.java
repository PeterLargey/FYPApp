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


public class KidsMenuFragment extends Fragment {

    private FirebaseFirestore db;
    private menuAdapter kidsAdapter;
    private RecyclerView kidsRecycler;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private String customerName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View kidsMenuView = inflater.inflate(R.layout.fragment_kids_menu, container, false);
        db = FirebaseFirestore.getInstance();
        if(getArguments() != null){
            customerName = getArguments().getString("customerName");
        }
        kidsRecycler = kidsMenuView.findViewById(R.id.kidsMenuRecycler);
        kidsRecycler.addItemDecoration(new DividerItemDecoration(kidsMenuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler(customerName);

        return kidsMenuView;
    }

    private void setUpRecycler(String customerName){
        Query query = db.collection("Menu").whereEqualTo("type", "Kid").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        kidsAdapter = new menuAdapter(options, customerName);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        kidsRecycler.setLayoutManager(staggeredGridLayoutManager);
        kidsRecycler.setAdapter(kidsAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(kidsAdapter != null){
            kidsAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(kidsAdapter != null){
            kidsAdapter.stopListening();
        }
    }
}