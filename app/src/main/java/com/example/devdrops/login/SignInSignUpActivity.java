package com.example.devdrops.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.devdrops.R;

public class SignInSignUpActivity extends AppCompatActivity {
    Button signIn;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_sign_up);

        signIn = findViewById(R.id.signIn_btn);
        signUp = findViewById(R.id.signUp_btn);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIn = new Intent(SignInSignUpActivity.this, SignInActivity.class);
                startActivity(signIn);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(SignInSignUpActivity.this, SignUpActivity.class);
                startActivity(signUp);
            }
        });
    }
}