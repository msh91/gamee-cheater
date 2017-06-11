package io.github.sharifim91.gameecheater.data.repositories;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;

import io.github.sharifim91.gameecheater.api.ApiService;
import io.github.sharifim91.gameecheater.data.GameeScore;
import io.github.sharifim91.gameecheater.data.ResponseStatus;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sharifi on 6/9/17.
 */

public class GameeRepositoryIml implements GameeRepository {
    private static final String TAG = "GameeRepositoryIml";
    private ApiService mApiService;

    public GameeRepositoryIml(ApiService mApiService) {
        this.mApiService = mApiService;
    }

    @Override
    public void getGameDetail(String url, DetailResponseCallback callback) {
        mApiService
                .getGameDetail(url)
                .enqueue(new DetailCallback(callback));
    }

    @Override
    public void saveScore(GameeScore score, ScoreResponseCallback callback) {
        mApiService
                .saveScore(
                        score.getGameeUrl(),
                        "bots.gameeapp.com",
                        "https://www.gameeapp.com",
                        getRequestBody(score)
                )
                .enqueue(new ApiResponseCallback(callback));
    }

    private JsonObject getRequestBody(GameeScore score) {
        JsonObject body = new JsonObject();
        body.addProperty("score", score.getScore());
        body.addProperty("url", score.getGameeUrl().substring(score.getGameeUrl().indexOf("/game")));
        body.addProperty("play_time", score.getPlayTime());
        body.addProperty("hash", "{\"ct\":\"uXmTe2r+D+BBjY/iujvn1Srn3rn5kU1Hcv+Ceiu63M1uoCNMoZsjOviVE4nWx7WT\",\"iv\":\"de5a9766c0ee9cb425115b458c4523dd\",\"s\":\"8a0dddba0e6d03e0\"}");
        Log.d(TAG, "getRequestBody() returned: " + body);
        return body;
    }

    private class DetailCallback implements Callback<ResponseBody> {
        private DetailResponseCallback callback;

        DetailCallback(DetailResponseCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            Log.d(TAG, "onResponse() called with: response = [" + response.code() + "]");
            if (!response.isSuccessful()) {
                try {
                    Log.d(TAG, "onResponse: errorBody : " + response.errorBody().string());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                callback.onDetailLoaded(null, ResponseStatus.BAD_RESPONSE);
                return;
            }
            try {
                String result = response.body().string();
                Log.d(TAG, "onResponse: " + result.contains("data-id"));
                callback.onDetailLoaded(result, ResponseStatus.RECEIVED);

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            t.printStackTrace();
            callback.onDetailLoaded(null, ResponseStatus.NO_CONNECTION);
        }
    }

    private class ApiResponseCallback implements Callback<ResponseBody> {
        private ScoreResponseCallback callback;

        ApiResponseCallback(ScoreResponseCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            Log.d(TAG, "onResponse() called with: response = [" + response.code() + "]");
            if (!response.isSuccessful()) {
                ResponseStatus status = ResponseStatus.BAD_RESPONSE;
                try {
                    String result = response.errorBody().string();
                    JsonObject object = new Gson().fromJson(result, JsonObject.class);
                    status.setMessage(object.get("status").getAsString());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                callback.onSaveFinished(status);
                return;
            }
            ResponseStatus status = ResponseStatus.RECEIVED;
            try {
                String result = response.body().string();
                JsonObject object = new Gson().fromJson(result, JsonObject.class);
                status.setMessage(object.get("status").getAsString());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            callback.onSaveFinished(status);
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            t.printStackTrace();
            callback.onSaveFinished(ResponseStatus.NO_CONNECTION);
        }
    }
}
