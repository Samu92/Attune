package es.app.attune.attune.Classes;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Samuel on 08/03/2018.
 */

public class SearchInterfaces {

    public interface ResultPlaylist {
        void reset();

        void addDataPlaylist(List<Track> items);
    }

    public interface ResultGenres{
        void reset();

        void addDataGenres(List<String> items);
    }

    public interface ActionListener {

        void init(String token);

        void searchRecomendations(float tempo,String genre, int duration);

        void getAvailableGenreSeeds();

        void selectTrack(Track item);

        void resume();

        void pause();

        void destroy();
    }
}
