package io.github.sharifim91.gameecheater.cheater;

import android.text.TextUtils;

import io.github.sharifim91.gameecheater.data.repositories.GameeRepository;

/**
 * Created by sharifi on 6/9/17.
 */

public class CheaterPresenter implements CheaterContract.ActionListener {
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
        mViewListener.setProgressIndicator(true);

    }

}
