package es.app.attune.attune.Classes;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import es.app.attune.attune.Database.AttPlaylist;
import es.app.attune.attune.Database.DaoSession;
import es.app.attune.attune.Database.Song;
import es.app.attune.attune.Interfaces.Player;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.AudioFeaturesTrack;
import kaaes.spotify.webapi.android.models.AudioFeaturesTracks;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Samuel on 08/03/2018.
 */

public class SearchFunctions implements SearchInterfaces.ActionListener {
    private static final String TAG = SearchFunctions.class.getSimpleName();
    public static final int SIZE = 50;

    private final Context mContext;
    private final SearchInterfaces.ResultPlaylist mResultPlaylist;
    private final SearchInterfaces.ResultGenres mResultGenres;
    private float mTempo;
    private String mGenre;
    private float mDuration;
    private AttPlaylist newPlaylist;

    private SearchSpotify mSearchPager;
    private SearchSpotify.CompleteListener mSearchListener;
    private SearchSpotify.GenresListener mGenresListener;

    private DaoSession daoSession;
    private DatabaseFunctions db;

    private Player mPlayer;


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayer = ((PlayerService.PlayerBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayer = null;
        }
    };

    public SearchFunctions(Context context, SearchInterfaces.ResultPlaylist result, SearchInterfaces.ResultGenres result_genres) {
        mContext = context;
        mResultPlaylist = result;
        mResultGenres = result_genres;

        // Inicializamos la sesión de base de datos
        daoSession = ((App) context.getApplicationContext()).getDaoSession();
        db = new DatabaseFunctions(daoSession);
    }

    @Override
    public void init(String accessToken) {
        logMessage("Api Client created");
        SpotifyApi spotifyApi = new SpotifyApi();

        if (accessToken != null) {
            spotifyApi.setAccessToken(accessToken);
        } else {
            logError("No valid access token");
        }

        mSearchPager = new SearchSpotify(spotifyApi.getService());

        mContext.bindService(PlayerService.getIntent(mContext), mServiceConnection, Activity.BIND_AUTO_CREATE);
    }

    @Override
    public void destroy() {
        mContext.unbindService(mServiceConnection);
    }

    @Override
    public void searchRecomendations(final AttPlaylist newPlaylist) {
        final float tempo  = newPlaylist.getTempo();
        String genre = newPlaylist.getGenre();
        int duration = newPlaylist.getDuration();

        if (tempo != 0 && !genre.isEmpty()) {
            mTempo = tempo;
            mGenre = genre;
            mDuration = duration;

            mResultPlaylist.reset();
            mSearchListener = new SearchSpotify.CompleteListener() {
                @Override
                public void onComplete(List<Track> items, AudioFeaturesTracks audioFeaturesTracks) {
                    for (Track track: items) {
                        UUID newUUID = java.util.UUID.randomUUID();
                        String newId = newUUID.toString();
                        String playlistId = newPlaylist.getId();
                        String trackId = track.id;
                        String spotifyId = track.uri;
                        String genreId = newPlaylist.getGenre();
                        String name = track.name;
                        long duration = track.duration_ms;
                        String artist = track.artists.get(0).name;
                        String imageUri = "";
                        float tempo = 0;
                        for (AudioFeaturesTrack feature: audioFeaturesTracks.audio_features) {
                            if(track.id.equals(feature.id)){
                                tempo = feature.tempo;
                            }
                        }

                        Song song = new Song(newId,playlistId,trackId,spotifyId,genreId,name,duration,tempo,artist,imageUri);
                        db.insertSong(song);
                    }
                    db.insertNewPlaylist(newPlaylist);
                    mResultPlaylist.showListPlaylist();
                }

                @Override
                public void onError(Throwable error) {
                    logError(error.getMessage());
                    mResultPlaylist.showError(error.getMessage());
                }
            };
            mSearchPager.getRecomendationPlaylist(newPlaylist, SIZE, mSearchListener);
        }
    }

    @Override
    public void getAvailableGenreSeeds() {
        mGenresListener = new SearchSpotify.GenresListener(){
            @Override
            public void onComplete(List<String> items) {
                mResultGenres.addDataGenres(items);
            }

            @Override
            public void onError(Throwable error) {
                logError(error.getMessage());
            }
        };
        mSearchPager.getGenres(mGenresListener);
    }

    @Override
    public void getUserData() {

    }

    @Override
    public void resume() {
        mContext.stopService(PlayerService.getIntent(mContext));
    }

    @Override
    public void pause() {
        mContext.startService(PlayerService.getIntent(mContext));
    }

    @Override
    public void selectTrack(Song item) {
        String previewUrl = item.getUrlSpotify();

        if (previewUrl == null) {
            logMessage("Track doesn't have a preview");
            return;
        }

        if (mPlayer == null) return;

        String currentTrackUrl = mPlayer.getCurrentTrack();

        if (currentTrackUrl == null || !currentTrackUrl.equals(previewUrl)) {
            mPlayer.play(previewUrl);
        } else if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.resume();
        }
    }

    private void logError(String msg) {
        Toast.makeText(mContext, "Error: " + msg, Toast.LENGTH_SHORT).show();
        Log.e(TAG, msg);
    }

    private void logMessage(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, msg);
    }
}
