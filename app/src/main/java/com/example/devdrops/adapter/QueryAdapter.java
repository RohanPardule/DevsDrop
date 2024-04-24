package com.example.devdrops.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devdrops.R;

import com.example.devdrops.databinding.QuestionsRowLayoutBinding;
import com.example.devdrops.fragments.AnswersActivity;
import com.example.devdrops.fragments.CommentActivity;
import com.example.devdrops.fragments.OtherUserProfileActivity;
import com.example.devdrops.model.QuestionModel;
import com.example.devdrops.model.QuestionModel;
import com.example.devdrops.model.Report;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        holder.binding.answercount.setText(questionModel.getAnswercount() + " Answers");

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
                            holder.binding.profession.setText(user.getProfession());

                    }
                });

        holder.binding.layout.setOnClickListener(view -> {
            Intent intent = new Intent(context, AnswersActivity.class);
            intent.putExtra("questionId", questionModel.getQuestionID());
            intent.putExtra("questionPostedBy", questionModel.getPostedby());
            intent.putExtra("question",questionModel.getQuestion());
            intent.putExtra("time",time);
            intent.putExtra("answercount",String.valueOf(questionModel.getAnswercount()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OtherUserProfileActivity.class);
                intent.putExtra("otherUser", questionModel.getPostedby());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.binding.postMenu.setOnClickListener(v -> {
            // Show a dialog to report the post
            showReportDialog(questionModel.getQuestionID());
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
       RelativeLayout layout;
       TextView answercount;
        QuestionsRowLayoutBinding binding;

        public QuestionModelViewholder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            name= itemView.findViewById(R.id.question_username);
            time = itemView.findViewById(R.id.question_time);
            layout=itemView.findViewById(R.id.layout);
            answercount=itemView.findViewById(R.id.answercount);
            profileImage=itemView.findViewById(R.id.questions_profileImage);
            binding = QuestionsRowLayoutBinding.bind(itemView);

        }
    }
    private void showReportDialog(String postId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Report Post");
        String[] reasons = { "Inappropriate behavior",
                "Spamming",
                "Harassment or bullying",
                "Impersonation",
                "Posting sensitive or harmful content",
                "Violating community guidelines",
                "Sharing inappropriate content",
                "Posting misleading information",
                "Other" };
        builder.setItems(reasons, (dialog, which) -> {
            // Save the report to the database
            saveReportToDatabase(postId, reasons[which]);
        });
        builder.show();
    }

    private void saveReportToDatabase(String postId, String reason) {
        // Implement the code to save the report to your database here
        // For example, if you're using Firebase Realtime Database:
        DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference("reported_queries");
        String reportId = reportsRef.push().getKey();
        Report report = new Report(postId, reason);
        reportsRef.child(reportId).setValue(report);
        Toast.makeText(context, "Post reported successfully", Toast.LENGTH_SHORT).show();
    }
}

