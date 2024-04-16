package com.example.devdrops.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devdrops.R;
import com.example.devdrops.databinding.AllUsersRowLayoutBinding;
import com.example.devdrops.fragments.OtherUserProfileActivity;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class GetAllUsersAdapter extends FirestoreRecyclerAdapter<UserModel,GetAllUsersAdapter.UserModelViewHolder> {

    Context context;

    public GetAllUsersAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options , Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {

        holder.binding.username.setText(model.getUsername());
        holder.binding.email.setText(model.getEmail());
        Picasso.get()
                .load(model.getProfile())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.userprofile);
        if ((model.getUserId().equals(FirebaseUtil.currentUserId())))
        {
            holder.binding.username
                    .setText(model.getUsername()+" (ME)");
        }


        holder.itemView.setOnClickListener(v -> {
            //chat activity
            Intent intent=new Intent(context, OtherUserProfileActivity.class);
//            AndroidUtil.passUsermodelIntent(intent,model);
            intent.putExtra("otherUser",model.getUserId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        });


    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_users_row_layout,parent,false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{
        AllUsersRowLayoutBinding binding;


        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AllUsersRowLayoutBinding.bind(itemView);
        }
    }
}

