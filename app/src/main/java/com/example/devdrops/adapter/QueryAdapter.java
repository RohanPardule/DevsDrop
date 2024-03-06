package com.example.devdrops.adapter;


import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devdrops.R;

import com.example.devdrops.databinding.QuestionsRowLayoutBinding;
import com.example.devdrops.fragments.AnswersActivity;
import com.example.devdrops.fragments.CommentActivity;
import com.example.devdrops.model.QuestionModel;
import com.example.devdrops.model.QuestionModel;
import com.example.devdrops.model.UserModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class QueryAdapter extends FirebaseRecyclerAdapter<
        QuestionModel, QueryAdapter.QuestionModelViewholder> {
    Context context;

    public QueryAdapter(
            @NonNull FirebaseRecyclerOptions<QuestionModel> options, Context context) {
        super(options);
       this.context=context;
    }

    @Override
    protected void
    onBindViewHolder(@NonNull QuestionModelViewholder holder,
                     int position, @NonNull QuestionModel questionModel) {




        String time = TimeAgo.using(questionModel.getPostedAt());
        holder.time.setText(time);

        FirebaseFirestore.getInstance().collection("users")
                .document(questionModel.getPostedby()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        UserModel user = task.getResult().toObject(UserModel.class);

                            Picasso.get()
                                    .load(user.getProfile())
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(holder.binding.questionsProfileImage);

                            holder.binding.questionUsername.setText(user.getUsername());

                            holder.binding.question.setText(questionModel.getQuestion());

                    }
                });

        holder.binding.layout.setOnClickListener(view -> {
            Intent intent = new Intent(context, AnswersActivity.class);

            intent.putExtra("questionId", questionModel.getQuestionID());
            intent.putExtra("questionPostedBy", questionModel.getPostedby());
            intent.putExtra("question",questionModel.getQuestion());
            intent.putExtra("time",questionModel.getPostedAt());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });


    }


    @NonNull
    @Override
    public QuestionModelViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.questions_row_layout, parent, false);
        return new QueryAdapter.QuestionModelViewholder(view);
    }


    class QuestionModelViewholder
            extends RecyclerView.ViewHolder {
        TextView question, time,name;
        CircleImageView profileImage;
        ConstraintLayout layout;
        QuestionsRowLayoutBinding binding;

        public QuestionModelViewholder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            name= itemView.findViewById(R.id.question_username);
            time = itemView.findViewById(R.id.question_time);
            layout=itemView.findViewById(R.id.layout);
            profileImage=itemView.findViewById(R.id.questions_profileImage);
            binding = QuestionsRowLayoutBinding.bind(itemView);

        }
    }
}

