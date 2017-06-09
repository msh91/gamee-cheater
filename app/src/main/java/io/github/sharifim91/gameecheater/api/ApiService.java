package io.github.sharifim91.gameecheater.api;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by sharifi on 6/9/17.
 */

public interface ApiService {
    @POST("/set-web-score-qkfnsog26w7173c9pk7whg0iau7zwhdkfd7ft3tn")
    Call<ResponseBody> saveScore(@Body JSONObject body);

    @GET
    Call<String> getGameDetail(@Url String url);
}
