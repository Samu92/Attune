package es.app.attune.attune.AttunePlayer;

import android.content.Context;
import android.support.annotation.Nullable;

import es.app.attune.attune.Database.AttPlaylist;
import es.app.attune.attune.Database.Song;

/**
 * Created by Samuel on 08/03/2018.
 */

public interface Player {
    void play(Song item);

    void pause();

    void resume();

    boolean isPlaying();

    @Nullable
    int getCurrentTrack();

    void release();

    void createMediaPlayer(String token, Context context);

    void setQueue(AttPlaylist item);

    void setQueue(AttPlaylist item, Song song);
}
