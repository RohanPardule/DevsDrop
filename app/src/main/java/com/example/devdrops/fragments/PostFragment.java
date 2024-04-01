package com.example.devdrops.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.devdrops.R;
import com.example.devdrops.adapter.PostAdapter;
import com.example.devdrops.model.DashBoardModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class PostFragment extends Fragment {

    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;
    PostAdapter adapter;
    DatabaseReference mbase;


    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);


        recyclerView = rootView.findViewById(R.id.dashboardRV);
        shimmerFrameLayout = rootView.findViewById(R.id.shimmerFrameLayout);

        mbase = FirebaseDatabase.getInstance().getReference().child("posts");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

// Set the LayoutManager to the RecyclerView
        recyclerView.setLayoutManager(layoutManager);

// query in the database to fetch appropriate data
        Query query = mbase.orderByChild("postedAt").limitToLast(50);
        FirebaseRecyclerOptions<DashBoardModel> options
                = new FirebaseRecyclerOptions.Builder<DashBoardModel>()
                .setQuery(query, DashBoardModel.class)
                .build();
        adapter = new PostAdapter(options,getContext());

startFetching();


        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void startFetching() {
        //start Shimmer layout animation
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                //stop shimmer layout animation
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
            }
        }, 1000);
    }


}