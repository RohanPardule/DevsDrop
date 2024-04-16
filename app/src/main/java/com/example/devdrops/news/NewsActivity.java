package com.example.devdrops.news;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devdrops.R;
import com.example.devdrops.model.UserModel;
import com.example.devdrops.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {

    //    #072247
    private RecyclerView newsRV;
    private RecyclerView categoryRV;
    private List<Articles> articlesArrayList;
    private ArrayList<CategoriesRVModal> categoriesRVModalArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;
    RetrofitInstance retrofitInstance;
    ImageView backbtn;
    TextView name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

//        frameLayout = findViewById(R.id.shimmer_container);
        name=findViewById(R.id.greeting);
        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        backbtn=findViewById(R.id.backbtn);
        articlesArrayList = new ArrayList<>();
        retrofitInstance=new RetrofitInstance();
        categoriesRVModalArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList, this);
        categoryRVAdapter = new CategoryRVAdapter(categoriesRVModalArrayList, this, this::OnCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategory();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();

        backbtn.setOnClickListener(view -> {
            onBackPressed();
        });

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                UserModel model=task.getResult().toObject(UserModel.class);

                String inputString =  model.getUsername().toString(); // Your string with two words separated by a space

                // Split the string into an array of words based on the space delimiter
                String[] words = inputString.split(" ");

                // Retrieve the first word from the array
                String firstWord = words[0];
                name.setText("Hi, "+firstWord+"!");
            }
        });

    }

    private void getCategory() {

        categoriesRVModalArrayList.add(new CategoriesRVModal("All", R.drawable.placeholder));

        categoriesRVModalArrayList.add(new CategoriesRVModal("Technology", R.drawable.placeholder));

        categoriesRVModalArrayList.add(new CategoriesRVModal("Android Development", R.drawable.placeholder));

        categoriesRVModalArrayList.add(new CategoriesRVModal("WebDev", R.drawable.placeholder));

        categoriesRVModalArrayList.add(new CategoriesRVModal("machine learning", R.drawable.placeholder));

        categoriesRVModalArrayList.add(new CategoriesRVModal("Database", R.drawable.placeholder));

        categoriesRVModalArrayList.add(new CategoriesRVModal("AI",R.drawable.placeholder));

        categoriesRVModalArrayList.add(new CategoriesRVModal("DevOps", R.drawable.placeholder));

        categoryRVAdapter.notifyDataSetChanged();


    }

//    https://newsapi.org/v2/top-headlines?country=in&category=" + category + "
    //   https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=9238eae1cd5e4d36a80a7400fc59f5f0

    private void getNews(String category) {
//https://newsapi.org/v2/everything?q=Android Development&apiKey=466da3ccf14d4e52b769df1687a074e1

        String categoryUrl = "https://newsapi.org/v2/everything?q=" + category + "&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=9238eae1cd5e4d36a80a7400fc59f5f0";
        String url = "https://newsapi.org/v2/top-headlines?country=in&category=Technology&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=9238eae1cd5e4d36a80a7400fc59f5f0";

        Call<NewsModal> call;

        if (category.equals("All")) {
            call = retrofitInstance.getService().getAllNews(url);
        } else {
            call = retrofitInstance.getService().getNewsByCategory(categoryUrl);
        }

        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {

                NewsModal newsModal = response.body();
                Log.d("NewsModel",newsModal.getArticles().toString());
                if (newsModal != null && newsModal.getArticles() != null) {
                    articlesArrayList.clear();
                    articlesArrayList.addAll(newsModal.getArticles());
                    newsRVAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(NewsActivity.this, "No articles found.", Toast.LENGTH_SHORT).show();
                }
//


            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) {

                Toast.makeText(NewsActivity.this, "Fail To Get News.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void OnCategoryClick(int position) {

        String category = categoriesRVModalArrayList.get(position).getCategory();
        getNews(category);

    }
}