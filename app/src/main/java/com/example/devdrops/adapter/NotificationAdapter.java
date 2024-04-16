package com.example.devdrops.adapter;


import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devdrops.R;
import com.example.devdrops.databinding.NotificationRowLayoutBinding;
import com.example.devdrops.fragments.AnswersActivity;
import com.example.devdrops.fragments.CommentActivity;
import com.example.devdrops.model.DashBoardModel;
import com.example.devdrops.model.Notification;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class NotificationAdapter extends FirebaseRecyclerAdapter<
        Notification, NotificationAdapter.NotificationViewholder> {
    Context context;
    public NotificationAdapter(
            @NonNull FirebaseRecyclerOptions<Notification> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void
    onBindViewHolder(@NonNull NotificationViewholder holder,
                     int position, @NonNull Notification notification) {


        String type = notification.getType();

        String time = TimeAgo.using(notification.getNotificationAt());
        holder.time.setText(time);

        holder.binding.openNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent;
                if (notification.getPostID().equals("Na")){
                    intent = new Intent(context, AnswersActivity.class);
                    intent.putExtra("questionId", notification.getQuestionId());

                }
                else {
                    intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("postId", notification.getPostID());
                    intent.putExtra("postedBy", notification.getPostedBy());
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }});

        FirebaseFirestore.getInstance().collection("users")
                .document(notification.getNotificationBy()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        UserModel user = task.getResult().toObject(UserModel.class);

                            Picasso.get()
                                    .load(user.getProfile())
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(holder.binding.notificationProfile);


                        if (type.equals("like")) {
                            holder.notification.setText(Html.fromHtml("<b>" + user.getUsername() + "</b>" + " liked your post"));
                        } else if (type.equals("comment")) {
                            holder.notification.setText(Html.fromHtml("<b>" + user.getUsername() + "</b>" + " Commented on your post"));
                        }
                        else if (type.equals("answer")) {
                            holder.notification.setText(Html.fromHtml("<b>" + user.getUsername() + "</b>" + " Answered on your query"));
                        } else {
                            holder.notification.setText(Html.fromHtml("<b>" + user.getUsername() + "</b>" + " start following you."));
                        }
                    }
                });


    }


    @NonNull
    @Override
    public NotificationViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_row_layout, parent, false);
        return new NotificationAdapter.NotificationViewholder(view);
    }


    class NotificationViewholder
            extends RecyclerView.ViewHolder {
        TextView notification, time;
        NotificationRowLayoutBinding binding;

        public NotificationViewholder(@NonNull View itemView) {
            super(itemView);
            notification = itemView.findViewById(R.id.notificationTV);
            time = itemView.findViewById(R.id.timeTV);
            binding = NotificationRowLayoutBinding.bind(itemView);

        }
    }
}

