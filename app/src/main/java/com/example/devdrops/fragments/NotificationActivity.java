package com.example.devdrops.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.devdrops.R;
import com.example.devdrops.adapter.NotificationAdapter;
import com.example.devdrops.adapter.PostAdapter;
import com.example.devdrops.model.DashBoardModel;
import com.example.devdrops.model.Notification;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotificationAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.notificationRV);
        backBtn=findViewById(R.id.backbtn);

        backBtn.setOnClickListener(view -> {
            onBackPressed();
        });
        mbase = FirebaseDatabase.getInstance().getReference().
                child("notification").child(FirebaseAuth.getInstance().getUid());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        Query query = mbase.orderByChild("notificationAt").limitToLast(50);
        FirebaseRecyclerOptions<Notification> options
                = new FirebaseRecyclerOptions.Builder<Notification>()
                .setQuery(query, Notification.class)
                .build();


        adapter = new NotificationAdapter(options);

        recyclerView.setAdapter(adapter);
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