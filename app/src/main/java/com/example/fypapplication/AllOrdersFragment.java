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
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;


public class AllOrdersFragment extends Fragment {


    private final String TAG = "TAG";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userId;
    private String role, staffMember;
    private RecyclerView mRecyclerView;
    private allOrdersAdapter adapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private TextView emptyMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View allOrdersView = inflater.inflate(R.layout.fragment_all_orders, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getUid();
        if(getArguments() != null){
            role = getArguments().getString("role");
            staffMember = getArguments().getString("staffMember");
        }

        Log.d(TAG, "Staff Member " + staffMember);
        Log.d(TAG, "Role " + role);
        emptyMessage = allOrdersView.findViewById(R.id.allOrdersEmptyMessage);

        mRecyclerView = allOrdersView.findViewById(R.id.allOrdersRecycler);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(allOrdersView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler(role, staffMember);


        return allOrdersView;
    }

    private void setUpRecycler(String role, String staffMember){
        Query query = db.collection("Orders").orderBy("tableNo", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty()){
                        emptyMessage.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>().setQuery(query, Order.class).build();
        adapter = new allOrdersAdapter(options, role, staffMember);
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