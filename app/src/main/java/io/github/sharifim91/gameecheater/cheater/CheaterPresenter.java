package io.github.sharifim91.gameecheater.cheater;

import android.text.TextUtils;

/**
 * Created by sharifi on 6/9/17.
 */

public class CheaterPresenter implements CheaterContract.ActionListener {
    private CheaterContract.ViewListener mViewListener;

    public CheaterPresenter(CheaterContract.ViewListener mViewListener) {
        this.mViewListener = mViewListener;
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
