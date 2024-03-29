package com.example.devdrops.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.devdrops.R;
import com.example.devdrops.databinding.ActivityEditProfileBinding;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    Uri uri;
    FirebaseStorage storage;
    ProgressDialog dialog;
    int FLAG=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Updating your profile");
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        DocumentReference currentUser = FirebaseUtil.currentUserDetails();
        currentUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                UserModel userModel = task.getResult().toObject(UserModel.class);
                binding.profileUsername.setText(userModel.getUsername().toString());
                binding.profileProffesion.setText(userModel.getProfession().toString());
                Picasso.get()
                        .load(userModel.getProfile())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(binding.profileImageView);

            }
        });

        binding.changeProfile.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            pickImageActivityResultLauncher.launch(intent);
        });


       binding.updateProfileBtn.setOnClickListener(view -> {
        showYesNoDialog(EditProfileActivity.this,"Save Changes","Are you sure to save changes?");
        });




    }
    private void showYesNoDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                // Handle positive (Yes) button click
                dialogInterface.dismiss(); // Dismiss the dialog
                dialog.show();
                if (FLAG==1)
                {
                    final StorageReference reference = storage.getReference().child("posts")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child(new Date().getTime() + "");

                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {


                                    FirebaseUtil.currentUserDetails().update("profile",uri.toString());
                                    if (binding.profileProffesion.getText().toString() != "") {
                                        FirebaseUtil.currentUserDetails().update("profession", binding.profileProffesion.getText().toString()
                                        );
                                    }
                                    if (binding.profileUsername.getText().toString() != "") {
                                        FirebaseUtil.currentUserDetails().update("username", binding.profileUsername.getText().toString()
                                        );
                                    }
                                    dialog.dismiss();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss(); // Dismiss dialog on failure
                                    Log.d("Error", e.toString());
                                    Toast.makeText(EditProfileActivity.this, "Failed to upload post. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
                else {
                    if (binding.profileProffesion.getText().toString() != "") {
                        FirebaseUtil.currentUserDetails().update("profession", binding.profileProffesion.getText().toString()
                        );
                    }
                    if (binding.profileUsername.getText().toString() != "") {
                        FirebaseUtil.currentUserDetails().update("username", binding.profileUsername.getText().toString()
                        );
                    }

                    dialog.dismiss();

                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                // Handle negative (No) button click
                dialogInterface.dismiss(); // Dismiss the dialog
                // Add your action here if user clicks No
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    ActivityResultLauncher<Intent> pickImageActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        uri = data.getData();
                       binding.profileImageView.setImageURI(uri);
                        FLAG=1;



                    }
                }
            });

}