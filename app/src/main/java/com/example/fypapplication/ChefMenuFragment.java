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
    private RecyclerView specialRecyclerView, starterRecyclerView, mainRecyclerView, dessertRecyclerView;
    private FirebaseFirestore db;
    private final String TAG = "TAG";
    private chefMenuAdapter specialAdapter, starterAdapter, mainAdapter, dessertAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View chefMenuView = inflater.inflate(R.layout.fragment_chef_menu, container, false);
        db = FirebaseFirestore.getInstance();

        if(getArguments() != null){
            role = getArguments().getString("role");
        }
        specialRecyclerView = chefMenuView.findViewById(R.id.chefSpecialMenuRecycler);
        specialRecyclerView.addItemDecoration(new DividerItemDecoration(chefMenuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpSpecialRecycler(role);
        starterRecyclerView = chefMenuView.findViewById(R.id.chefStarterMenuRecycler);
        starterRecyclerView.addItemDecoration(new DividerItemDecoration(chefMenuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpStarterRecycler(role);
        mainRecyclerView = chefMenuView.findViewById(R.id.chefMainMenuRecycler);
        mainRecyclerView.addItemDecoration(new DividerItemDecoration(chefMenuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpMainRecycler(role);
        dessertRecyclerView = chefMenuView.findViewById(R.id.chefDessertMenuRecycler);
        dessertRecyclerView.addItemDecoration(new DividerItemDecoration(chefMenuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpDessertRecycler(role);

        return chefMenuView;
    }

    private void setUpDessertRecycler(String role) {
        Query query = db.collection("Menu").whereEqualTo("type", "Dessert").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        dessertAdapter = new chefMenuAdapter(options, role);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        dessertRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        dessertRecyclerView.setAdapter(dessertAdapter);
    }

    private void setUpMainRecycler(String role) {
        Query query = db.collection("Menu").whereEqualTo("type", "Main").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        mainAdapter = new chefMenuAdapter(options, role);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mainRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mainRecyclerView.setAdapter(mainAdapter);
    }

    private void setUpStarterRecycler(String role) {
        Query query = db.collection("Menu").whereEqualTo("type", "Starter").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        starterAdapter = new chefMenuAdapter(options, role);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        starterRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        starterRecyclerView.setAdapter(starterAdapter);
    }

    private void setUpSpecialRecycler(String role) {
        Query query = db.collection("Menu").whereEqualTo("type", "Special").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        specialAdapter = new chefMenuAdapter(options, role);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        specialRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        specialRecyclerView.setAdapter(specialAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(specialAdapter != null){
            specialAdapter.startListening();
        }

        if(starterAdapter != null){
            starterAdapter.startListening();
        }

        if(mainAdapter != null){
            mainAdapter.startListening();
        }

        if(dessertAdapter != null){
            dessertAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(specialAdapter != null){
            specialAdapter.stopListening();
        }

        if(starterAdapter != null){
            starterAdapter.stopListening();
        }

        if(mainAdapter != null){
            mainAdapter.stopListening();
        }

        if(dessertAdapter != null){
            dessertAdapter.stopListening();
        }
    }

}