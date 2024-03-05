package com.example.devdrops.news;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devdrops.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {

    //    #072247
    private RecyclerView newsRV;
    private RecyclerView categoryRV;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoriesRVModal> categoriesRVModalArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

//        frameLayout = findViewById(R.id.shimmer_container);
        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        articlesArrayList = new ArrayList<>();
        categoriesRVModalArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList, this);
        categoryRVAdapter = new CategoryRVAdapter(categoriesRVModalArrayList, this, this::OnCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategory();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();


    }

    private void getCategory() {

        categoriesRVModalArrayList.add(new CategoriesRVModal("All", "https://images.unsplash.com/photo-1457369804613-52c61a468e7d?auto=format&fit=crop&q=80&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&w=2070"));

        categoriesRVModalArrayList.add(new CategoriesRVModal("Technology", "https://images.unsplash.com/photo-1539683255143-73a6b838b106?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1885&q=80"));

        categoriesRVModalArrayList.add(new CategoriesRVModal("Android Development", "https://media.istockphoto.com/id/1369513916/photo/hand-with-atom-nucleus-and-electrons-symbol.jpg?s=1024x1024&w=is&k=20&c=7j0N8B5eQ27M2ik5sM1P676pDeeZ6hvdizYtYvmFc_w="));

        categoriesRVModalArrayList.add(new CategoriesRVModal("WebDev", "https://images.unsplash.com/photo-1552674605-db6ffd4facb5?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80"));

        categoriesRVModalArrayList.add(new CategoriesRVModal("machine learning", "https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80"));

        categoriesRVModalArrayList.add(new CategoriesRVModal("Database", "https://plus.unsplash.com/premium_photo-1676651178962-67cb1622b7ca?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80"));

        categoriesRVModalArrayList.add(new CategoriesRVModal("AI", "https://images.unsplash.com/photo-1586899028174-e7098604235b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2071&q=80"));

        categoriesRVModalArrayList.add(new CategoriesRVModal("DevOps", "https://images.unsplash.com/photo-1505751172876-fa1923c5c528?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80"));

        categoryRVAdapter.notifyDataSetChanged();


    }

//    https://newsapi.org/v2/top-headlines?country=in&category=" + category + "
    //   https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=9238eae1cd5e4d36a80a7400fc59f5f0

    private void getNews(String category) {
//https://newsapi.org/v2/everything?q=Android Development&apiKey=466da3ccf14d4e52b769df1687a074e1
        articlesArrayList.clear();
        String categoryUrl = "https://newsapi.org/v2/everything?q=" + category + "&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=9238eae1cd5e4d36a80a7400fc59f5f0";
        String url = "https://newsapi.org/v2/top-headlines?country=in&category=Technology&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=9238eae1cd5e4d36a80a7400fc59f5f0";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<NewsModal> call;

        if (category.equals("All")) {
            call = retrofitApi.getAllNews(url);
        } else {
            call = retrofitApi.getNewsByCategory(categoryUrl);
        }

        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {

                NewsModal newsModal = response.body();
                ArrayList<Articles> articles = newsModal.getArticles();
                for (int i = 0; i < articles.size(); i++) {
                    articlesArrayList.add
                            (new Articles(articles.get(i).getTitle(),
                                    articles.get(i).getDescription(),
                                    articles.get(i).getUrlToImage(),
                                    articles.get(i).getUrl(),
                                    articles.get(i).getContent()));
                }

                newsRVAdapter.notifyDataSetChanged();
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