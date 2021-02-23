package com.example.myapplication.network;/*
 Created by arenas on 23/02/21.
*/


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton

{
    private static RetrofitSingleton instance = null;
    private static String BASE_URL="http://192.168.1.10:8090/heatmap/";

    // Keep your services here, build them in buildRetrofit method later
    private ApiInterface api;

    public static RetrofitSingleton getInstance() {
        if (instance == null) {
            instance = new RetrofitSingleton();
        }

        return instance;
    }

    // Build retrofit once when creating a single instance
    private RetrofitSingleton() {
        // Implement a method to build your retrofit
        buildRetrofit();
    }

    private void buildRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        Retrofit retrofit=new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        // Build your services once
        this.api = retrofit.create(ApiInterface.class);
    }

    public ApiInterface getUserService() {
        return this.api;
    }

}