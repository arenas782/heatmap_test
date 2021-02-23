package com.example.myapplication.network;/*
 Created by arenas on 23/02/21.
*/

import com.example.myapplication.models.heatMap;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiInterface {
    @GET
    Call<List<heatMap>> getData(@Url String url);
}
