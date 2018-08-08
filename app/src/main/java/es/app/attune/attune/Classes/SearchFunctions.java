package es.app.attune.attune.Classes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import es.app.attune.attune.Database.AttPlaylist;
import es.app.attune.attune.Database.DaoSession;
import es.app.attune.attune.Database.Song;
import es.app.attune.attune.Fragments.NewPlayList;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.AudioFeaturesTrack;
import kaaes.spotify.webapi.android.models.AudioFeaturesTracks;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.UserPrivate;

/**
 * Created by Samuel on 08/03/2018.
 */

public class SearchFunctions implements SearchInterfaces.ActionListener {
    private static final String TAG = SearchFunctions.class.getSimpleName();
    public static final int SIZE = 50;
    private static final String CLIENT_ID = "";
    private static final int PAGE_SIZE = 20;

    private final Context mContext;
    private final SearchInterfaces.ResultPlaylist mResultPlaylist;
    private final SearchInterfaces.ResultGenres mResultGenres;
    private final SearchInterfaces.ResultUserData mResultUserData;
    private final SearchInterfaces.ResultNewPlaylist mResultNewPlaylist;
    private float mTempo;
    private String mGenre;
    private float mDuration;
    private AttPlaylist newPlaylist;

    private SearchSpotify mSearchPager;
    private SearchSpotify.CompleteListener mSearchListener;
    private SearchSpotify.ManualSearchListener mManualSearchListener;
    private SearchSpotify.GenresListener mGenresListener;
    private SearchSpotify.UserDataListener mUserDataListener;

    private DaoSession daoSession;
    private DatabaseFunctions db;

    private String mCurrentQuery;

    public SearchFunctions(Context context, SearchInterfaces.ResultPlaylist result, SearchInterfaces.ResultGenres result_genres, SearchInterfaces.ResultUserData result_userdata, SearchInterfaces.ResultNewPlaylist resultNewPlaylist) {
        mContext = context;
        mResultPlaylist = result;
        mResultGenres = result_genres;
        mResultUserData = result_userdata;
        mResultNewPlaylist = resultNewPlaylist;

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
            Toast.makeText(mContext,"No valid access token", Toast.LENGTH_SHORT).show();
        }
        mSearchPager = new SearchSpotify(spotifyApi.getService());
    }

    @Override
    public void searchRecomendations(final AttPlaylist playlist, final int mode) {
        if(mode == 0){
            final float tempo  = playlist.getTempo();
            String genre = playlist.getGenre();

            if (tempo != 0 && !genre.isEmpty()) {
                mTempo = tempo;
                mGenre = genre;

                mResultPlaylist.reset();
                mSearchListener = new SearchSpotify.CompleteListener() {
                    @Override
                    public void onComplete(List<Track> items, AudioFeaturesTracks audioFeaturesTracks, Map<String, String> dates) {
                        long playlist_duration = 0;
                        List<Song> songs = new ArrayList<Song>();
                        int i = 0;
                        for (Track track: items) {
                            UUID newUUID = java.util.UUID.randomUUID();
                            String newId = newUUID.toString();
                            //int position = db.getSongNextPosition(playlist.getId());
                            int position = i;
                            i += 1;
                            String playlistId = playlist.getId();
                            String trackId = track.id;
                            String spotifyId = track.uri;
                            String genreId = playlist.getGenre();
                            String name = track.name;
                            long duration = track.duration_ms;
                            String artist = track.artists.get(0).name;
                            String imageUri = track.album.images.get(0).url;
                            String previewUrl = track.preview_url;
                            float acousticness = 0;
                            float danceability = 0;
                            float energy = 0;
                            float instrumentalness = 0;
                            float liveness = 0;
                            float loudness = 0;
                            int popularity = 0;
                            float speechiness = 0;
                            float valence = 0;
                            float tempo = 0;
                            String date = dates.get(track.id);
                            for (AudioFeaturesTrack feature: audioFeaturesTracks.audio_features) {
                                if(track.id.equals(feature.id)){
                                    tempo = feature.tempo;
                                    acousticness = feature.acousticness;
                                    danceability = feature.danceability;
                                    energy = feature.energy;
                                    instrumentalness = feature.instrumentalness;
                                    liveness = feature.liveness;
                                    loudness = feature.loudness;
                                    popularity = track.popularity;
                                    speechiness = feature.speechiness;
                                    valence = feature.valence;
                                }
                            }

                            playlist_duration += track.duration_ms;

                            Song song = new Song(newId,position,playlistId,trackId,spotifyId,
                                    genreId,name,track.duration_ms,tempo,artist,imageUri,previewUrl,
                                    acousticness,danceability,energy,instrumentalness,liveness,
                                    loudness,popularity,speechiness,valence,0,date);
                            songs.add(song);
                        }

                        playlist.setDuration((int) playlist_duration);
                        db.insertNewPlaylist(playlist,songs);
                        mResultPlaylist.showListPlaylist();
                        mResultNewPlaylist.dismissProgress();
                    }

                    @Override
                    public void onError(Throwable error) {
                        logError(error.getMessage());
                        mResultPlaylist.showError(error);
                    }
                };
                mSearchPager.getRecomendationPlaylist(playlist, SIZE, mSearchListener, 0);
            }
        }else if(mode == 1){
            final float tempo  = NewPlayList.getTempo();
            String genre = NewPlayList.getCategory();

            if (tempo != 0 && !genre.isEmpty()) {
                mTempo = tempo;
                mGenre = genre;

                mResultPlaylist.reset();
                mSearchListener = new SearchSpotify.CompleteListener() {
                    @Override
                    public void onComplete(List<Track> items, AudioFeaturesTracks audioFeaturesTracks, Map<String, String> dates) {
                        long playlist_duration = 0;
                        List<Song> songs = new ArrayList<Song>();
                        int i = 0;
                        for (Track track: items) {
                            UUID newUUID = java.util.UUID.randomUUID();
                            String newId = newUUID.toString();
                            //int position = db.getSongNextPosition(playlist.getId());
                            int position = i;
                            i += 1;
                            String playlistId = playlist.getId();
                            String trackId = track.id;
                            String spotifyId = track.uri;
                            String genreId = playlist.getGenre();
                            String name = track.name;
                            long duration = track.duration_ms;
                            String artist = track.artists.get(0).name;
                            String imageUri = track.album.images.get(0).url;
                            String previewUrl = track.preview_url;
                            float acousticness = 0;
                            float danceability = 0;
                            float energy = 0;
                            float instrumentalness = 0;
                            float liveness = 0;
                            float loudness = 0;
                            int popularity = 0;
                            float speechiness = 0;
                            float valence = 0;
                            float tempo = 0;
                            String date = dates.get(track.id);
                            for (AudioFeaturesTrack feature: audioFeaturesTracks.audio_features) {
                                if(track.id.equals(feature.id)){
                                    tempo = feature.tempo;
                                    acousticness = feature.acousticness;
                                    danceability = feature.danceability;
                                    energy = feature.energy;
                                    instrumentalness = feature.instrumentalness;
                                    liveness = feature.liveness;
                                    loudness = feature.loudness;
                                    popularity = track.popularity;
                                    speechiness = feature.speechiness;
                                    valence = feature.valence;
                                }
                            }

                            playlist_duration += track.duration_ms;

                            Song song = new Song(newId,position,playlistId,trackId,spotifyId,
                                    genreId,name,track.duration_ms,tempo,artist,imageUri,previewUrl,
                                    acousticness,danceability,energy,instrumentalness,liveness,
                                    loudness,popularity,speechiness,valence, 0,date);
                            songs.add(song);
                        }

                        db.insertSongsInPlaylist(playlist,songs);
                        mResultPlaylist.showListPlaylist();
                        mResultNewPlaylist.dismissProgress();
                    }

                    @Override
                    public void onError(Throwable error) {
                        logError(error.getMessage());
                        mResultPlaylist.showError(error);
                    }
                };
                mSearchPager.getRecomendationPlaylist(playlist, SIZE, mSearchListener, 1);
            }
        }
    }

    @Override
    public void search(String searchQuery, final float mTempoFilter) {
        //if (searchQuery != null && !searchQuery.isEmpty()) {
            logMessage("query text submit " + searchQuery);
            mCurrentQuery = searchQuery;
            //mView.reset();
            mManualSearchListener = new SearchSpotify.ManualSearchListener() {
                @Override
                public void onComplete(List<Track> items, AudioFeaturesTracks audioFeaturesTracks, Map<String, String> dates) {
                    // Hemos recibido la lista de canciones y sus features
                    List<Song> song_list = new ArrayList<Song>();
                    for (Track track: items) {
                        UUID newUUID = java.util.UUID.randomUUID();
                        String newId = newUUID.toString();
                        int position = -1;
                        String playlistId = "";
                        String trackId = track.id;
                        String spotifyId = track.uri;
                        String genreId = "";
                        String name = track.name;
                        long duration = track.duration_ms;
                        String artist = track.artists.get(0).name;
                        String imageUri = "";
                        if(track.album.images.get(0).url.equals("")){
                            imageUri = "";
                        }else{
                            imageUri = track.album.images.get(0).url;
                        }
                        String previewUrl = track.preview_url;
                        float acousticness = 0;
                        float danceability = 0;
                        float energy = 0;
                        float instrumentalness = 0;
                        float liveness = 0;
                        float loudness = 0;
                        int popularity = 0;
                        float speechiness = 0;
                        float valence = 0;
                        float tempo = 0;
                        String date = dates.get(track.id);
                        for (AudioFeaturesTrack feature: audioFeaturesTracks.audio_features) {
                            if(feature != null){
                                if(track.id.equals(feature.id)){
                                    tempo = feature.tempo;
                                    acousticness = feature.acousticness;
                                    danceability = feature.danceability;
                                    energy = feature.energy;
                                    instrumentalness = feature.instrumentalness;
                                    liveness = feature.liveness;
                                    loudness = feature.loudness;
                                    popularity = track.popularity;
                                    speechiness = feature.speechiness;
                                    valence = feature.valence;
                                }
                            }
                        }
                        Song song = new Song(newId, position, playlistId,trackId,spotifyId,
                                genreId,name,track.duration_ms,tempo,artist,imageUri,previewUrl,
                                acousticness,danceability,energy,instrumentalness,liveness,
                                loudness,popularity,speechiness,valence,0,date);
                        song_list.add(song);
                    }

                    // Antes de añadir, si hay filtro de BPM eliminamos aquellas canciones que no se acerquen al BPM
                    if(mTempoFilter > 0){
                        List<Song> temp_song = new ArrayList<Song>();
                        for (Song item: song_list) {
                            if(((item.getTempo() > mTempoFilter - 10.0) && (item.getTempo() < mTempoFilter + 10.0))){
                                temp_song.add(item);
                            }
                        }
                        mResultPlaylist.addDataToSearchList(temp_song);
                    }else{
                        mResultPlaylist.addDataToSearchList(song_list);
                    }
                }

                @Override
                public void onError(Throwable error) {
                    logError(error.getMessage());
                    mResultPlaylist.showError(error);
                }
            };
            mSearchPager.getFirstPage(searchQuery, PAGE_SIZE, mManualSearchListener);
        //}
    }

    @Override
    public void loadMoreResults() {
        Log.d(TAG, "Load more...");
        mSearchPager.getNextPage(mManualSearchListener);
    }

    @Override
    public String getRecordedQuery(String keyCurrentQuery) {
        return mCurrentQuery;
    }

    @Override
    public void exportToSpotify(String owner, AttPlaylist item) {
        mSearchPager.exportToSpotify(owner, item);
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
                mResultPlaylist.showError(error);
            }
        };
        mSearchPager.getGenres(mGenresListener);
    }

    @Override
    public void getUserData() {
        mUserDataListener = new SearchSpotify.UserDataListener(){

            @Override
            public void onComplete(UserPrivate user) {
                mResultUserData.setUserData(user);
            }

            @Override
            public void onError(Throwable error) {
                logError(error.getMessage());
                mResultPlaylist.showError(error);
            }
        };
        mSearchPager.getUserDataCall(mUserDataListener);
    }

    private void logError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Log.e(TAG, msg);
    }

    private void logMessage(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, msg);
    }

}
