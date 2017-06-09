package io.github.sharifim91.gameecheater.cheater;

/**
 * Created by sharifi on 6/9/17.
 */

public class CheaterPresenter implements CheaterContract.ActionListener {
    private CheaterContract.ViewListener mViewListener;

    public CheaterPresenter(CheaterContract.ViewListener mViewListener) {
        this.mViewListener = mViewListener;
    }

    @Override
    public void savePoint(String gameeUrl, long score) {

    }
}
