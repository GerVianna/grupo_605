package com.example.services;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String URL = "http://so-unlam.net.ar/api/api/";
    private static RetrofitClient instanceRetrofit;
    private Retrofit retrofit;
    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build();

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if (instanceRetrofit == null) {
            instanceRetrofit = new RetrofitClient();
        }
        return instanceRetrofit;
    }

    public ServiceApi getService() {
        return retrofit.create(ServiceApi.class);
    }


}
