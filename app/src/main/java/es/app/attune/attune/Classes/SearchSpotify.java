package es.app.attune.attune.Classes;

import com.spotify.sdk.android.player.Spotify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.app.attune.attune.Database.AttPlaylist;
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
    private float mDuration;

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

    public void getRecomendationPlaylist(AttPlaylist playlist, int size, CompleteListener listener){
        mCurrentOffset = 0;
        mSize = size;
        mTempo = playlist.getTempo();
        mDuration = playlist.getDuration();
        getDataPlaylist(playlist.getTempo(), playlist.getGenre(), playlist.getDuration(), 0, size, listener);
    }

    public void getGenres(final GenresListener listener){
        getDataGenres(listener);
    }

    private void getDataPlaylist(float tempo, String genre, final int max_duration, int offset, final int limit, final CompleteListener listener){
        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.OFFSET, offset);
        options.put(SpotifyService.LIMIT, limit);
        options.put(SpotifyService.MARKET, "ES");
        options.put("seed_genres",genre);
        options.put("target_tempo",tempo);

        mSpotifyApi.getRecommendations(options, new Callback<Recommendations>() {
            @Override
            public void success(Recommendations recommendations, Response response) {
                List<Track> result = new ArrayList<>();
                int temp_duration = 0;
                //listener.onComplete(recommendations.tracks);
                for (Track track: recommendations.tracks) {
                    if(temp_duration < (max_duration*60000)){
                        temp_duration += track.duration_ms;
                        result.add(track);
                    }
                }
                listener.onComplete(result);
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
