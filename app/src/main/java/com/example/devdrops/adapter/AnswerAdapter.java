package com.example.devdrops.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devdrops.R;
import com.example.devdrops.databinding.CommentSampleBinding;
import com.example.devdrops.fragments.OtherUserProfileActivity;
import com.example.devdrops.model.AnswerModel;
import com.example.devdrops.model.Comment;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.viewHolder>{

    Context context ;
    ArrayList<AnswerModel> list;

    public AnswerAdapter(Context context, ArrayList<AnswerModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_sample, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        AnswerModel answerModel = list.get(position);

        String time = TimeAgo.using(answerModel.getPostedAt());
        holder.binding.time.setText(time);


        FirebaseUtil.PostUsername(answerModel.getPostedby()).get().addOnCompleteListener(task -> {
            UserModel user = task.getResult().toObject(UserModel.class);

            holder.binding.comment.setText(Html.fromHtml( "<b>" + user.getUsername() + "</b>"+ "  " + answerModel.getAnswer()));
            if (user.getProfile()!=null)
            {
                Picasso.get()
                        .load(user.getProfile())
                        .placeholder(R.drawable.placeholder)
                        .into(holder.binding.profileImage);

            }
        });

        holder.binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OtherUserProfileActivity.class);
                intent.putExtra("otherUser", answerModel.getPostedby());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
//
//        FirebaseDatabase.getInstance().getReference()
//                .child("Users")
//                .child(comment.getCommentedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User user =snapshot.getValue(User.class);
//                Picasso.get()
//                        .load(user.getProfile())
//                        .placeholder(R.drawable.placeholder)
//                        .into(holder.binding.profileImage);
//                holder.binding.comment.setText(Html.fromHtml( "<b>" + user.getName() + "</b>"+ "  " + comment.getCommentBody()));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        CommentSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CommentSampleBinding.bind(itemView);
        }
    }
}
