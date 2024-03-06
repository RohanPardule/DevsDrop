package com.example.devdrops.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devdrops.R;
import com.example.devdrops.fragments.CommentActivity;
import com.example.devdrops.fragments.OtherUserProfileActivity;
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

public class PostAdapter extends FirebaseRecyclerAdapter<
        DashBoardModel, PostAdapter.DashBoardModelsViewholder> {
    Context context;

    public PostAdapter(
            @NonNull FirebaseRecyclerOptions<DashBoardModel> options,Context context) {
        super(options);
     this.context=context;
    }


    @Override
    protected void
    onBindViewHolder(@NonNull DashBoardModelsViewholder holder,
                     int position, @NonNull DashBoardModel model) {

        FirebaseUtil.PostUsername(model.getPostedBy()).get().addOnCompleteListener(task -> {
            UserModel model1 = task.getResult().toObject(UserModel.class);
            holder.username.setText(model1.getUsername().toString());
if (model1.getProfile()!=null)
{
            Picasso.get()
                    .load(model1.getProfile())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.profileImage);
}
        });

        long timestamp = model.getPostedAt();

        // Convert the timestamp to Date
        Date date = new Date(timestamp);

        // Format the Date to a human-readable format
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM h:mma", Locale.ENGLISH);
        String formattedDate = sdf.format(date);
        holder.time.setText(formattedDate.toString());

        holder.like.setText(model.getPostLike() + "");
//        holder.comment.setText(model.getCommentCount()+"");

        String description = model.getPostDescription();
        if (description.equals("")) {
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setText(model.getPostDescription());
            holder.description.setVisibility(View.VISIBLE);
        }

        Picasso.get()
                .load(model.getPostImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.post_image);



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

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(postId)
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .removeValue()
                                            //can use delete too if it wont work
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Update local data (increment like count and change drawable)
                                                    model.setPostLike(model.getPostLike() - 1);
                                                    holder.like.setText(String.valueOf(model.getPostLike()));
                                                    holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);

                                                    // Update postLike count in the database
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("posts")
                                                            .child(postId)
                                                            .child("postLike")
                                                            .setValue(model.getPostLike());


                                                }
                                            });

                                } else {
                                    // User has not liked, implemented like logic here
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

                                                    //  ( sending notification)

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
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);

                    intent.putExtra("postId", model.getPostId());
                    intent.putExtra("postedBy", model.getPostedBy());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

            }
        });

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OtherUserProfileActivity.class);
                intent.putExtra("otherUser", model.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });




    }


    @NonNull
    @Override
    public DashBoardModelsViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_row_layout, parent, false);
        return new PostAdapter.DashBoardModelsViewholder(view);
    }


    class DashBoardModelsViewholder
            extends RecyclerView.ViewHolder {

        ImageView post_image, menu, comment,profileImage;
        TextView username, description, like, time;


        public DashBoardModelsViewholder(@NonNull View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.dashBoard_postImage);
            username = itemView.findViewById(R.id.dashBoard_userName);
            description = itemView.findViewById(R.id.postDescription);
            time = itemView.findViewById(R.id.time);
            menu = itemView.findViewById(R.id.post_menu);
            profileImage=itemView.findViewById(R.id.profileImage);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
        }
    }
}

