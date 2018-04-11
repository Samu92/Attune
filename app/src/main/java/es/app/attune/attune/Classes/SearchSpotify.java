package es.app.attune.attune.Classes;

import com.spotify.sdk.android.player.Spotify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Recommendations;
import kaaes.spotify.webapi.android.models.SeedsGenres;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Samuel on 08/03/2018.
 */

public class SearchSpotify {

    private final SpotifyService mSpotifyApi;
    private int mCurrentOffset;
    private int mSize;
    private float mTempo;

    public interface CompleteListener {
        void onComplete(List<Track> items);
        void onError(Throwable error);
    }

    public interface GenresListener{
        void onComplete(List<String> items);
        void onError(Throwable error);
    }

    public SearchSpotify(SpotifyService spotifyApi) {
        mSpotifyApi = spotifyApi;
    }

    public void getRecomendationPlaylist(float tempo, String genre, int size, CompleteListener listener){
        mCurrentOffset = 0;
        mSize = size;
        mTempo = tempo;
        getDataPlaylist(tempo, genre, 0, size, listener);
    }

    public void getGenres(final GenresListener listener){
        getDataGenres(listener);
    }

    private void getDataPlaylist(float tempo, String genre, int offset, final int limit, final CompleteListener listener){
        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.OFFSET, offset);
        options.put(SpotifyService.LIMIT, limit);
        options.put(SpotifyService.MARKET, "ES");
        options.put("seed_genres",genre);
        options.put("target_tempo",tempo);

        mSpotifyApi.getRecommendations(options, new Callback<Recommendations>() {
            @Override
            public void success(Recommendations recommendations, Response response) {
                listener.onComplete(recommendations.tracks);
            }

            @Override
            public void failure(RetrofitError error) {
                listener.onError(error);
            }
        });
    }

    private void getDataGenres(final GenresListener listener){
        mSpotifyApi.getSeedsGenres(new Callback<SeedsGenres>() {
            @Override
            public void success(SeedsGenres seedsGenres, Response response) {
                listener.onComplete(seedsGenres.genres);
            }

            @Override
            public void failure(RetrofitError error) {
                listener.onError(error);
            }
        });
    }
}
