package io.github.sharifim91.gameecheater.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sharifi on 6/9/17.
 */

public class GameeScore {
    @SerializedName("score")
    private long score;

    @SerializedName("url")
    private String gameeUrl;

    @SerializedName("play_time")
    private int playTime;

    @SerializedName("hash")
    private String hash;

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getGameeUrl() {
        return gameeUrl;
    }

    public void setGameeUrl(String gameeUrl) {
        this.gameeUrl = gameeUrl;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
