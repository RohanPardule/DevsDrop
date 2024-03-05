package com.example.devdrops.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devdrops.R;
import com.example.devdrops.model.DashBoardModel;
import com.example.devdrops.model.Notification;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ProfilePostAdapter extends FirebaseRecyclerAdapter<
        DashBoardModel, ProfilePostAdapter.DashBoardModelsViewholder> {

    public ProfilePostAdapter(
            @NonNull FirebaseRecyclerOptions<DashBoardModel> options) {
        super(options);
    }


    @Override
    protected void
    onBindViewHolder(@NonNull DashBoardModelsViewholder holder,
                     int position, @NonNull DashBoardModel model) {


        Picasso.get()
                .load(model.getPostImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.post_image);


    }


    @NonNull
    @Override
    public DashBoardModelsViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_post_row, parent, false);
        return new ProfilePostAdapter.DashBoardModelsViewholder(view);
    }

    class DashBoardModelsViewholder
            extends RecyclerView.ViewHolder {

        ImageView post_image;


        public DashBoardModelsViewholder(@NonNull View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.profile_postImage);

        }
    }
}

