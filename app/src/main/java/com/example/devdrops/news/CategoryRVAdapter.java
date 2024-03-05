package com.example.devdrops.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devdrops.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.ViewHolder> {

    private ArrayList<CategoriesRVModal> categoriesRVModal;
    private Context context;

    private CategoryClickInterface categoryClickInterface;

    public CategoryRVAdapter(ArrayList<CategoriesRVModal> categoriesRVModal, Context context, CategoryClickInterface categoryClickInterface) {
        this.categoriesRVModal = categoriesRVModal;
        this.context = context;
        this.categoryClickInterface = categoryClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CategoriesRVModal categoriesRVModal1 = categoriesRVModal.get(position);
        holder.categoryTV.setText(categoriesRVModal1.getCategory());
        Picasso.get().load(categoriesRVModal1.getCategoryImageUrl()).into(holder.categoryIV);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClickInterface.OnCategoryClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoriesRVModal.size();
    }

    public interface CategoryClickInterface {
        void OnCategoryClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryTV;
        private ImageView categoryIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryTV = itemView.findViewById(R.id.idTVCategories);
            categoryIV = itemView.findViewById(R.id.idIVCategories);
            ;
        }
    }
}
