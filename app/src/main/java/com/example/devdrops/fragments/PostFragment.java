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
    LinearLayout lnr_data_unavailable;
    PostAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the

    // Firebase Realtime Database
    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
        recyclerView = rootView.findViewById(R.id.dashboardRV);
        shimmerFrameLayout = rootView.findViewById(R.id.shimmerFrameLayout);

        mbase
                = FirebaseDatabase.getInstance().getReference().child("posts");
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
        // Connecting Adapter class with the Recycler view*/
        // Reverse the order of elements in the adapter
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
//check whether internet connection available or not


                    //stop shimmer layout animation
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);


            }
        }, 2000);
    }


}