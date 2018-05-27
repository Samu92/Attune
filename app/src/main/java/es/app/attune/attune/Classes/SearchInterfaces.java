package es.app.attune.attune.Classes;

import android.app.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

import es.app.attune.attune.Database.AttPlaylist;
import es.app.attune.attune.Database.Song;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.UserPrivate;

/**
 * Created by Samuel on 08/03/2018.
 */

public class SearchInterfaces {

    public interface ResultPlaylist {
        void reset();

        void showListPlaylist();

        void showError(String message);
    }

    public interface ResultGenres{
        void reset();

        void addDataGenres(List<String> items);
    }

    public interface ResultUserData{
        void setUserData(UserPrivate user);
    }

    public interface ActionListener {

        void init(String token);

        void searchRecomendations(AttPlaylist newPlaylist);

        void getAvailableGenreSeeds();

        void createPlayer();

        void getUserData();

        void selectTrack(Song item);

        void resume();

        void pause();

        void destroy();
    }
}
