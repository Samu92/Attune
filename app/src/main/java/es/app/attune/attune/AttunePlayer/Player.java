package es.app.attune.attune.AttunePlayer;

import android.content.Context;
import android.support.annotation.Nullable;

/**
 * Created by Samuel on 08/03/2018.
 */

public interface Player {
    void play(String url);

    void pause();

    void resume();

    boolean isPlaying();

    @Nullable
    String getCurrentTrack();

    void release();

    void createMediaPlayer(String token, Context context);
}
