package es.app.attune.attune.Classes;

import java.util.List;

import es.app.attune.attune.Database.AttPlaylist;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Samuel on 08/03/2018.
 */

public class SearchInterfaces {

    public interface ResultPlaylist {
        void reset();

        void showListPlaylist();
    }

    public interface ResultGenres{
        void reset();

        void addDataGenres(List<String> items);
    }

    public interface ActionListener {

        void init(String token);

        void searchRecomendations(AttPlaylist newPlaylist);

        void getAvailableGenreSeeds();

        void selectTrack(Track item);

        void resume();

        void pause();

        void destroy();
    }
}
