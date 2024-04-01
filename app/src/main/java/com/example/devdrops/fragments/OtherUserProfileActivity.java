package com.example.devdrops.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.devdrops.R;
import com.example.devdrops.databinding.ActivityOtherUserProfileBinding;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class OtherUserProfileActivity extends AppCompatActivity {
    String otherUserId;
    ActivityOtherUserProfileBinding binding;
    DocumentReference otherUser,currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtherUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i=getIntent();
        otherUserId=i.getStringExtra("otherUser");

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



    }
}