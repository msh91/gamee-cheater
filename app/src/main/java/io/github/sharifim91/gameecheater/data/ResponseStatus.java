package io.github.sharifim91.gameecheater.data;

import android.content.Context;
import android.text.TextUtils;

import io.github.sharifim91.gameecheater.R;

/**
 * Created by sharifi on 6/9/17.
 */

public enum ResponseStatus {
    /**
     * no error occurred
     */
    RECEIVED,
    /**
     * error in connecting to repository (Server or Database)
     */
    NO_CONNECTION,
    /**
     * error in getting response (Json Error, Server Error, etc)
     */
    BAD_RESPONSE,
    /**
     * no data available in repository
     */
    EMPTY_RESPONSE;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage(Context context) {
        if (!TextUtils.isEmpty(message)) {
            return message;
        }
        if (context == null) {
            return "";
        }
        switch (this) {
            case NO_CONNECTION:
                message = context.getString(R.string.error_connection);
                break;
            case BAD_RESPONSE:
                message = context.getString(R.string.error_response_wrong);
                break;
            case EMPTY_RESPONSE:
                message = context.getString(R.string.error_response_empty);
                break;
        }
        return message;
    }
}
