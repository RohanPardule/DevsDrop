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

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class PostAdapter extends FirebaseRecyclerAdapter<
        DashBoardModel, PostAdapter.DashBoardModelsViewholder> {

    public PostAdapter(
            @NonNull FirebaseRecyclerOptions<DashBoardModel> options)
    {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "DashBoardModel.xml") iwth data in
    // model class(here "DashBoardModel.class")
    @Override
    protected void
    onBindViewHolder(@NonNull DashBoardModelsViewholder holder,
                     int position, @NonNull DashBoardModel model)
    {

        FirebaseUtil.PostUsername(model.getPostedBy()).get().addOnCompleteListener(task -> {
            UserModel model1=task.getResult().toObject(UserModel.class);
            holder.username.setText(model1.getUsername().toString());
        });

        long timestamp = model.getPostedAt();

        // Convert the timestamp to Date
        Date date = new Date(timestamp);

        // Format the Date to a human-readable format
        SimpleDateFormat sdf = new  SimpleDateFormat("d MMM h:mma", Locale.ENGLISH);
        String formattedDate = sdf.format(date);
        holder.time.setText(formattedDate.toString());

        holder.like.setText(model.getPostLike()+"");
        holder.comment.setText(model.getCommentCount()+"");

        String description = model.getPostDescription();
        if (description.equals("")){
            holder.description.setVisibility(View.GONE);
        }else {
            holder.description.setText(model.getPostDescription());
            holder.description.setVisibility(View.VISIBLE);
        }

        Picasso.get()
                .load(model.getPostImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.post_image);


//        FirebaseDatabase.getInstance().getReference()
//                .child("posts")
//                .child(getRef(position).getKey())
//                .child("likes")
//                .child(FirebaseAuth.getInstance().getUid())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()){
//                            holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_2, 0, 0, 0);
//                        }else {
//                            holder.like.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    FirebaseDatabase.getInstance().getReference()
//                                            .child("posts")
//                                            .child(getRef(position).getKey())
//                                            .child("likes")
//                                            .child(FirebaseAuth.getInstance().getUid())
//                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    FirebaseDatabase.getInstance().getReference()
//                                                            .child("posts")
//                                                            .child(getRef(position).getKey())
//                                                            .child("postLike")
//                                                            .setValue(model.getPostLike() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                @Override
//                                                                public void onSuccess(Void aVoid) {
//                                                                    holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_2, 0, 0, 0);
//
//
////        later to be implemented in sending notification
////                                                                    Notification notification = new Notification();
////                                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
////                                                                    notification.setNotificationAt(new Date().getTime());
////                                                                    notification.setPostID(getRef(position).getKey());
////                                                                    notification.setPostedBy(model.getPostedBy());
////                                                                    notification.setType("like");
////
////                                                                    FirebaseDatabase.getInstance().getReference()
////                                                                            .child("notification")
////                                                                            .child(model.getPostedBy())
////                                                                            .push()
////                                                                            .setValue(notification);
//                                                                }
//                                                            });
//                                                }
//                                            });
//
//                                }
//                            });
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

        String postId = getRef(position).getKey(); // Retrieve the key of the current post

        // Check if the current user has liked the post
        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(postId)
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // User has liked the post
                            holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_2, 0, 0, 0);
                        } else {
                            // User has not liked the post
                            holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the current user has liked the post
                FirebaseDatabase.getInstance().getReference()
                        .child("posts")
                        .child(postId)
                        .child("likes")
                        .child(FirebaseAuth.getInstance().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // User has already liked, implement unlike logic here if needed
                                    // ...
                                } else {
                                    // User has not liked, implement like logic here
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(postId)
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Update local data (increment like count and change drawable)
                                                    model.setPostLike(model.getPostLike() + 1);
                                                    holder.like.setText(String.valueOf(model.getPostLike()));
                                                    holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_2, 0, 0, 0);

                                                    // Update postLike count in the database
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("posts")
                                                            .child(postId)
                                                            .child("postLike")
                                                            .setValue(model.getPostLike());

                                                    // ... other logic (e.g., sending notification)

                                                    Notification notification = new Notification();
                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                    notification.setNotificationAt(new Date().getTime());
                                                    notification.setPostID(postId);
                                                    notification.setPostedBy(model.getPostedBy());
                                                    notification.setType("like");

                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("notification")
                                                            .child(model.getPostedBy())
                                                            .push()
                                                            .setValue(notification);
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle error
                            }
                        });
            }
        });






        // Add firstname from model class (here
        // "DashBoardModel.class")to appropriate view in Card
//        // view (here "DashBoardModel.xml")
//        holder.firstname.setText(model.getFirstname());
//
//        // Add lastname from model class (here
//        // "DashBoardModel.class")to appropriate view in Card
//        // view (here "DashBoardModel.xml")
//        holder.lastname.setText(model.getLastname());
//
//        // Add age from model class (here
//        // "DashBoardModel.class")to appropriate view in Card
//        // view (here "DashBoardModel.xml")
//        holder.age.setText(model.getAge());
    }

    // Function to tell the class about the Card view (here
    // "DashBoardModel.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public DashBoardModelsViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_row_layout, parent, false);
        return new PostAdapter.DashBoardModelsViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "DashBoardModel.xml")
    class DashBoardModelsViewholder
            extends RecyclerView.ViewHolder {

        ImageView post_image,menu;
        TextView username,description,like,comment,time;


        public DashBoardModelsViewholder(@NonNull View itemView)
        {
            super(itemView);

            post_image=itemView.findViewById(R.id.dashBoard_postImage);
            username=itemView.findViewById(R.id.dashBoard_userName);
            description=itemView.findViewById(R.id.postDescription);
            time=itemView.findViewById(R.id.time);
            menu=itemView.findViewById(R.id.post_menu);

            like=itemView.findViewById(R.id.like);
            comment=itemView.findViewById(R.id.comment);
        }
    }
}

