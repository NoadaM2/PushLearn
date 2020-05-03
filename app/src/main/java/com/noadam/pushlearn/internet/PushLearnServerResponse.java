package com.noadam.pushlearn.internet;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.data.ParserFromJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;



public class PushLearnServerResponse {


    private Context context;
    private Retrofit mRetrofit;
    private PushLearnServer apiInterface;

    public PushLearnServerResponse(Context context) {
        String BASE_URL = "http://pushlearn.hhos.ru.s68.hhos.ru/index.php/";
        this.context = context;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
        apiInterface = mRetrofit.create(PushLearnServer.class);

    }

    public PushLearnServerResponse(Context context, String mode) {
        String BASE_URL = "";
        if(mode == "avatar") {
            BASE_URL = "http://pushlearn.hhos.ru.s68.hhos.ru/upload_file.php/";
        }
        this.context = context;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
        apiInterface = mRetrofit.create(PushLearnServer.class);

    }

    public void sendBusyEmailResponse(String email, PushLearnServerStringCallBack callback) { // checked stable
        try {
            JSONObject childData = new JSONObject();
            childData.put("email",email);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "busy_email");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.busyEmail(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetNickNameByHashResponse(String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("hash",hash);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_nickname_by_hash");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getNickNameByHash(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetPremiumByHashResponse(String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("hash",hash);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_premium_by_hash");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.checkPremium(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetStarredPacksByHashResponse(String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("hash",hash);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_starred_packs_by_hash");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getStarredPacksByHash(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendStarPackByHashResponse(int packID, String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("hash",hash);
            childData.put("id_pack",packID);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "join_pack");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.starPackByHash(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetComPackByIDResponse(int packID, String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("hash",hash);
            childData.put("id_pack",packID);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "return_pack_from_id");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getComPackByID(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendIfUserStaredPackByHashAndPackIDResponse(int packID, String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("hash", hash);
            childData.put("id_pack",packID);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "if_user_starred_pack");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.ifUserStaredPackByHashAndPackID(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendUnStarPackByHashAndPackIDResponse(int packID, String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("hash", hash);
            childData.put("from", "user");
            childData.put("id_pack",packID);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "delete_pack");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.unStarPackByHashAndPackID(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendDeletePackByHashAndPackIDResponse(int packID, String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("hash", hash);
            childData.put("from", "bd");
            childData.put("id_pack",packID);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "delete_pack");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.deletePackByHashAndPackID(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetNickNameByIDResponse(int id, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("id",id);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_nickname_by_id");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getNickNameByID(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetSubDirectoriesByDirectoryIDResponse(int directory_id, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("id_directory", directory_id);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_subdirectories");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getSubDirectoriesByDirectoryID(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetSubDirectoryByIDResponse(int subdirectory_id, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("id_subdirectory", subdirectory_id);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_subdirectory_by_id");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getSubDirectoryByID(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetDirectoryByIDResponse(int directory_id, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("id_directory", directory_id);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_directory_by_id");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getDirectoryByID(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendSetNickNameByHashResponse(String nickname, String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("nickname", nickname);
            childData.put("hash", hash);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "set_nickname_by_hash");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.setNickNameByHash(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetIdByNickNameResponse(String nickname, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("nickname", nickname);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_id_by_nickname");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getIdByNickName(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendCreatePackResponse(String packName, String description, int directory_id, int subdirectory_id,String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("name", packName);
            childData.put("description", description);
            childData.put("access", "public"); // TODO Access here
            childData.put("directory_id", directory_id);
            childData.put("subdirectory_id", subdirectory_id);
            childData.put("hash", hash);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "create_pack");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.createPack(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendUpdatePackResponse(int packID, String packName,String description, int directory_id, int subdirectory_id,String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("id_pack", packID);
            childData.put("name", packName);
            childData.put("description", description);
            childData.put("access", "public"); // TODO Access here
            childData.put("directory_id", directory_id);
            childData.put("subdirectory_id", subdirectory_id);
            childData.put("hash", hash);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "set_pack");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.updatePackByPackID(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendCreateCardResponse(int id_pack,String question,String answer, String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("id_pack", id_pack);
            childData.put("question", question);
            childData.put("answer", answer);
            childData.put("hash", hash);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "create_card");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.createCard(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetDirectoriesResponse(int language_id, PushLearnServerDirectoriesListCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("language_id", language_id);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_directories_by_language_id");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getDirectoriesByLanguageID(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        ParserFromJSON parser = new ParserFromJSON();
                        callback.onResponse(parser.parseJsonDirectoriesArray(a));
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetTopUsersResponse(PushLearnServerStringCallBack callback) {
        try {
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_top_users");

            Call<String> userCall = apiInterface.getTopUsers(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetPreviousTopUsersResponse(PushLearnServerStringCallBack callback) {
        try {
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_previous_top");

            Call<String> userCall = apiInterface.getPreviousTopUsers(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendEmailVerificationCodeResponse(String email, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("email",email);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "confirm_email");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.sendEmailVerificationCode(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkEmailVerificationCodeResponse(String email, String code, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("email",email);
            childData.put("code",code);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "check_code");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.checkEmailVerificationCode(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetCardsOfComPackByPackIDResponse(int packID, String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("hash", hash);
            childData.put("id_pack", packID);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_cards_by_packID");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getCardsOfComPackByPackID(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetPacksByNickNameResponse(String nickname, String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("hash", hash);
            childData.put("nickname", nickname);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_packs_by_nickname");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getPacksByNickName(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetPacksByPackNameResponse(String packname, int page, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("string", packname);
            childData.put("offset", page);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_packs_by_packname");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getPacksByPackName(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetPacksByDirectoryIdAndPackNameResponse(String packname,int directory_id, int page, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("string", packname);
            childData.put("id_directory", directory_id);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_packs_by_directory_id_and_packname");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getPacksByDirectoryIdAndPackName(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetPacksByDirectoryIdAndSubDirectoryIdAndPackNameResponse(String packname, int directory_id,  int subdirectory_id, int page, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("string", packname);
            childData.put("id_directory", directory_id);
            childData.put("offset", page);
            childData.put("id_subdirectory", subdirectory_id);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_packs_by_directory_id_and_subdirectory_id_and_packname");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getPacksByDirectoryIdAndSubDirectoryIdAndPackName(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetPacksByPackNameAndDescriptionResponse(String packname, String description, int page, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("packname", packname);
            childData.put("description", description);
            childData.put("offset", page);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_packs_by_description_and_packname");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getPacksByPackName(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetPacksByDirectoryIdAndPackNameAndDescriptionResponse(String packname, String description, int directory_id, int page, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("string", packname);
            childData.put("description", description);
            childData.put("id_directory", directory_id);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_packs_by_directory_id_and_packname_and_description");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getPacksByDirectoryIdAndPackName(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetPacksByDirectoryIdAndSubDirectoryIdAndPackNameResponse(String packname, String description, int directory_id,  int subdirectory_id, int page, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("string", packname);
            childData.put("description", description);
            childData.put("id_directory", directory_id);
            childData.put("offset", page);
            childData.put("id_subdirectory", subdirectory_id);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_packs_by_directory_id_and_subdirectory_id_and_packname_and_description");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getPacksByDirectoryIdAndSubDirectoryIdAndPackName(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendBusyNickNameResponse(String nickname, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("nickname", nickname);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "busy_nickname");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.busyNickName(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetNumberOfComPacksByNickNameResponse(String nickname, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("nickname", nickname);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_number_of_packs_by_nickname");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getNumberOfComPacksByNickName(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetRatingByNickNameResponse(String nickname, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("nickname", nickname);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_rating_by_nickname");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getRatingByNickName(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetLanguageIDByNickNameResponse(String nickname, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("nickname", nickname);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "get_language_id_by_nickname");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.getLanguageIDByNickName(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendSignInResponse(String login, String password, PushLearnServerStringCallBack callback) { // checked stable
        try {
            JSONObject childData = new JSONObject();
            childData.put("email",login);
            childData.put("password",password);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "sign_in");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.signIn(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendSignUpResponse(String email, String password, String nickname, int language_id, PushLearnServerStringCallBack callback) { // checked stable
        try {
            JSONObject childData = new JSONObject();
            childData.put("email",email);
            childData.put("password",password);
            childData.put("nickname",nickname);
            childData.put("language_id",language_id);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "sign_up");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.signUp(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendLogInUsingVKResponse(String token, String vk_id, int lang_id, PushLearnServerStringCallBack callback) { // checked stable
        try {
            JSONObject childData = new JSONObject();
            childData.put("token",token);
            childData.put("vk_id",vk_id);
            childData.put("language_id", lang_id);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "log_vk");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.logInUsingVK(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendLogInUsingGoogleResponse(String token, String nickname, String email, int lang_id, String photoUrl, PushLearnServerStringCallBack callback) { // checked stable
        try {
            JSONObject childData = new JSONObject();
            childData.put("id_token",token);
            childData.put("nickname",nickname);
            childData.put("email",email);
            childData.put("language_id",lang_id);
            childData.put("photo",photoUrl);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "log_google");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.logInUsingGoogle(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendUpdateAvatarResponse(String hash, File avatar,String filename, PushLearnServerStringCallBack callback) {
        RequestBody rb =  RequestBody.create(MediaType.parse("multipart/form-data"), avatar);
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uploadfile",filename, rb)
                .addFormDataPart("hash",hash)
                .build();
            Call<String> userCall = apiInterface.editAvatar(body);
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
    }

    public void sendCreateDirectoryByHashResponse(String directory, String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("directory", directory);
            childData.put("hash", hash);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "add_directory");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.addDirectoryByHash(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendCreateSubDirectoryByHashResponse(String subdirectory,int directory_id, String hash, PushLearnServerStringCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("directory_id", directory_id);
            childData.put("subdirectory", subdirectory);
            childData.put("hash", hash);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "add_subdirectory");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.addSubDirectoryByHash(finalObject.toString());
            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response != null) {
                        String a = response.body();
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError(t);
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
   /* private void onFailureInternet(Throwable t){
        if (t instanceof IOException) {
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
            // logging probably not necessary
        }
        else {
            //  Toast.makeText(getApplicationContext(), "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
        }
    }*/
}