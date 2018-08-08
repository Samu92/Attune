package es.app.attune.attune.Classes;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import es.app.attune.attune.Activity.MainActivity;
import es.app.attune.attune.Database.AttPlaylist;
import es.app.attune.attune.Database.Song;
import es.app.attune.attune.Fragments.AdvancedParameters;
import es.app.attune.attune.Fragments.NewPlayList;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Albums;
import kaaes.spotify.webapi.android.models.AudioFeaturesTrack;
import kaaes.spotify.webapi.android.models.AudioFeaturesTracks;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Recommendations;
import kaaes.spotify.webapi.android.models.SeedsGenres;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import kaaes.spotify.webapi.android.models.UserPrivate;
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
    private String mCurrentQuery;
    private int mPageSize;
    private String mYear_start;
    private String mYear_end;
    private Map<String,String> dates;
    private String mGenre;
    private int partition_step;
    private int steps_size;
    private float mAcousticness;
    private float mDanceability;
    private float mEnergy;
    private float mInstrumentalness;
    private float mLiveness;
    private float mLoudness;
    private int mPopularity;
    private float mSpeechiness;
    private float mValence;

    public interface CompleteListener {
        void onComplete(List<Track> items, AudioFeaturesTracks audioFeaturesTracks, Map<String, String> dates);
        void onError(Throwable error);
    }

    public interface GenresListener{
        void onComplete(List<String> items);
        void onError(Throwable error);
    }

    public interface  ManualSearchListener{
        void onComplete(List<Track> items, AudioFeaturesTracks audioFeaturesTracks, Map<String, String> dates);
        void onError(Throwable error);
    }

    public interface UserDataListener{
        void onComplete(UserPrivate user);
        void onError(Throwable error);
    }

    public SearchSpotify(SpotifyService spotifyApi) {
        mSpotifyApi = spotifyApi;
        dates = new HashMap<String,String>();
    }

    public void getRecomendationPlaylist(AttPlaylist playlist, int size, CompleteListener listener, int mode){
        if(mode == 0){
            mCurrentOffset = 0;
            mSize = size;
            mTempo = playlist.getTempo();
            mDuration = playlist.getDuration();
            mYear_start = playlist.getPlaylist_start_date();
            mYear_end = playlist.getPlaylist_end_date();
            mGenre = playlist.getGenre();
            mAcousticness = playlist.getAcousticness();
            mDanceability = playlist.getDanceability();
            mEnergy = playlist.getEnergy();
            mInstrumentalness = playlist.getInstrumentalness();
            mLiveness = playlist.getLiveness();
            mLoudness = playlist.getLoudness();
            mPopularity = playlist.getPopularity();
            mSpeechiness = playlist.getSpeechiness();
            mValence = playlist.getValence();
            getDataPlaylist(playlist,
                    0, size, listener);
        }else if (mode == 1){
            mCurrentOffset = 0;
            mSize = size;
            mTempo = NewPlayList.getTempo();
            mDuration = NewPlayList.getDuration();
            mYear_start = NewPlayList.getYearStart();
            mYear_end = NewPlayList.getYearEnd();
            mGenre = NewPlayList.getCategory();
            mAcousticness = AdvancedParameters.getAcousticness();
            mDanceability = AdvancedParameters.getDanceability();
            mEnergy = AdvancedParameters.getEnergy();
            mInstrumentalness = AdvancedParameters.getInstrumentalness();
            mLiveness = AdvancedParameters.getLiveness();
            mLoudness = AdvancedParameters.getLoudness();
            mPopularity = AdvancedParameters.getPopularity();
            mSpeechiness = AdvancedParameters.getSpeechiness();
            mValence = AdvancedParameters.getValence();
            getDataPlaylist(playlist,
                    0, size, listener);
        }
    }

    public void getGenres(final GenresListener listener){
        getDataGenres(listener);
    }

    public void getUserDataCall(final UserDataListener listener){
        getUserData(listener);
    }

    private void getDataPlaylist(AttPlaylist playlist,
                                 int offset, final int limit, final CompleteListener listener){
        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.OFFSET, offset);
        options.put(SpotifyService.LIMIT, limit);
        options.put(SpotifyService.MARKET, "ES");
        options.put("seed_genres",mGenre);
        options.put("target_tempo",mTempo);
        if(playlist.getSong_duration() > 0) options.put("target_duration_ms",((int) mDuration*60000));
        if(playlist.getAcousticness() != -1) options.put("target_acousticness",mAcousticness);
        if(playlist.getDanceability() != -1) options.put("target_danceability",mDanceability);
        if(playlist.getEnergy() != -1) options.put("target_energy",mEnergy);
        if(playlist.getInstrumentalness() != -1) options.put("target_instrumentalness", mInstrumentalness);
        if(playlist.getLiveness() != -1) options.put("target_liveness",mLiveness);
        if(playlist.getLoudness() != -1) options.put("target_loudness",mLoudness);
        if(playlist.getPopularity() != -1) options.put("target_popularity",mPopularity);
        if(playlist.getSpeechiness() != -1) options.put("target_speechiness",mSpeechiness);
        if(playlist.getValence() != -1) options.put("target_valence",mValence);

        mSpotifyApi.getRecommendations(options, new Callback<Recommendations>() {
            @Override
            public void success(Recommendations recommendations, Response response) {
                getAudioFeaturesTracks(recommendations.tracks,listener);
            }

            @Override
            public void failure(RetrofitError error) {
                listener.onError(error);
            }
        });
    }


    public void getFirstPage(String searchQuery, int pageSize, ManualSearchListener mSearchListener) {
        mCurrentOffset = 0;
        mPageSize = pageSize;
        mCurrentQuery = searchQuery;
        getSearchData(searchQuery, 0, pageSize, mSearchListener);
    }

    public void getNextPage(ManualSearchListener mSearchListener) {
        mCurrentOffset += mPageSize;
        getSearchData(mCurrentQuery, mCurrentOffset, mPageSize, mSearchListener);
    }

    private void getSearchData(String query, int offset, final int limit, final ManualSearchListener listener) {
        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.OFFSET, offset);
        options.put(SpotifyService.LIMIT, limit);

        mSpotifyApi.searchTracks(query, options, new SpotifyCallback<TracksPager>() {
            @Override
            public void success(TracksPager tracksPager, Response response) {
                // Hemos obtenido las canciones pero necesitamos consultar los bpm de cada una
                getAudioSearchFeaturesTracks(tracksPager.tracks.items,listener);
            }

            @Override
            public void failure(SpotifyError spotifyError) {
                listener.onError(spotifyError);
            }
        });
    }


    private void getUserData(final UserDataListener listener){
        mSpotifyApi.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, Response response) {
                listener.onComplete(userPrivate);
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

    private void getAudioFeaturesTracks(final List<Track> tracks, final CompleteListener listener){
        if(!tracks.isEmpty()) {
            // Hacemos un shuflle para añadir un factor de aletoriedad
            Collections.shuffle(tracks);
            String ids = "";
            for (Track track : tracks) {
                ids = ids.concat(track.id + ",");
            }
            mSpotifyApi.getTracksAudioFeatures(ids, new Callback<AudioFeaturesTracks>() {
                @Override
                public void success(AudioFeaturesTracks audioFeaturesTracks, Response response) {

                    // Eliminamos aquellas canciones que no cumplan el rango de BPM
                    for (AudioFeaturesTrack feature: audioFeaturesTracks.audio_features) {
                        if (!((feature.tempo > mTempo - 10.0) && (feature.tempo < mTempo + 10.0))){
                            for(int i = 0; i < tracks.size(); i++){
                                if(tracks.get(i).id.equals(feature.id)){
                                    tracks.remove(i);
                                }
                            }
                        }
                    }

                    // Como tenemos una duración fijada de la playlist no podemos pasarnos de ella
                    // seleccionaremos canciones hasta que se llegue al límite establecido en milisegundos.
                    /*List<Track> result = new ArrayList<>();
                    int temp_duration = 0;
                    for (Track track: tracks) {
                        if(temp_duration <= (mDuration*60000)){
                            temp_duration += track.duration_ms;
                            result.add(track);
                        }
                    }*/

                    //getAutomaticModeDates(result, audioFeaturesTracks, listener);
                    getAutomaticModeDates(tracks, audioFeaturesTracks, listener);
                }

                @Override
                public void failure(RetrofitError error) {
                    listener.onError(error);
                }
            });
        }
    }

    private void getAudioSearchFeaturesTracks(final List<Track> tracks, final ManualSearchListener listener){
        if(!tracks.isEmpty()) {
            String ids = "";
            for (Track track : tracks) {
                ids = ids.concat(track.id + ",");
            }
            mSpotifyApi.getTracksAudioFeatures(ids, new Callback<AudioFeaturesTracks>() {
                @Override
                public void success(AudioFeaturesTracks audioFeaturesTracks, Response response) {
                    getManualModeDates(tracks,audioFeaturesTracks,listener);
                }

                @Override
                public void failure(RetrofitError error) {
                    listener.onError(error);
                }
            });
        }else{
            listener.onComplete(tracks,null,null);
        }
    }

    private void getAutomaticModeDates(final List<Track> tracks, final AudioFeaturesTracks features, final CompleteListener listener){
        final List<Track> result = new ArrayList<Track>();

        List<String> ids = new ArrayList<String>();
        for (Track track : tracks) {
            ids.add(track.album.id);
        }

        int partitionSize = 20;
        List<List<String>> partitions = new LinkedList<List<String>>();
        for (int i = 0; i < ids.size(); i += partitionSize) {
            partitions.add(ids.subList(i,
                    Math.min(i + partitionSize, ids.size())));
        }

        partition_step = 0;
        steps_size = partitions.size() - 1;
        dates.clear();

        calculateDatesRecursive(tracks,partitions,features,listener);
    }

    private void calculateDatesRecursive(final List<Track> tracks, final List<List<String>> partitions, final AudioFeaturesTracks features, final CompleteListener listener) {
        if(partition_step <= steps_size) {
            String str_ids = "";
            for (String id : partitions.get(partition_step)) {
                str_ids = str_ids.concat(id + ",");
            }
            str_ids = str_ids.substring(0, str_ids.length() - 1);

            mSpotifyApi.getAlbums(str_ids, new Callback<Albums>() {
                @Override
                public void success(Albums albums, Response response) {
                    for(Track track: tracks){
                        if(albums.albums != null){
                            for(Album album: albums.albums){
                                if(track.album.id.equals(album.id)){
                                    dates.put(track.id,album.release_date);
                                }
                            }
                        }
                    }
                    partition_step += 1;
                    calculateDatesRecursive(tracks,partitions,features,listener);
                }

                @Override
                public void failure(RetrofitError error) {
                    listener.onError(error);
                }
            });
        }else{
            // Eliminamos aquellas canciones que no están dentro de los años indicados
            List<Track> result_temp = new ArrayList<>();
            Map<String,String> dates_temp = new HashMap<String,String>();
            dates_temp.putAll(dates);

            result_temp.addAll(tracks);
            if(mYear_start != "" & mYear_end != ""){
                for (Track track: tracks) {
                    for (Map.Entry<String,String> entry: dates.entrySet()) {
                        if(entry.getKey() == track.id){
                            Date release = null;
                            try {
                                release = new SimpleDateFormat("yyyy").parse(entry.getValue());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(release);
                            String release_year = Integer.valueOf(cal.get(Calendar.YEAR)).toString();
                            if(!(Integer.valueOf(release_year) >= Integer.valueOf(mYear_start) & Integer.valueOf(release_year) <= Integer.valueOf(mYear_end))) {
                                result_temp.remove(track);
                                dates_temp.remove(entry.getKey());
                            }
                        }
                    }
                }
            }

            // Como tenemos una duración fijada de la playlist no podemos pasarnos de ella
            // seleccionaremos canciones hasta que se llegue al límite establecido en milisegundos.
            List<Track> result_finish = new ArrayList<>();
            int temp_duration = 0;
            for (Track track: result_temp) {
                if(temp_duration <= (mDuration*60000)){
                    temp_duration += track.duration_ms;
                    result_finish.add(track);
                }
            }
            listener.onComplete(result_finish,features,dates_temp);
        }
    }

    public void exportToSpotify(final String owner, final AttPlaylist item) {
        Map<String, Object> options = new HashMap<>();
        options.put("name",item.getName());
        options.put("public", false);
        mSpotifyApi.createPlaylist(owner, options, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                List<Song> songs = item.getSongs();
                String songs_temp = "";
                for (Song it: songs) {
                    songs_temp += "spotify:track:" + it.getIdSpotify() + ",";
                }
                Map<String, Object> options1 = new HashMap<>();
                options1.put("uris",songs_temp);
                Map<String, Object> options2 = new HashMap<>();
                options2.put("uris",songs_temp);
                mSpotifyApi.addTracksToPlaylist(owner, playlist.id, options1, options2, new Callback<Pager<PlaylistTrack>>() {
                    @Override
                    public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                        MainActivity.createPlaylistSuccess();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("Export", error.getResponse().toString());
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Export", error.getResponse().toString());
            }
        });
    }

    private void getManualModeDates(final List<Track> tracks, final AudioFeaturesTracks features, final ManualSearchListener listener){
        String ids = "";
        for (Track track : tracks) {
            ids = ids.concat(track.album.id + ",");
        }
        ids = ids.substring(0,ids.length()-1);
        mSpotifyApi.getAlbums(ids, new Callback<Albums>() {
            @Override
            public void success(Albums albums, Response response) {
                Map<String,String> dates = new HashMap<String,String>();
                for (Track track: tracks) {
                    if(albums.albums != null){
                        for (Album album: albums.albums){
                            if(album != null){
                                if(track.album.id.equals(album.id)){
                                    dates.put(track.id,album.release_date);
                                }
                            }
                        }
                    }
                }

                listener.onComplete(tracks, features, dates);
            }

            @Override
            public void failure(RetrofitError error) {
                listener.onError(error);
            }
        });
    }
}
