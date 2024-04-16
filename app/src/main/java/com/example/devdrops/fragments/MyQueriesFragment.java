package com.example.devdrops.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devdrops.R;
import com.example.devdrops.adapter.QueryAdapter;
import com.example.devdrops.databinding.FragmentMyQueriesBinding;

import com.example.devdrops.model.QuestionModel;
import com.example.devdrops.util.FirebaseUtil;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MyQueriesFragment extends Fragment {

    FragmentMyQueriesBinding binding;
    RecyclerView recyclerView;
    QueryAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase;

    public MyQueriesFragment() {
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
        binding= FragmentMyQueriesBinding.inflate(inflater, container, false);

        binding.addQuestionBtn.setOnClickListener(view -> {
            Intent i= new Intent(getContext(),AddQuestionActivity.class);
            getContext().startActivity(i);
        });



        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);

        Query query =  FirebaseDatabase.getInstance().getReference().
                child("queries")
                .orderByChild("postedby")
                .equalTo(FirebaseUtil.currentUserId());
        FirebaseRecyclerOptions<QuestionModel> options
                = new FirebaseRecyclerOptions.Builder<QuestionModel>()
                .setQuery(query, QuestionModel.class)
                .build();


        adapter = new QueryAdapter(options,getContext());

        binding.recyclerView.setAdapter(adapter);
        return binding.getRoot();
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
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }
}