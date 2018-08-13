package es.app.attune.attune.attunePlayer;

import android.content.Context;

import es.app.attune.attune.database.AttPlaylist;
import es.app.attune.attune.database.Song;

/**
 * Created by Samuel on 08/03/2018.
 */

public interface Player {
    void play(Song item);

    void pause();

    void resume();

    boolean isPlaying();

    int getCurrentTrack();

    void release();

    void createMediaPlayer(String token, Context context);

    void setQueue(AttPlaylist item);

    void setQueue(AttPlaylist item, Song song);
}
