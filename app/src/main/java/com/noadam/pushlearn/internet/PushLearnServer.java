package com.noadam.pushlearn.internet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PushLearnServer {
    @Headers("Content-Type: application/json")
    @POST("sign_in")
    Call<String> signIn(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("sign_up")
    Call<String> signUp(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("busy_email")
    Call<String> busyEmail(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("busy_email")
    Call<String> busyNickName(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("get_nickname_by_hash")
    Call<String> getNickNameByHash(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("update_packs")
    Call<String> updateMyComPacks(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("return_pack_from_id")
    Call<String> getComPackByID(@Body String body);
}
