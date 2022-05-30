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
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


public class WaitingListFragment extends Fragment {

    private RecyclerView waitingListRecycler;
    private FloatingActionButton addToWaitingList;
    private waitingListAdapter adapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private FirebaseFirestore db;
    private TextView emptyMessage;
    private String role;
    private final String TAG = "TAG";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View waitingListView = inflater.inflate(R.layout.fragment_waiting_list, container, false);
        db = FirebaseFirestore.getInstance();

        if(getArguments() != null){
            role = getArguments().getString("role");
        }

        waitingListRecycler = waitingListView.findViewById(R.id.waitingListRecycler);
        waitingListRecycler.addItemDecoration(new DividerItemDecoration(waitingListView.getContext(), DividerItemDecoration.VERTICAL));
        emptyMessage = waitingListView.findViewById(R.id.waitingListEmptyMessage);
        setUpRecycler();

        addToWaitingList = waitingListView.findViewById(R.id.addToWaitingList);
        addToWaitingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(waitingListView.getContext(), AddToWaitingList.class);
                i.putExtra("role", role);
                startActivity(i);
            }
        });

        return waitingListView;
    }

    private void setUpRecycler(){
        Query query = db.collection("WaitingList").orderBy("waitTime", Query.Direction.ASCENDING);
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