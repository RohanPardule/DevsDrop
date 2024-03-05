package com.example.devdrops.fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.devdrops.R;

public class AnswersActivity extends AppCompatActivity {
String questionId,questionPostedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        Intent intent=getIntent();
      questionId=  intent.getStringExtra("questionId");
      questionPostedBy=intent.getStringExtra("questionPostedBy");



    }
}