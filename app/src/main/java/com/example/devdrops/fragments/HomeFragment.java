package com.example.devdrops.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.devdrops.R;
import com.example.devdrops.adapter.DashBoardAdapter;
import com.example.devdrops.model.DashBoardModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

RecyclerView recyclerView;

    ArrayList<DashBoardModel> postList;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;

    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.dashboardRV);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
//        recyclerView.showShimmerAdapter();
        setUpRecyclerView();
        return view;
    }
    void setUpRecyclerView() {

        postList = new ArrayList<>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);  // Keep this line if you want items to be added from the bottom
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);




        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                DashBoardAdapter postAdapter = new DashBoardAdapter(getContext(), postList);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DashBoardModel post = dataSnapshot.getValue(DashBoardModel.class);
                    post.setPostId(dataSnapshot.getKey());
                    postList.add(post);

                }
                postAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);
                        recyclerView.smoothScrollToPosition(0);
                    }
                });
                recyclerView.setAdapter(postAdapter);
//              recyclerView.hideShimmerAdapter();
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}