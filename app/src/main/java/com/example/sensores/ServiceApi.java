package com.example.sensores;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServiceApi {
    @FormUrlEncoded
    @POST("loginUser")
    Call<ResponseBody> loginUser(
            @Field("email") String email,
            @Field("password") String password);
}
