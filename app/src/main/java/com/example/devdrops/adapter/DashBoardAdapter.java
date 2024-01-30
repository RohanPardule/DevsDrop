package com.example.devdrops.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.devdrops.R;
import com.example.devdrops.model.DashBoardModel;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.MyViewHolder> {

    Context context;
    ArrayList<DashBoardModel> list;

    public DashBoardAdapter(Context context, ArrayList<DashBoardModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_row_layout,parent,false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      DashBoardModel model=list.get(position);
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
//
//        holder.like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // ... existing code ...
//
//                // Update the like count in the current item without refreshing the entire list
//                model.setPostLike(model.getPostLike() + 1);
//                holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_2, 0, 0, 0);
//
//                // Notify the adapter about the change
//
//
//                // ... existing code ...
//            }
//        });

        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(model.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_2, 0, 0, 0);
                        }else {
                            holder.like.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(model.getPostId())
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("posts")
                                                            .child(model.getPostId())
                                                            .child("postLike")
                                                            .setValue(model.getPostLike() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_2, 0, 0, 0);

                                                                    updateItem(position, model);
//        later to be implemented in sending notification                                                            Notification notification = new Notification();
//                                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
//                                                                    notification.setNotificationAt(new Date().getTime());
//                                                                    notification.setPostID(model.getPostId());
//                                                                    notification.setPostedBy(model.getPostedBy());
//                                                                    notification.setType("like");
//
//                                                                    FirebaseDatabase.getInstance().getReference()
//                                                                            .child("notification")
//                                                                            .child(model.getPostedBy())
//                                                                            .push()
//                                                                            .setValue(notification);
                                                                }
                                                            });
                                                }
                                            });

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



//        holder.comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, CommentActivity.class);
//                intent.putExtra("postId", model.getPostId());
//                intent.putExtra("postedBy", model.getPostedBy());
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView post_image,menu;
        TextView username,description,like,comment,time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
           post_image=itemView.findViewById(R.id.dashBoard_postImage);
           username=itemView.findViewById(R.id.dashBoard_userName);
           description=itemView.findViewById(R.id.postDescription);
           time=itemView.findViewById(R.id.time);
           menu=itemView.findViewById(R.id.post_menu);

           like=itemView.findViewById(R.id.like);
           comment=itemView.findViewById(R.id.comment);
        }



        private void showToast(String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
    public void updateItem(int position, DashBoardModel updatedItem) {
        list.set(position, updatedItem);
        notifyItemChanged(position);
    }





}

