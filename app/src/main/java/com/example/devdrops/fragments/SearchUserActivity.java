package com.example.devdrops.fragments;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devdrops.R;
import com.example.devdrops.adapter.GetAllUsersAdapter;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class SearchUserActivity extends AppCompatActivity {
    EditText searchInput;
    ImageButton searchButton,backButton;
    RecyclerView recyclerView;
    GetAllUsersAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        searchInput=findViewById(R.id.search_user_input);
        searchButton=findViewById(R.id.search_user_btn);
        backButton=findViewById(R.id.back_btn);
        recyclerView=findViewById(R.id.search_user_recycler_view);
        //editext should be focused and open keyboard as soon as we press search button
        searchInput.requestFocus();


        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        searchButton.setOnClickListener(v -> {
            String searchTerm=searchInput.getText().toString();
            if(searchTerm.isEmpty()|| searchTerm.length()<3)
            {
                searchInput.setError("Invalid Username");
                return;
            }
            setUpSearchRecyclerView(searchTerm);
        });
    }
    void setUpSearchRecyclerView(String searchTerm){


        Query query = FirebaseUtil.allUserCollectionReference()
                .orderBy("username") // Order the results by username
                .startAt(searchTerm) // Username starts with the search term
                .endAt(searchTerm  + "\uf8ff"); // Username ends with the search term + end unicode character

// Create FirestoreRecyclerOptions with the modified query
//        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
//                .setQuery(query, UserModel.class)
//                .build();
//        Query query= FirebaseUtil.allUserCollectionReference().whereGreaterThanOrEqualTo("username",searchTerm);

        FirestoreRecyclerOptions<UserModel> options=new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class).build();

        adapter=new GetAllUsersAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null)
            adapter.startListening();
    }
}