package com.example.devdrops;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.devdrops.fragments.AddPostFragment;
import com.example.devdrops.fragments.DoubtFragment;
import com.example.devdrops.fragments.NotificationActivity;
import com.example.devdrops.fragments.PostFragment;
import com.example.devdrops.fragments.ProfileFragment;
import com.example.devdrops.fragments.QuestionsFragment;
import com.example.devdrops.fragments.SearchUserActivity;
import com.example.devdrops.news.NewsActivity;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    SmoothBottomBar smoothBottomBar;
    ImageButton newsIcon,searchBtn;
    ImageButton notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsIcon = findViewById(R.id.newsAppIcon);
        searchBtn = findViewById(R.id.search);
        notification = findViewById(R.id.notification);

        notification.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        });
        newsIcon.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, NewsActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        });
        searchBtn.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SearchUserActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        });

        smoothBottomBar = findViewById(R.id.bottomNav);
        loadFragment(new PostFragment());

        smoothBottomBar.setOnItemSelectedListener((OnItemSelectedListener) i -> {

            if (i == 0) {
                loadFragment(new PostFragment());
            } else if (i == 1) {
                loadFragment(new DoubtFragment());
            } else if (i == 2) {
                loadFragment(new AddPostFragment());
            }
//            else if (i == 3) {
//              startActivity(new Intent(MainActivity.this, NewsActivity.class));
//            }
          else {
                loadFragment(new ProfileFragment());
            }

            return true;
        });

    }

    private void loadFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit();

    }

}
