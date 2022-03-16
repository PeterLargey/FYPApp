package com.example.fypapplication;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.Arrays;
import java.util.Map;


public class ServerOrderFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FirebaseFirestore db;
    private String staffMember, role;
    private orderAdapter adapter;
    private final String TAG = "TAG";
    private StaggeredGridLayoutManager staggeredGridLayoutManager;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View serverOrderView = inflater.inflate(R.layout.fragment_server_order, container, false);
        db = FirebaseFirestore.getInstance();
        if(getArguments() != null){
            staffMember = getArguments().getString("staffMember");
            role = getArguments().getString("role");

        }
        mRecyclerView = serverOrderView.findViewById(R.id.myOrderRecycler);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(serverOrderView.getContext(), DividerItemDecoration.VERTICAL));

        Log.d(TAG, "Server Order Fragment name: " + staffMember);
        setUpRecycler(role);
        return serverOrderView;
    }

    private void setUpRecycler(String role){
        Query query = db.collection("Orders").whereEqualTo("staffMember", staffMember).orderBy("tableNo", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>().setQuery(query, Order.class).build();
        adapter = new orderAdapter(options, role);
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