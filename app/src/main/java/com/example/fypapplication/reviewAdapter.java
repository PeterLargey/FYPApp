package com.example.fypapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class reviewAdapter extends FirestoreRecyclerAdapter<Review, reviewAdapter.ReviewViewHolder> {

    public reviewAdapter(@NonNull FirestoreRecyclerOptions<Review> options){super(options);}

    @Override
    protected void onBindViewHolder(@NonNull ReviewViewHolder holder, int position, @NonNull Review model) {
        holder.title.setText(model.getTitle());
        holder.rating.setRating(Float.parseFloat(model.getRating()));
        holder.desc.setText(model.getDesc());
        holder.username.setText(model.getUser());
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_reviews_layout, parent, false);
        return new ReviewViewHolder(v);
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private RatingBar rating;
        private TextView desc;
        private TextView username;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.reviewTitle);
            rating = itemView.findViewById(R.id.reviewRatings);
            desc = itemView.findViewById(R.id.reviewDesc);
            username = itemView.findViewById(R.id.reviewUsername);
        }
    }


}
