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
    @POST("get_nickname_by_id")
    Call<String> getNickNameByID(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("return_pack_from_id")
    Call<String> getComPackByID(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("get_cards_by_packID")
    Call<String> getCardsOfComPackByPackID(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("get_number_of_packs_by_nickname")
    Call<String> getNumberOfComPacksByNickName(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("get_rating_by_nickname")
    Call<String> getRatingByNickName(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("get_language_id_by_nickname")
    Call<String> getLanguageIDByNickName(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("get_packs_by_nickname")
    Call<String> getPacksByNickName(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("join_pack")
    Call<String> starPackByHash(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("if_user_starred_pack")
    Call<String> ifUserStaredPackByHashAndPackID(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("if_user_starred_pack")
    Call<String> unStarPackByHashAndPackID(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("if_user_starred_pack")
    Call<String> deletePackByHashAndPackID(@Body String body);
}
