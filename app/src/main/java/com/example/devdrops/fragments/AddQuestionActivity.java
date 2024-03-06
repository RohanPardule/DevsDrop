package com.example.devdrops.fragments;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devdrops.MainActivity;
import com.example.devdrops.R;
import com.example.devdrops.databinding.ActivityAddQuestionBinding;
import com.example.devdrops.model.DashBoardModel;
import com.example.devdrops.model.QuestionModel;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddQuestionActivity extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog dialog;

    TextView name ,profession;
    EditText myquery;
    AppCompatButton postBtn;
    CircleImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

       name=findViewById(R.id.addquestion_name);
        profession=findViewById(R.id.addquestion_profession);
        myquery=findViewById(R.id.myQuestion);
        postBtn=findViewById(R.id.addquestion_postBtn);
        profileImage=findViewById(R.id.addquestion_profileImage);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Query Uploading");
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseUtil.currentUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        UserModel user = task.getResult().toObject(UserModel.class);

                        Picasso.get()
                                .load(user.getProfile())
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.placeholder)
                                .into(profileImage);
                        name.setText(user.getUsername());
                        profession.setText(user.getProfession());


                    }
                });






       myquery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String description = myquery.getText().toString();
                if (!description.isEmpty()) {
                    postBtn.setBackgroundDrawable(ContextCompat.getDrawable(AddQuestionActivity.this, R.drawable.follow_btn_bg));
                   postBtn.setTextColor(AddQuestionActivity.this.getResources().getColor(R.color.white));
                    postBtn.setEnabled(true);
                } else {
                  postBtn.setBackgroundDrawable(ContextCompat.getDrawable(AddQuestionActivity.this, R.drawable.follow_active_btn));
                   postBtn.setTextColor(AddQuestionActivity.this.getResources().getColor(R.color.gray));
                 postBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


       postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();
                String query = myquery.getText().toString();

                dialog.show();


                String postId = database.getReference().child("query").push().getKey();
                QuestionModel question = new QuestionModel();
                question.setQuestionID(postId);

                question.setPostedby(FirebaseAuth.getInstance().getUid());
                question.setQuestion(query);
                question.setPostedAt(new Date().getTime());
                question.setAnswercount(0);


                database.getReference().child("queries")
                        .child(postId)
                        .setValue(question).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();

                               myquery.setText("");

                                Toast.makeText(AddQuestionActivity.this, "Query Posted Successfully", Toast.LENGTH_SHORT).show();

                                onBackPressed();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss(); // Dismiss dialog on failure
                                Log.d("Error1", e.toString());
                                Toast.makeText(AddQuestionActivity.this, "Failed to upload post. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


    }


    public void hideKeyboard() {
        // Check if no view has focus:
        View view = AddQuestionActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) AddQuestionActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }




}
