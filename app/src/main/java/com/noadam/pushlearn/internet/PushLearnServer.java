package com.noadam.pushlearn.internet;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
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

    @Headers("Content-Type: application/json")
    @POST("get_subdirectories_by_directory_id")
    Call<String> getSubDirectoriesByDirectoryID(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("get_directories")
    Call<String> getDirectories(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("create_pack")
    Call<String> createPack(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("create_card")
    Call<String> createCard(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("compare_user_id_and_hash")
    Call<String> compareUSerIdAndHash(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("set_nickname_by_hash")
    Call<String> setNickNameByHash(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("get_packs_by_packname")
    Call<String>getPacksByPackName(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("get_packs_by_directory_id_and_packname")
    Call<String>getPacksByDirectoryIdAndPackName(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("get_packs_by_directory_id_and_subdirectory_id_and_packname")
    Call<String>getPacksByDirectoryIdAndSubDirectoryIdAndPackName(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("log_vk")
    Call<String>logInUsingVK(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("get_id_by_nickname")
    Call<String>getIdByNickName(@Body String body);

    @Headers("Cookie: antibot-hostia=true")
    @POST("/upload_file.php/{hash}")
    Call<String> editAvatar(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("get_subdirectories_by_directory_id")
    Call<String> getDirectoryByID(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("get_subdirectories_by_directory_id")
    Call<String> getSubDirectoryByID(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("set_pack")
    Call<String> updatePackByPackID(@Body String body);
}
