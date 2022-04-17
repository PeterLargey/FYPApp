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

public class EditMenuFragment extends Fragment {

    private final String TAG = "TAG";
    private FirebaseFirestore db;
    private managerMenuAdapter specialAdapter, starterAdapter, mainAdapter, dessertAdapter, drinkAdapter, kidAdapter;
    private RecyclerView specialRecycler, starterRecycler, mainRecycler, dessertRecycler, drinkRecycler, kidRecycler;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View editMenuView = inflater.inflate(R.layout.fragment_edit_menu, container, false);
        db = FirebaseFirestore.getInstance();
        specialRecycler = editMenuView.findViewById(R.id.specialManagerMenuRecycler);
        specialRecycler.addItemDecoration(new DividerItemDecoration(editMenuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpSpecialRecycler();
        starterRecycler = editMenuView.findViewById(R.id.starterManagerMenuRecycler);
        starterRecycler.addItemDecoration(new DividerItemDecoration(editMenuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler();
        mainRecycler = editMenuView.findViewById(R.id.mainManagerMenuRecycler);
        mainRecycler.addItemDecoration(new DividerItemDecoration(editMenuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpMainRecycler();
        dessertRecycler = editMenuView.findViewById(R.id.dessertManagerMenuRecycler);
        dessertRecycler.addItemDecoration(new DividerItemDecoration(editMenuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpDessertRecycler();
        drinkRecycler = editMenuView.findViewById(R.id.drinkManagerMenuRecycler);
        drinkRecycler.addItemDecoration(new DividerItemDecoration(editMenuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpDrinkRecycler();
        kidRecycler = editMenuView.findViewById(R.id.kidManagerMenuRecycler);
        kidRecycler.addItemDecoration(new DividerItemDecoration(editMenuView.getContext(), DividerItemDecoration.VERTICAL));
        setUpKidRecycler();

        return editMenuView;
    }

    private void setUpKidRecycler() {
        Query query = db.collection("Menu").whereEqualTo("type", "Kid").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        kidAdapter = new managerMenuAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        kidRecycler.setLayoutManager(staggeredGridLayoutManager);
        kidRecycler.setAdapter(kidAdapter);
    }

    private void setUpRecycler(){
        Query query = db.collection("Menu").whereEqualTo("type", "Starter").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        starterAdapter = new managerMenuAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        starterRecycler.setLayoutManager(staggeredGridLayoutManager);
        starterRecycler.setAdapter(starterAdapter);
    }

    private void setUpMainRecycler(){
        Query query = db.collection("Menu").whereEqualTo("type", "Main").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        mainAdapter = new managerMenuAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mainRecycler.setLayoutManager(staggeredGridLayoutManager);
        mainRecycler.setAdapter(mainAdapter);
    }

    private void setUpDessertRecycler(){
        Query query = db.collection("Menu").whereEqualTo("type", "Dessert").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        dessertAdapter = new managerMenuAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        dessertRecycler.setLayoutManager(staggeredGridLayoutManager);
        dessertRecycler.setAdapter(dessertAdapter);
    }

    private void setUpDrinkRecycler(){
        Query query = db.collection("Menu").whereEqualTo("type", "Drink").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        drinkAdapter = new managerMenuAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        drinkRecycler.setLayoutManager(staggeredGridLayoutManager);
        drinkRecycler.setAdapter(drinkAdapter);
    }

    private void setUpSpecialRecycler(){
        Query query = db.collection("Menu").whereEqualTo("type", "Special").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>().setQuery(query, MenuItem.class).build();
        specialAdapter = new managerMenuAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        specialRecycler.setLayoutManager(staggeredGridLayoutManager);
        specialRecycler.setAdapter(specialAdapter);
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

//    private void MenuData(){
//        String sectionOneTitle = "Starters";
//        //List<MenuItem> starters = new ArrayList<>();
//        db.collection("Menu").whereEqualTo("type", "Starter").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    List<MenuItem> starters = new ArrayList<>();
//                    for(QueryDocumentSnapshot document : task.getResult()){
//                        //Log.d(TAG, document.getId() + " => " + document.getData());
//                        Map<String, Object> data = document.getData();
//                        Object[] values = data.values().toArray();
//                        //Log.d(TAG, "Document snapshot data: " + values);
//                        String price = (String) values[0];
//                        //Log.d(TAG, "Price:  " + price);
//                        String name = (String) values[1];
//                        String type = (String) values[2];
//                        String desc = (String) values[3];
//                        //Log.d(TAG, "MenuItem:  " + name + " " + desc + " " + price);
//                        MenuItem item = new MenuItem(type, name, desc, price);
//                        //starters.add(item);
//                        starters.add(item);
//
//                    }
//                    MenuSection starterSection = new MenuSection(sectionOneTitle, starters);
//                    menuSections.add(starterSection);
//                    Log.d(TAG, "Menu Section" + menuSections.toString());
//
//                }else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        });
//
//
//
//        String sectionTwoTitle = "Mains";
//        db.collection("Menu").whereEqualTo("type", "Main").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    List<MenuItem> mains = new ArrayList<>();
//                    for(QueryDocumentSnapshot document : task.getResult()){
//                        //Log.d(TAG, document.getId() + " => " + document.getData());
//                        Map<String, Object> data = document.getData();
//                        Object[] values = data.values().toArray();
//                        //Log.d(TAG, "Document snapshot data: " + values);
//                        String price = (String) values[0];
//                        //Log.d(TAG, "Price:  " + price);
//                        String name = (String) values[1];
//                        String type = (String) values[2];
//                        String desc = (String) values[3];
//                        //Log.d(TAG, "MenuItem:  " + name + " " + desc + " " + price);
//                        MenuItem item = new MenuItem(type, name, desc, price);
//                        //starters.add(item);
//                        mains.add(item);
//
//                    }
//                    MenuSection mainSection = new MenuSection(sectionTwoTitle, mains);
//                    menuSections.add(mainSection);
//                    Log.d(TAG, "Menu Section" + menuSections.toString());
//                }else{
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        });
//
//
//        String sectionThreeTitle = "Desserts";
//        db.collection("Menu").whereEqualTo("type", "Dessert").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    List<MenuItem> desserts = new ArrayList<>();
//                    for(QueryDocumentSnapshot document : task.getResult()){
//                        //Log.d(TAG, document.getId() + " => " + document.getData());
//                        Map<String, Object> data = document.getData();
//                        Object[] values = data.values().toArray();
//                        //Log.d(TAG, "Document snapshot data: " + values);
//                        String price = (String) values[0];
//                        //Log.d(TAG, "Price:  " + price);
//                        String name = (String) values[1];
//                        String type = (String) values[2];
//                        String desc = (String) values[3];
//                        //Log.d(TAG, "MenuItem:  " + name + " " + desc + " " + price);
//                        MenuItem item = new MenuItem(type, name, desc, price);
//                        //starters.add(item);
//                        desserts.add(item);
//
//                    }
//                    MenuSection dessertSection = new MenuSection(sectionThreeTitle, desserts);
//                    menuSections.add(dessertSection);
//                    Log.d(TAG, "Menu Section" + menuSections.toString());
//                }else{
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        });
//
//
//
//        String sectionFourTitle = "Drinks";
//        db.collection("Menu").whereEqualTo("type", "Drink").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    List<MenuItem> drinks = new ArrayList<>();
//                    for(QueryDocumentSnapshot document : task.getResult()){
//                        //Log.d(TAG, document.getId() + " => " + document.getData());
//                        Map<String, Object> data = document.getData();
//                        Object[] values = data.values().toArray();
//                        //Log.d(TAG, "Document snapshot data: " + values);
//                        String price = (String) values[0];
//                        //Log.d(TAG, "Price:  " + price);
//                        String name = (String) values[1];
//                        String type = (String) values[2];
//                        String desc = (String) values[3];
//                        //Log.d(TAG, "MenuItem:  " + name + " " + desc + " " + price);
//                        MenuItem item = new MenuItem(type, name, desc, price);
//                        //starters.add(item);
//                        drinks.add(item);
//
//                    }
//                    MenuSection drinkSection = new MenuSection(sectionFourTitle, drinks);
//                    menuSections.add(drinkSection);
//                    adapter = new menuRecyclerAdapter(menuSections);
//                    mRecyclerView.setAdapter(adapter);
//                    Log.d(TAG, "Menu Section" + menuSections.toString());
//                }else{
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        });
//
//    }

}