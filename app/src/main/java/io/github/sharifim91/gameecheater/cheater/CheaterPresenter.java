package io.github.sharifim91.gameecheater.cheater;

import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.github.sharifim91.gameecheater.data.GameeScore;
import io.github.sharifim91.gameecheater.data.ResponseStatus;
import io.github.sharifim91.gameecheater.data.repositories.GameeRepository;

/**
 * Created by sharifi on 6/9/17.
 */

public class CheaterPresenter implements CheaterContract.ActionListener {
    private static final String TAG = "CheaterPresenter";
    private final GameeRepository mGameeRepository;
    private CheaterContract.ViewListener mViewListener;

    public CheaterPresenter(CheaterContract.ViewListener mViewListener, GameeRepository repository) {
        this.mViewListener = mViewListener;
        mGameeRepository = repository;
    }

    @Override
    public void attemptSaveScore(String gameeUrl, String score) {
        if (TextUtils.isEmpty(gameeUrl)) {
            mViewListener.showUrlError();
            return;
        }
        if (TextUtils.isEmpty(score)) {
            mViewListener.showScoreUrl();
            return;
        }
        GameeScore gameeScore = new GameeScore();
        gameeScore.setGameeUrl(gameeUrl);
        gameeScore.setScore(Long.valueOf(score));

        mViewListener.setProgressIndicator(true);
        mGameeRepository.getGameDetail(gameeUrl, new GetDetailCallback(gameeScore));
    }

    @Override
    public void prepareSaveScore(GameeScore score, String gameDetail) {
        String dataId = getDataId(gameDetail);
        Log.d(TAG, "prepareSaveScore: dataId : " + dataId);
        if (TextUtils.isEmpty(dataId)) {
            mViewListener.showError(ResponseStatus.BAD_RESPONSE);
            return;
        }
        getHash(score, dataId);

    }

    private void getHash(GameeScore score, String dataId) {
//        SecretKey secretKey = new SecretKeySpec();
    }

    private String getDataId(String html) {
        if (TextUtils.isEmpty(html)) {
            return null;
        }
        Document document = Jsoup.parse(html);
        Element element = document.getElementById("dataId");
        if (element == null) {
            return null;
        }
        Log.d(TAG, "getDataId: element : " + element.data());
        Element data = element.getElementById("data-id");
        if (data == null) {
            return null;
        }
        return data.data();
    }

    private class GetDetailCallback implements GameeRepository.DetailResponseCallback {
        private GameeScore score;

        GetDetailCallback(GameeScore gameeScore) {
            score = gameeScore;
        }

        @Override
        public void onDetailLoaded(String detail, ResponseStatus status) {
            Log.d(TAG, "onDetailLoaded() called with: detail = [" + detail + "], status = [" + status + "]");
            if (status != ResponseStatus.RECEIVED) {
                mViewListener.showError(status);
                return;
            }
            prepareSaveScore(score, detail);
        }
    }
}
