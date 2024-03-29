package com.example.devdrops.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


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

import com.bumptech.glide.Glide;
import com.example.devdrops.R;
import com.example.devdrops.adapter.PostAdapter;
import com.example.devdrops.adapter.ProfilePostAdapter;

import com.example.devdrops.model.DashBoardModel;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Date;

public class ProfileFragment extends Fragment {


    RecyclerView recyclerView;
    ProfilePostAdapter adapter; // Create Object of the Adapter class

    TextView profile_username,followcount,following,nopostTextView;
    TextView profile_Proffesion;
    ImageView profile_imageView;

    Uri uri;
    FirebaseStorage storage;
    ProgressDialog dialog;
    int FLAG=0;



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance();
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
      nopostTextView=rootView.findViewById(R.id.no_post_yet);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading");
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        profile_imageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            pickImageActivityResultLauncher.launch(intent);
        });





        DocumentReference currentUser = FirebaseUtil.currentUserDetails();
        currentUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                UserModel userModel = task.getResult().toObject(UserModel.class);
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

            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("posts").orderByChild("postedBy").equalTo(FirebaseUtil.currentUserId());

        FirebaseRecyclerOptions<DashBoardModel> options
                = new FirebaseRecyclerOptions.Builder<DashBoardModel>()
                .setQuery(query, DashBoardModel.class)
                .build();

        // the Adapter class itself
        adapter = new ProfilePostAdapter(options);


        recyclerView.setAdapter(adapter);

//        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onChanged() {
//                super.onChanged();
//                if (adapter.getItemCount() == 0) {
//
//                    nopostTextView.setVisibility(View.VISIBLE);
//                } else {
//                    nopostTextView.setVisibility(View.GONE);
//                }
//            }
//        });
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
    ActivityResultLauncher<Intent> pickImageActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        uri = data.getData();
                        profile_imageView.setImageURI(uri);
                        FLAG=1;



                    }
                }
            });

}
//update_profile.setOnClickListener(view -> {
//        dialog.show();
//        if (FLAG==1)
//        {
//final StorageReference reference = storage.getReference().child("posts")
//        .child(FirebaseAuth.getInstance().getUid())
//        .child(new Date().getTime() + "");
//
//        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//@Override
//public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//@Override
//public void onSuccess(Uri uri) {
//
//
//        FirebaseUtil.currentUserDetails().update("profile",uri.toString());
//        if (profile_Proffesion.getText().toString() != "") {
//        FirebaseUtil.currentUserDetails().update("profession", profile_Proffesion.getText().toString()
//        );
//        }
//        dialog.dismiss();
//
//        }
//        }).addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull Exception e) {
//        dialog.dismiss(); // Dismiss dialog on failure
//        Log.d("Error", e.toString());
//        Toast.makeText(getContext(), "Failed to upload post. Please try again.", Toast.LENGTH_SHORT).show();
//        }
//        });
//        }
//        });
//        }
//        else
//        {
//        if (profile_Proffesion.getText().toString() != "") {
//        FirebaseUtil.currentUserDetails().update("profession", profile_Proffesion.getText().toString()
//        );
//        dialog.dismiss();
//        }
//
//        dialog.dismiss();
//
//        }
//
//
//        });