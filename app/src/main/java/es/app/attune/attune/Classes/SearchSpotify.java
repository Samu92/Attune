package es.app.attune.attune.Classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.app.attune.attune.Database.AttPlaylist;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AudioFeaturesTrack;
import kaaes.spotify.webapi.android.models.AudioFeaturesTracks;
import kaaes.spotify.webapi.android.models.Recommendations;
import kaaes.spotify.webapi.android.models.SeedsGenres;
import kaaes.spotify.webapi.android.models.Track;
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


    public interface CompleteListener {
        void onComplete(List<Track> items, AudioFeaturesTracks audioFeaturesTracks);
        void onError(Throwable error);
    }

    public interface GenresListener{
        void onComplete(List<String> items);
        void onError(Throwable error);
    }

    public interface UserDataListener{
        void onComplete(UserPrivate user);
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
        getDataPlaylist(playlist,
                0, size, listener);
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
        options.put("seed_genres",playlist.getGenre());
        options.put("target_tempo",playlist.getTempo());
        if(playlist.getSong_duration() > 0) options.put("target_duration_ms",((int) playlist.getSong_duration()*60000));
        if(playlist.getAcousticness() != -1) options.put("target_acousticness",playlist.getAcousticness());
        if(playlist.getDanceability() != -1) options.put("target_danceability",playlist.getDanceability());
        if(playlist.getEnergy() != -1) options.put("target_energy",playlist.getEnergy());
        if(playlist.getInstrumentalness() != -1) options.put("target_instrumentalness", playlist.getInstrumentalness());
        if(playlist.getLiveness() != -1) options.put("target_liveness",playlist.getLiveness());
        if(playlist.getLoudness() != -1) options.put("target_loudness",playlist.getLoudness());
        if(playlist.getPopularity() != -1) options.put("target_popularity",playlist.getPopularity());
        if(playlist.getSpeechiness() != -1) options.put("target_speechiness",playlist.getSpeechiness());
        if(playlist.getValence() != -1) options.put("target_valence",playlist.getValence());

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
                    List<Track> result = new ArrayList<>();
                    int temp_duration = 0;
                    for (Track track: tracks) {
                        if(temp_duration <= (mDuration*60000)){
                            temp_duration += track.duration_ms;
                            result.add(track);
                        }
                    }

                    listener.onComplete(result, audioFeaturesTracks);
                }

                @Override
                public void failure(RetrofitError error) {
                    listener.onError(error);
                }
            });
        }
    }
}
