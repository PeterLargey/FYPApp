package com.example.fypapplication;

import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class WaitingListFragment extends Fragment {

    private RecyclerView waitingListRecycler;
    private FloatingActionButton addToWaitingList;
    private waitingListAdapter adapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View waitingListView = inflater.inflate(R.layout.fragment_waiting_list, container, false);
        db = FirebaseFirestore.getInstance();

        waitingListRecycler = waitingListView.findViewById(R.id.waitingListRecycler);
        waitingListRecycler.addItemDecoration(new DividerItemDecoration(waitingListView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler();

        addToWaitingList = waitingListView.findViewById(R.id.addToWaitingList);
        addToWaitingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(waitingListView.getContext(), AddToWaitingList.class);
                startActivity(i);
            }
        });

        return waitingListView;
    }

    private void setUpRecycler(){
        Query query = db.collection("WaitingList").orderBy("waitTime", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<WaitingList> options = new FirestoreRecyclerOptions.Builder<WaitingList>().setQuery(query, WaitingList.class).build();
        adapter = new waitingListAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        waitingListRecycler.setLayoutManager(staggeredGridLayoutManager);
        waitingListRecycler.setAdapter(adapter);
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