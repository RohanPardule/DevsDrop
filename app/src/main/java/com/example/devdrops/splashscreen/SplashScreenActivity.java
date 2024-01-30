package com.example.devdrops.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devdrops.MainActivity;
import com.example.devdrops.R;
import com.example.devdrops.login.SignInActivity;
import com.example.devdrops.login.SignInSignUpActivity;
import com.example.devdrops.util.FirebaseUtil;

public class SplashScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


      new Handler().postDelayed(new Runnable()
      {
        @Override
        public void run()
        {
          if (FirebaseUtil.isLoggedIn()) {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
          } else {
            Intent intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
            startActivity(intent);
          }

          overridePendingTransition(R.anim.enter, R.anim.exit);
        }
      }, 2500);







    }


}