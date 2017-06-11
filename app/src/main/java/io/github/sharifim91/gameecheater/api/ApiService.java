package io.github.sharifim91.gameecheater.api;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by sharifi on 6/9/17.
 */

public interface ApiService {
    @POST("/set-web-score-qkfnsog26w7173c9pk7whg0iau7zwhdkfd7ft3tn")
    Call<ResponseBody> saveScore(
            @Header("Referer") String referer,
            @Header("Host") String host,
            @Header("Origin") String origin,
            @Body JsonObject body
            );


    @GET
    Call<ResponseBody> getGameDetail(@Url String url);
}
