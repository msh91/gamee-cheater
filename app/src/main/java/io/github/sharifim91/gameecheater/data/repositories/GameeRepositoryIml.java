package io.github.sharifim91.gameecheater.data.repositories;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

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
        Gson gson = new Gson();
        JSONObject body = gson.fromJson(gson.toJson(score), JSONObject.class);
        mApiService
                .saveScore(body)
                .enqueue(new ApiResponseCallback(callback));
    }

    private class DetailCallback implements Callback<String> {
        private DetailResponseCallback callback;

        DetailCallback(DetailResponseCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<String> call, Response<String> response) {
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
            callback.onDetailLoaded(response.body(), ResponseStatus.RECEIVED);
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
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
                try {
                    Log.d(TAG, "onResponse: errorBody : " + response.errorBody().string());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                callback.onSaveFinished(ResponseStatus.BAD_RESPONSE);
                return;
            }
            try {
                Log.d(TAG, "onResponse: body : " + response.body().string());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            callback.onSaveFinished(ResponseStatus.RECEIVED);
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            t.printStackTrace();
            callback.onSaveFinished(ResponseStatus.NO_CONNECTION);
        }
    }
}
