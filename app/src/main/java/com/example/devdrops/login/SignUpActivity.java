package com.example.devdrops.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.devdrops.MainActivity;
import com.example.devdrops.R;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, username;
    Button signUpBtn;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.email_address);
        passwordEditText = findViewById(R.id.create_password);
        username = findViewById(R.id.create_username);
        signUpBtn = findViewById(R.id.signUp_button);
        progressBar = findViewById(R.id.progress_bar);
        setInProgress(false);
        signUpBtn.setOnClickListener(v -> {
            signUp();
        });
    }

//    public void signUp() {
//        setInProgress(true);
//        String email = emailEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//        String name = username.getText().toString();
//
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        setInProgress(false);
//                        if (task.isSuccessful()) {
//                            // Sign up success
//                            Toast.makeText(SignUpActivity.this, "Sign up successful.", Toast.LENGTH_SHORT).show();
//
//                            String id = FirebaseAuth.getInstance().getUid();
//                            UserModel userModel = new UserModel(email, name, id, "Na","Na", 0,0,false,0);
//                            FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Log.d("firebase", "entered here successfully");
//                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(intent);
//
//                                        finish();
//                                    }
//                                }
//                            });
//                        } else {
//                            // If sign up fails, display a message to the user.
//                            if (task.getException() != null) {
//                                // Check if the user already exists
//                                if (task.getException().getMessage().contains("email address is already in use")) {
//                                    Toast.makeText(SignUpActivity.this, "Email is already registered.", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    // Other errors
//                                    Toast.makeText(SignUpActivity.this, "Sign up failed. " + task.getException().getMessage(),
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//                    }
//                });
//    }


    public void signUp() {
        setInProgress(true);
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String name = username.getText().toString();

        if (!isValidEmail(email)) {
            emailEditText.setError("Invalid email address");
            setInProgress(false);
            return;
        }

        if (!isValidPassword(password)) {
            passwordEditText.setError("Invalid password (must be at least 6 characters long)");
            setInProgress(false);
            return;
        }

        if (!isValidUsername(name)) {
            username.setError("Invalid username (must be at least 3 characters long)");
            setInProgress(false);
            return;
        }

        // Check if the email is already in use
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            if (result != null && result.getSignInMethods() != null && !result.getSignInMethods().isEmpty()) {
                                // Email is already registered
                                setInProgress(false);
                                Toast.makeText(SignUpActivity.this, "Email is already registered.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Email is not registered, proceed with sign-up
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                setInProgress(false);
                                                if (task.isSuccessful()) {
                                                    // Sign up success
                                                    Toast.makeText(SignUpActivity.this, "Sign up successful.", Toast.LENGTH_SHORT).show();

                                                    String id = FirebaseAuth.getInstance().getUid();
                                                    UserModel userModel = new UserModel(email, name, id, "Na","Na", 0,0,false,0);
                                                    FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("firebase", "entered here successfully");
                                                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    // If sign up fails, display a message to the user.
                                                    if (task.getException() != null) {
                                                        // Other errors
                                                        Toast.makeText(SignUpActivity.this, "Sign up failed. " + task.getException().getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Error occurred while checking email availability
                            setInProgress(false);
                            Toast.makeText(SignUpActivity.this, "Error occurred while checking email availability.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            signUpBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            signUpBtn.setVisibility(View.VISIBLE);
        }
    }
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6 && !password.isEmpty();
    }

    private boolean isValidUsername(String username) {
        return username.length() >= 3 && !username.isEmpty();
    }
}