package com.example.devdrops.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.devdrops.MainActivity;
import com.example.devdrops.R;
import com.example.devdrops.login.SignInActivity;
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
              Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
              startActivity(intent);
          } else {
            Intent intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
              startActivity(intent);
          }

          overridePendingTransition(R.anim.enter, R.anim.exit);
        }
      }, 2500);







    }


}