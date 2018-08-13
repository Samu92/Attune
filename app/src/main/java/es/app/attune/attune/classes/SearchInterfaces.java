package es.app.attune.attune.classes;

import java.util.List;

import es.app.attune.attune.database.AttPlaylist;
import es.app.attune.attune.database.Song;
import kaaes.spotify.webapi.android.models.UserPrivate;

/**
 * Created by Samuel on 08/03/2018.
 */

public class SearchInterfaces {

    public interface ResultPlaylist {
        void reset();

        void showListPlaylist();

        void showError(Throwable message);

        void addDataToSearchList(List<Song> items);
    }

    public interface ResultNewPlaylist{
        void dismissProgress();
    }

    public interface ResultGenres{
        void reset();

        void addDataGenres(List<String> items);
    }

    public interface ResultUserData{
        void setUserData(UserPrivate user);

        void setUserData();
    }

    public interface ActionListener {

        void init(String token);

        void searchRecomendations(AttPlaylist newPlaylist, int i);

        void exportToSpotify(String owner, AttPlaylist item);

        void getAvailableGenreSeeds();

        void getUserData();

        void search(String searchQuery, float mTempoFilter);

        void loadMoreResults();

        String getRecordedQuery(String keyCurrentQuery);
    }
}
