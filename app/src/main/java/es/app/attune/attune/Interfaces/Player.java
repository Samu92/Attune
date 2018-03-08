package es.app.attune.attune.Interfaces;

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
}
