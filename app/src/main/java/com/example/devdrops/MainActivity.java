package com.example.devdrops;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.devdrops.fragments.AddPostFragment;
import com.example.devdrops.fragments.PostFragment;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    SmoothBottomBar smoothBottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        smoothBottomBar = findViewById(R.id.bottomNav);
        loadFragment(new PostFragment());

        smoothBottomBar.setOnItemSelectedListener((OnItemSelectedListener) i -> {

            if (i == 0) {
                loadFragment(new PostFragment());
            }
//            else if (i == 1) {
//
//            }
            else if (i == 2) {
                loadFragment(new AddPostFragment());
            }
//            else {
//                loadFragment(new Profile());
//            }

            return true;
        });

    }

    private void loadFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit();

    }

}
