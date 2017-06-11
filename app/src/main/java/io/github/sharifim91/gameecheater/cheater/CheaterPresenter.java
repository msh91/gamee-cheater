package io.github.sharifim91.gameecheater.cheater;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.github.sharifim91.gameecheater.data.GameeScore;
import io.github.sharifim91.gameecheater.data.ResponseStatus;
import io.github.sharifim91.gameecheater.data.repositories.GameeRepository;

import static android.R.id.message;

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
        String hash = getHash(score, dataId);
        if (TextUtils.isEmpty(hash)) {
            mViewListener.showError(ResponseStatus.BAD_RESPONSE);
            return;
        }
        score.setHash(hash);
        score.setPlayTime(15);
        Log.d(TAG, "prepareSaveScore() returned: score : " + score);
        mGameeRepository.saveScore(score, new SaveScoreCallback());
    }

    private String getHash(GameeScore score, String dataId) {
        if (dataId.length() < 16) {
            return null;
        }
        String s = dataId.substring(0, 32);
        SecretKey secretKey = new SecretKeySpec(s.getBytes(), "AES");
        JsonObject content = new JsonObject();
        content.addProperty("score", score.getScore());
        content.addProperty("timestamp", new Date().getTime());
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            String iv = Base64.encodeToString(cipher.getIV(), Base64.DEFAULT);
            byte[] cipherText = cipher.doFinal(content.toString().getBytes("UTF-8"));
            String ct = new String(Base64.encode(cipherText, Base64.DEFAULT), "UTF-8");
            JsonObject hash = new JsonObject();
            hash.addProperty("ct", ct);
            hash.addProperty("iv", iv);
            hash.addProperty("s", s);
            Log.d(TAG, "getHash() returned: " + hash.toString());
            return hash.toString();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }


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
        String data = element.attr("data-id");
        Log.d(TAG, "getDataId() returned: " + data);
        return data;
    }

    private class SaveScoreCallback implements GameeRepository.ScoreResponseCallback {
        @Override
        public void onSaveFinished(ResponseStatus status) {
            Log.d(TAG, "onSaveFinished() called with: status = [" + status + "]");
            mViewListener.showError(status);
        }
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
