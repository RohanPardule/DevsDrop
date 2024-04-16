package com.example.devdrops.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.devdrops.R;
import com.example.devdrops.databinding.ProfilePostRowBinding;
import com.example.devdrops.fragments.CommentActivity;
import com.example.devdrops.model.DashBoardModel;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.DashBoardModelsViewHolder> {

    private List<DashBoardModel> dataList;
    Context context;

    public ProfilePostAdapter(List<DashBoardModel> dataList,Context context) {
        this.dataList = dataList;
        this.context=context;
    }

    @NonNull
    @Override
    public DashBoardModelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_post_row, parent, false);
        return new DashBoardModelsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashBoardModelsViewHolder holder, int position) {
        DashBoardModel model = dataList.get(position);
        Picasso.get().load(model.getPostImage()).placeholder(R.drawable.placeholder).into(holder.post_image);

        holder.binding.profilePostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CommentActivity.class);

                intent.putExtra("postId", model.getPostId());
                intent.putExtra("postedBy", model.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }});
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class DashBoardModelsViewHolder extends RecyclerView.ViewHolder {
        ImageView post_image;
        ProfilePostRowBinding binding;

        public DashBoardModelsViewHolder(@NonNull View itemView) {
            super(itemView);
            post_image = itemView.findViewById(R.id.profile_postImage);
            binding= ProfilePostRowBinding.bind(itemView);
        }
    }
}
