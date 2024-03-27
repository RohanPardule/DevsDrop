package com.example.devdrops.news;




import com.example.devdrops.credentials.Credentials;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {


    private static final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(Credentials.BASE_URL).
            addConverterFactory(GsonConverterFactory.create());


    private static final Retrofit retrofit = retrofitBuilder.build();

    private static final RetrofitApi Api = retrofit.create(RetrofitApi.class);


    public static RetrofitApi getService() {
        return Api;
    }
    // Replace with your API base URL

//    private static Retrofit retrofit = null;
//    private static RetrofitInstance instance;
//
//    public RetrofitInstance() {
//
//    }
//
//    public static RetrofitInstance getInstance() {
//        if (instance == null) {
//            instance = new RetrofitInstance();
//        }
//        return instance;
//    }
////    int cacheSize = 10 * 1024 * 1024; // 10 MB
//
//
//    public static ApiService getService(){
////        int cacheSize = 10 * 1024 * 1024; // 10 MB
////        Cache cache = new Cache(context.getCacheDir(), cacheSize);
////
////        // Create an OkHttpClient with cache
////        OkHttpClient okHttpClient = new OkHttpClient.Builder()
////                .cache(cache)
////                .build();
//        if (retrofit == null){
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(Credentials.BASE_URL)
////                    .client(okHttpClient)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//
//        return retrofit.create(ApiService.class);
//    }
}
