package io.github.sharifim91.gameecheater.cheater;

import io.github.sharifim91.gameecheater.data.ResponseStatus;

/**
 * Created by sharifi on 6/9/17.
 */

public interface CheaterContract {
    interface ViewListener {
        void setProgressIndicator(boolean active);

        void showMessage(String message);

        void showError(ResponseStatus status);
    }

    interface ActionListener {
        void savePoint(String gameeUrl, long score);
    }
}
