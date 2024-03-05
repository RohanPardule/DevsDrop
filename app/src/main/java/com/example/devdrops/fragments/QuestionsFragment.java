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
import com.example.devdrops.adapter.NotificationAdapter;
import com.example.devdrops.adapter.QueryAdapter;
import com.example.devdrops.databinding.FragmentQuestionsBinding;
import com.example.devdrops.model.Notification;
import com.example.devdrops.model.QuestionModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class QuestionsFragment extends Fragment {


FragmentQuestionsBinding binding;
    RecyclerView recyclerView;
   QueryAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase;

    public QuestionsFragment() {
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
        binding= FragmentQuestionsBinding.inflate(inflater, container, false);

        binding.addQuestionBtn.setOnClickListener(view -> {
            Intent i= new Intent(getContext(),AddQuestionActivity.class);
            getContext().startActivity(i);
        });

        mbase = FirebaseDatabase.getInstance().getReference().
                child("queries");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
       binding.recyclerView.setLayoutManager(layoutManager);

        Query query = mbase.orderByChild("postedAt").limitToLast(50);
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