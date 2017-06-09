package io.github.sharifim91.gameecheater.data.repositories;

import io.github.sharifim91.gameecheater.data.GameeScore;
import io.github.sharifim91.gameecheater.data.ResponseStatus;

/**
 * Created by sharifi on 6/9/17.
 */

public interface GameeRepository {
    void getGameDetail(String url, DetailResponseCallback callback);
    void saveScore(GameeScore score, ScoreResponseCallback callback);

    interface ScoreResponseCallback {
        void onSaveFinished(ResponseStatus status);
    }

    interface DetailResponseCallback {
        void onDetailLoaded(String detail, ResponseStatus status);
    }
}
