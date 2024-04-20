package com.example.devdrops.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.devdrops.R;
import com.example.devdrops.adapter.ProfilePostAdapter;
import com.example.devdrops.databinding.ActivityOtherUserProfileBinding;
import com.example.devdrops.model.DashBoardModel;
import com.example.devdrops.model.Report;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OtherUserProfileActivity extends AppCompatActivity {
    String otherUserId;
    ActivityOtherUserProfileBinding binding;
    DocumentReference otherUser,currentUser;
    ProfilePostAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtherUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i=getIntent();
        otherUserId=i.getStringExtra("otherUser");
        binding.backbtnOtherProfile.setOnClickListener(view -> {
            onBackPressed();
        });

     otherUser = FirebaseUtil.getOtherUserDetails(otherUserId);
     currentUser=FirebaseUtil.currentUserDetails();
        otherUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                UserModel userModel = task.getResult().toObject(UserModel.class);
                binding.otherProfileUsername.setText(userModel.getUsername().toString());
                binding.otherProfileProffesion.setText(userModel.getProfession().toString());
                Picasso.get()
                        .load(userModel.getProfile())
                        .placeholder(R.drawable.placeholder)
                        .into(binding.otherProfileImageView);
                binding.followcount.setText(String.valueOf(userModel.getFollowersCount()));
                binding.followingcount.setText(String.valueOf(userModel.getFollowingCount()));
                binding.noOfPosts.setText(String.valueOf(userModel.getNumberOfPosts()));

            }
        });
        binding.followBtn.setOnClickListener(view -> {

            if (binding.followBtn.getText().toString().equals("Follow +")) {
                UserModel userModel = new UserModel();
                userModel.setUserId(FirebaseUtil.currentUserId());
                UserModel currentUsermodel=new UserModel();
                currentUsermodel.setUserId(FirebaseUtil.currentUserId());

                otherUser.collection("followers").document(FirebaseUtil.currentUserId()).set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        otherUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                UserModel model=task.getResult().toObject(UserModel.class);
                              int  count=model.getFollowersCount();
                                binding.followBtn.setText("Following");
                                otherUser.update("followersCount",count+1);
                            }
                        });

                    }
                });
currentUser.collection("following").document(otherUserId).set(currentUsermodel).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        currentUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                UserModel model=task.getResult().toObject(UserModel.class);

                    int count = model.getFollowingCount();

                currentUser.update("followingCount",count+1);
            }
        });

    }
});
            }
            else {
                otherUser.collection("followers").document(FirebaseUtil.currentUserId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        otherUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                UserModel model=task.getResult().toObject(UserModel.class);
                                int  count=model.getFollowersCount();
                                binding.followBtn.setText("Follow +");
                                otherUser.update("followersCount",count-1);
                            }
                        });

                    }
                });
                currentUser.collection("following").document(otherUserId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        currentUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                UserModel model=task.getResult().toObject(UserModel.class);

                                int count = model.getFollowingCount();

                                currentUser.update("followingCount",count-1);
                            }
                        });

                    }
                });

            }
        });

        otherUser.collection("followers").document(FirebaseUtil.currentUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Current user ID exists in the collection
                        binding.followBtn.setText("Following");
                    } else {
                        // Current user ID doesn't exist in the collection
                        // You can perform any additional actions here if needed
                        binding.followBtn.setText("Follow +");
                    }
                } else {
                    // Handle errors
                    Log.d("Tag", "Error getting documents: ", task.getException());
                }
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(OtherUserProfileActivity.this, 3, GridLayoutManager.VERTICAL, false);

        binding.profileRv.setLayoutManager(layoutManager);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("posts").orderByChild("postedBy").equalTo(otherUserId).limitToLast(50);

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
                ProfilePostAdapter adapter = new ProfilePostAdapter(dataList,OtherUserProfileActivity.this);
                binding.profileRv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });


        binding.reportBtn.setOnClickListener(v -> {
            // Show a dialog to report the post
            showReportDialog(otherUserId);
        });

    }
    private void showReportDialog(String postId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OtherUserProfileActivity.this);
        builder.setTitle("Report User");
        String[] userReportReasons = {
                "Inappropriate behavior",
                "Spamming",
                "Harassment or bullying",
                "Impersonation",
                "Posting sensitive or harmful content",
                "Violating community guidelines",
                "Sharing inappropriate content",
                "Posting misleading information",
                "Other" 
        };

        builder.setItems(userReportReasons, (dialog, which) -> {
            // Save the report to the database
            saveReportToDatabase(postId, userReportReasons[which]);
        });
        builder.show();
    }

    private void saveReportToDatabase(String postId, String reason) {
        // Implement the code to save the report to your database here
        // For example, if you're using Firebase Realtime Database:
        DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference("reported_users");
        String reportId = reportsRef.push().getKey();
        Report report = new Report(postId, reason);
        reportsRef.child(reportId).setValue(report);
        Toast.makeText(OtherUserProfileActivity.this, "User reported successfully", Toast.LENGTH_SHORT).show();
    }

}