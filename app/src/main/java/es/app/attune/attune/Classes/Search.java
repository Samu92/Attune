package es.app.attune.attune.Classes;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Samuel on 08/03/2018.
 */

public class Search {

    public interface ResultPlaylist {
        void reset();

        void addData(List<Track> items);
    }

    public interface ActionListener {

        void init(String token);

        String getCurrentQuery();

        void search(String searchQuery);

        void loadMoreResults();

        void selectTrack(Track item);

        void resume();

        void pause();

        void destroy();

    }
}
