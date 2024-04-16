package com.example.devdrops.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.devdrops.R;
import com.example.devdrops.adapter.PostAdapter;
import com.example.devdrops.adapter.ProfilePostAdapter;

import com.example.devdrops.interfaces.UserModelCallback;
import com.example.devdrops.model.DashBoardModel;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ProfileFragment extends Fragment {


    RecyclerView recyclerView;
    ProfilePostAdapter adapter; // Create Object of the Adapter class

    TextView profile_username,followcount,following,noOfpost;
    TextView profile_Proffesion,edit_profile_btn;
    ImageView profile_imageView;
    SwipeRefreshLayout swipeRefreshLayout;

    Uri uri;

    ProgressDialog dialog;
    int FLAG=0;



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        recyclerView = rootView.findViewById(R.id.profileRv);
        followcount=rootView.findViewById(R.id.followcount);
        following=rootView.findViewById(R.id.followingcount);
        profile_username = rootView.findViewById(R.id.profile_username);
        profile_Proffesion = rootView.findViewById(R.id.profile_Proffesion);
        profile_imageView=rootView.findViewById(R.id.profile_imageView);
        noOfpost=rootView.findViewById(R.id.no_of_posts);
        edit_profile_btn=rootView.findViewById(R.id.editProfile);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading");
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


loadUserData();


        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("posts").orderByChild("postedBy").equalTo(FirebaseUtil.currentUserId()).limitToLast(50);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DashBoardModel> dataList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DashBoardModel model = dataSnapshot.getValue(DashBoardModel.class);
                    dataList.add(model);
                }
                // Reverse the data list
                Collections.reverse(dataList);

                // Pass the reversed data to the adapter
                ProfilePostAdapter adapter = new ProfilePostAdapter(dataList,getContext());
             recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        // the Adapter class itself
//        adapter = new ProfilePostAdapter(options);
//        recyclerView.setAdapter(adapter);

        edit_profile_btn.setOnClickListener(view -> {
            Intent i=new Intent(getActivity(), EditProfileActivity.class);
            getContext().startActivity(i);
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Perform data refreshing operation
                // Example: Call a method to fetch new data from a data source
                swipeRefreshLayout.setRefreshing(true);
             loadUserData();


                // Simulate data fetching for 2 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Stop SwipeRefreshLayout progress bar
                        swipeRefreshLayout.setRefreshing(false);

                        // Update RecyclerView with new data
                        // Here you would typically notify your adapter that the data set has changed
                    }
                }, 2000);
            }

        });


        return rootView;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }


    public void loadUserData() {
        FirebaseUtil.getCurrentUserModel(new UserModelCallback() {
            @Override
            public void onUserModelCallback(UserModel userModel) {
                if (userModel != null) {
                    // Set user data to UI components
                    profile_username.setText(userModel.getUsername().toString());
                    profile_Proffesion.setText(userModel.getProfession().toString());
                    Picasso.get()
                            .load(userModel.getProfile())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(profile_imageView);

                    followcount.setText(String.valueOf(userModel.getFollowersCount()));
                    try {
                        following.setText(String.valueOf(userModel.getFollowingCount()));
                    }
                    catch (Exception e){
                        following.setText(0);
                    }
                    try {
                        noOfpost.setText(String.valueOf(userModel.getNumberOfPosts()));
                    }
                    catch (Exception e){
                        noOfpost.setText(0);
                    }
                } else {
                    // Handle user data not found
                    // Show default or placeholder data
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }
}


//    ActivityResultLauncher<Intent> pickImageActivityResultLauncher =
//            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Intent data = result.getData();
//
//                        uri = data.getData();
//                        profile_imageView.setImageURI(uri);
//                        FLAG=1;
//
//
//
//                    }
//                }
//            });
//
//
