package com.example.devdrops.util;

import android.content.Context;
import android.widget.Toast;


public class AndroidUtil {

    public static  void showToast(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

//    public static  void passUsermodelIntent(Intent intent, UserModel model)
//    {
//        intent.putExtra("username",model.getUsername());
//        intent.putExtra("phone",model.getPhone());
//        intent.putExtra("userId",model.getUserId());
//    }
//
//    public static  UserModel getUserModelFromIntent(Intent intent)
//    {
//        UserModel model=new UserModel();
//        model.setUsername(intent.getStringExtra("username"));
//        model.setPhone(intent.getStringExtra("phone"));
//        model.setUserId(intent.getStringExtra("userId"));
//        return model;
//    }
//
//
//
////using dogModel for breed list
//    public static  void passDogModelIntent(Intent intent, DogModel model)
//    {
//
//        intent.putExtra("name",model.getName());
//        intent.putExtra("type",model.getBreedGroup());
//        intent.putExtra("lifespan",model.getLifeSpan());
//        intent.putExtra("temperament",model.getTemperament());
//        intent.putExtra("description",model.getDescription());
//        intent.putExtra("imageId",model.getreference_image_id());
//    }
//
//    public static DogModel getDogModelFromIntent(Intent intent)
//    {
//        DogModel model=new DogModel();
//        model.setName(intent.getStringExtra("name"));
//        model.setBreedGroup(intent.getStringExtra("type"));
//        model.setLifeSpan(intent.getStringExtra("lifespan"));
//        model.setTemperament(intent.getStringExtra("temperament"));
//        model.setreference_image_id(intent.getStringExtra("imageId"));
//        model.setDescription(intent.getStringExtra("description"));
//        return model;
//    }
//
//
//
////using PostDogModel for adoption post
//    public static  void passPostDogModelIntent(Intent intent, PostDogModel model)
//    {
//
//        intent.putExtra("BreedName",model.getDogBreed());
//        intent.putExtra("username",model.getUsername());
//        intent.putExtra("address",model.getAddress());
//        intent.putExtra("phone",model.getPhone());
//        intent.putExtra("description",model.getDescription());
//        intent.putExtra("imageUrl",model.getImageUrl());
//        intent.putExtra("userId",model.getUserId());
//    }
//
//    public static PostDogModel getPostDogModelIntent(Intent intent)
//    {
//       PostDogModel model=new PostDogModel();
//        model.setUsername(intent.getStringExtra("username"));
//        model.setDogBreed(intent.getStringExtra("BreedName"));
//        model.setAddress(intent.getStringExtra("address"));
//        model.setPhone(intent.getStringExtra("phone"));
//        model.setImageUrl(intent.getStringExtra("imageUrl"));
//        model.setDescription(intent.getStringExtra("description"));
//        model.setUserId(intent.getStringExtra("userId"));
//        return model;
//    }
}
