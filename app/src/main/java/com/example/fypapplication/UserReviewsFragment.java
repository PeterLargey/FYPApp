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


public class UserReviewsFragment extends Fragment {

    private FirebaseFirestore db;
    private reviewAdapter adapter;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View reviewsView = inflater.inflate(R.layout.fragment_user_reviews, container,false);
        db = FirebaseFirestore.getInstance();
        mRecyclerView = reviewsView.findViewById(R.id.customerReviewsRecycler);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(reviewsView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler();
        return reviewsView;
    }

    private void setUpRecycler(){
        Query query = db.collection("Reviews").orderBy("title", Query.Direction.ASCENDING );
        FirestoreRecyclerOptions<Review> options = new FirestoreRecyclerOptions.Builder<Review>().setQuery(query, Review.class).build();
        adapter = new reviewAdapter(options);
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