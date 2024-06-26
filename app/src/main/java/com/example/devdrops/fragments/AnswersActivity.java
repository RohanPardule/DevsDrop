package com.example.devdrops.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devdrops.R;
import com.example.devdrops.adapter.AnswerAdapter;
import com.example.devdrops.adapter.CommentAdapter;
import com.example.devdrops.model.AnswerModel;
import com.example.devdrops.model.Comment;
import com.example.devdrops.model.Notification;
import com.example.devdrops.model.QuestionModel;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnswersActivity extends AppCompatActivity {
String questionId,questionPostedBy,questionStr,time;
    String answercount_intent;
TextView name,questionTv,timeTv,answercount_Tv;
EditText answerEt;
RecyclerView answerRv;
ImageView postAnswerBtn;
CircleImageView profileImage;
    FirebaseDatabase database;
    ImageView backBtn;
    ArrayList<AnswerModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        database=FirebaseDatabase.getInstance();
        name=findViewById(R.id.question_username);
        answercount_Tv=findViewById(R.id.answercount);
        questionTv=findViewById(R.id.question);
        timeTv=findViewById(R.id.time);
        answerEt=findViewById(R.id.answerET);
        postAnswerBtn=findViewById(R.id.answerPostBtn);
        profileImage=findViewById(R.id.questions_profileImage);
        answerRv=findViewById(R.id.answersRV);
        backBtn=findViewById(R.id.backbtn_answers);



      getDataFromIntent();

      setUpquestionDetails(questionPostedBy,questionStr,time,answercount_intent);

      postAnswerBtn.setOnClickListener(view -> {
          if (!answerEt.getText().toString().equals("")) {

              AnswerModel answer = new AnswerModel();
              answer.setAnswer(answerEt.getText().toString());
              answer.setPostedAt(new Date().getTime());
              answer.setPostedby(FirebaseAuth.getInstance().getUid());
              answer.setLike(0);

              database.getReference()
                      .child("queries")
                      .child(questionId)
                      .child("answers")
                      .push()
                      .setValue(answer).addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                              database.getReference()
                                      .child("queries")
                                      .child(questionId)
                                      .child("answercount").addListenerForSingleValueEvent(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                                              int answercount = 0;
                                              if (snapshot.exists()) {
                                                  answercount = snapshot.getValue(Integer.class);
                                              }

                                              int finalAnswercount = answercount;
                                              int updatecount=answercount+1;
                                              database.getReference()
                                                      .child("queries")
                                                      .child(questionId)
                                                      .child("answercount")
                                                      .setValue(answercount + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                          @Override
                                                          public void onSuccess(Void aVoid) {
                                                              answerEt.setText("");
                                                              Toast.makeText(AnswersActivity.this, "Commented Succesfully", Toast.LENGTH_SHORT).show();
                                                              answercount_Tv.setText(updatecount+" Answers");

                                                              Notification notification = new Notification();
                                                              notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                              notification.setNotificationAt(new Date().getTime());
                                                              notification.setPostID("Na");
                                                              notification.setPostedBy(questionPostedBy);
                                                              notification.setQuestionId(questionId);
                                                              notification.setType("answer");

                                                              FirebaseDatabase.getInstance().getReference()
                                                                      .child("notification")
                                                                      .child(questionPostedBy)
                                                                      .push()
                                                                      .setValue(notification);
                                                          }
                                                      });
                                          }

                                          @Override
                                          public void onCancelled(@NonNull DatabaseError error) {

                                          }
                                      });
                          }
                      });

              try {
                  InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                  imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
              } catch (Exception e) {

              }
          }
          else {
              Toast.makeText(this, "Your Answer is Empty", Toast.LENGTH_SHORT).show();
          }
          //
      });


        AnswerAdapter adapter = new AnswerAdapter(this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        answerRv.setLayoutManager(layoutManager);
       answerRv.setAdapter(adapter);



        database.getReference()
                .child("queries")
                .child(questionId)
                .child("answers").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            AnswerModel answerModel = dataSnapshot.getValue(AnswerModel.class);
                            list.add(answerModel);
                        }
//                        binding.scrollView.fullScroll(binding.scrollView.FOCUS_DOWN);

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        backBtn.setOnClickListener(view -> {
            onBackPressed();
        });

    }
    void getDataFromIntent(){
        Intent intent=getIntent();
        questionId=  intent.getStringExtra("questionId");

//        database.getReference().child("queries").child(questionId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                QuestionModel model=snapshot.getValue(QuestionModel.class);
//                questionPostedBy=model.getPostedby();
//                questionStr=model.getQuestion();
//                time= String.valueOf(model.getPostedAt());
//                answercount_intent= String.valueOf(model.getAnswercount());
//                Log.d("Questionmodel",model.toString());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        questionPostedBy=intent.getStringExtra("questionPostedBy");
        questionStr=intent.getStringExtra("question");
        time=intent.getStringExtra("time");
        answercount_intent = intent.getStringExtra("answercount");
    }
    void setUpquestionDetails(String postedBy,String query,String time,String answercount)
    {
        FirebaseFirestore.getInstance().collection("users")
                .document(postedBy).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        UserModel user = task.getResult().toObject(UserModel.class);

                        Picasso.get()
                                .load(user.getProfile())
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.placeholder)
                                .into(profileImage);
                        name.setText(user.getUsername());
                        questionTv.setText(query);
                        timeTv.setText(time);
                        answercount_Tv.setText(answercount +" Answers");



                    }
                });


    }
}