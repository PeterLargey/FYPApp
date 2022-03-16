package com.example.fypapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UserMenuFragment extends Fragment {

    private final String TAG = "TAG";
    private FirebaseFirestore db;
    private menuAdapter specialAdapter, starterAdapter, mainAdapter, dessertAdapter, drinkAdapter;
    private RecyclerView specialRecycler, starterRecycler, mainRecycler, dessertRecycler, drinkRecycler;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private String customerName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View menuView = inflater.inflate(R.layout.fragment_user_menu, container,false);
        db = FirebaseFirestore.getInstance();
        if(getArguments() != null){
            customerName = getArguments().getString("customerName");
        }
        specialRecycler = menuView.findViewById(R.id.specialMenuRecycler);
        specialRecycler.addItemDecoration(new DividerItemDecoration(menuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpSpecialRecycler(customerName);
        starterRecycler = menuView.findViewById(R.id.starterMenuRecycler);
        starterRecycler.addItemDecoration(new DividerItemDecoration(menuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler(customerName);
        mainRecycler = menuView.findViewById(R.id.mainMenuRecycler);
        mainRecycler.addItemDecoration(new DividerItemDecoration(menuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpMainRecycler(customerName);
        dessertRecycler = menuView.findViewById(R.id.dessertMenuRecycler);
        dessertRecycler.addItemDecoration(new DividerItemDecoration(menuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpDessertRecycler(customerName);
        drinkRecycler = menuView.findViewById(R.id.drinkMenuRecycler);
        drinkRecycler.addItemDecoration(new DividerItemDecoration(menuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpDrinkRecycler(customerName);

        return menuView;
    }

    private void setUpSpecialRecycler(String customerName){
        Query query = db.collection("Menu").whereEqualTo("type", "Special").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        specialAdapter = new menuAdapter(options, customerName);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        specialRecycler.setLayoutManager(staggeredGridLayoutManager);
        specialRecycler.setAdapter(specialAdapter);
    }

    private void setUpRecycler(String customerName){
        Query query = db.collection("Menu").whereEqualTo("type", "Starter").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        starterAdapter = new menuAdapter(options, customerName);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        starterRecycler.setLayoutManager(staggeredGridLayoutManager);
        starterRecycler.setAdapter(starterAdapter);
    }

    private void setUpMainRecycler(String customerName){
        Query query = db.collection("Menu").whereEqualTo("type", "Main").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        mainAdapter = new menuAdapter(options, customerName);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mainRecycler.setLayoutManager(staggeredGridLayoutManager);
        mainRecycler.setAdapter(mainAdapter);
    }

    private void setUpDessertRecycler(String customerName){
        Query query = db.collection("Menu").whereEqualTo("type", "Dessert").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        dessertAdapter = new menuAdapter(options, customerName);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        dessertRecycler.setLayoutManager(staggeredGridLayoutManager);
        dessertRecycler.setAdapter(dessertAdapter);
    }

    private void setUpDrinkRecycler(String customerName){
        Query query = db.collection("Menu").whereEqualTo("type", "Drink").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        drinkAdapter = new menuAdapter(options,customerName);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        drinkRecycler.setLayoutManager(staggeredGridLayoutManager);
        drinkRecycler.setAdapter(drinkAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        if(starterAdapter != null){
            starterAdapter.startListening();
        }

        if(mainAdapter != null){
            mainAdapter.startListening();
        }

        if(dessertAdapter != null){
            dessertAdapter.startListening();
        }

        if(drinkAdapter != null){
            drinkAdapter.startListening();
        }

        if(specialAdapter != null){
            specialAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(starterAdapter != null){
            starterAdapter.stopListening();
        }

        if(mainAdapter != null){
            mainAdapter.stopListening();
        }

        if(dessertAdapter != null){
            dessertAdapter.stopListening();
        }

        if(drinkAdapter != null){
            drinkAdapter.stopListening();
        }

        if(specialAdapter != null){
            specialAdapter.stopListening();
        }
    }
}