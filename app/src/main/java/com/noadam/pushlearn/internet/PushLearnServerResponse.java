package com.noadam.pushlearn.internet;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class PushLearnServerResponse {

    static final String BASE_URL = "http://pushlearn.hhos.ru.s68.hhos.ru/index.php/";
    private Context context;
    private Retrofit mRetrofit;
    private PushLearnServer apiInterface;

    public PushLearnServerResponse(Context context) {
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

    public void sendBusyEmailResponse(String email, PushLearnServerCallBack callback) { // checked stable
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
                    callback.onError();
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetNickNameByHashResponse(String hash, PushLearnServerCallBack callback) {
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
                    callback.onError();
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

   /* public void sendUpdateMyComPackListResponse(String IDsOfSavedMyComPacks, String hash, PushLearnServerCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("hash", hash);
            childData.put("ids", IDsOfSavedMyComPacks);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "update_packs");
            finalObject.put("object", childData);

            Call<String> userCall = apiInterface.updateMyComPacks(finalObject.toString());
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
                    callback.onError();
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
*/
    public void sendGetCardsOfComPackByPackIDResponse(int packID, String hash, PushLearnServerCallBack callback) {
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
                        String a = parseArrayFromServerResponse(response.body());
                        callback.onResponse(a);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.onError();
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String parseArrayFromServerResponse(String response) {
        response.replaceAll("\"|[|]","");
        return response;
    }

    public void sendGetComPackByIDResponse(String id, String hash, PushLearnServerCallBack callback) {
        try {
            JSONObject childData = new JSONObject();
            childData.put("hash", hash);
            childData.put("id_pack", id);
            JSONObject finalObject = new JSONObject();
            finalObject.put("type", "update_packs");
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
                    callback.onError();
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
 public void sendGetPacksByNickNameResponse(String nickname, String hash, PushLearnServerCallBack callback) {
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
                    callback.onError();
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendBusyNickNameResponse(String nickname, PushLearnServerCallBack callback) {
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
                    callback.onError();
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void sendGetNumberOfComPacksByNickNameResponse(String nickname, PushLearnServerCallBack callback) {
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
                    callback.onError();
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void sendGetRatingByNickNameResponse(String nickname, PushLearnServerCallBack callback) {
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
                    callback.onError();
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGetLanguageIDByNickNameResponse(String nickname, PushLearnServerCallBack callback) {
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
                    callback.onError();
                Log.v("SERVER  ERROR", t+"");
            }
        });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendSignInResponse(String login, String password, PushLearnServerCallBack callback) { // checked stable
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
                    callback.onError();
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendSignUpResponse(String email, String password, String nickname, int language_id, PushLearnServerCallBack callback) { // checked stable
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
                    callback.onError();
                    Log.v("SERVER  ERROR", t+"");
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
