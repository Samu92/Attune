package es.app.attune.attune.attunePlayer;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.app.attune.attune.activity.MainActivity;
import es.app.attune.attune.classes.App;
import es.app.attune.attune.classes.CredentialsHandler;
import es.app.attune.attune.database.AttPlaylist;
import es.app.attune.attune.database.Song;

/**
 * Created by Samuel on 08/03/2018.
 */

public class AttunePlayer implements Player, com.spotify.sdk.android.player.SpotifyPlayer.NotificationCallback, ConnectionStateCallback {
    private static final String TAG = AttunePlayer.class.getSimpleName();

    private com.spotify.sdk.android.player.SpotifyPlayer mSpotifyPlayer;
    private int mCurrentSong;
    private static final String CLIENT_ID = "8bcf4a1c62f64325a456b1bee9e857d9";
    private List<Song> currentSongs;
    private boolean repetitionState;
    private String token;

    AttunePlayer() {
        currentSongs = new ArrayList<Song>();
        repetitionState = false;
    }

    @Override
    public void play(Song item) {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn()) {
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
            } else {
                currentSongs.clear();
                currentSongs.add(item);
                Log.i("SongPlay", "Playing " + item.getUrlSpotify());
                mSpotifyPlayer.playUri(null, item.getUrlSpotify(), 0, 0);
                mCurrentSong = 0;
            }
        }
    }

    @Override
    public void setQueue(AttPlaylist item) {
        if (mSpotifyPlayer != null) {
            Log.i("PlayPlaylist", "Spotify player is not null...");
            if (!mSpotifyPlayer.isLoggedIn()) {
                Log.i("PlayPlaylist", "Spotify player is not logged...");
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
            } else {
                Log.i("PlayPlaylist", "Spotify player is logged...");
                currentSongs.clear();
                currentSongs.addAll(item.getSongs());
                Collections.sort(currentSongs, new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {

                        if (o1.getPosition() > o2.getPosition()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
                if (currentSongs.size() > 0) {
                    Log.i("PlaylistPlay", "Playing " + currentSongs.get(0).getUrlSpotify());
                    mSpotifyPlayer.playUri(null, currentSongs.get(0).getUrlSpotify(), 0, 0);
                    mCurrentSong = 0;
                }
            }
        }
    }

    @Override
    public void setQueue(AttPlaylist item, Song song) {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn()) {
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
            } else {
                currentSongs.clear();
                currentSongs.addAll(item.getSongs());
                Collections.sort(currentSongs, new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {

                        if (o1.getPosition() > o2.getPosition()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
                int currentPosition = song.getPosition();
                if (currentSongs.size() > 0) {
                    Log.i("PlaylistPlay", "Playing " + currentSongs.get(0).getUrlSpotify());
                    mSpotifyPlayer.playUri(null, currentSongs.get(currentPosition).getUrlSpotify(), 0, 0);
                    mCurrentSong = currentPosition;
                }
            }
        }
    }

    public void skipToPreviousSong() {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn()) {
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
            } else {
                if (mCurrentSong != 0) {
                    mCurrentSong -= 1;
                    mSpotifyPlayer.playUri(null, currentSongs.get(mCurrentSong).getUrlSpotify(), 0, 0);
                }
            }
        }
    }

    public void skipToNextSong() {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn()) {
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
            } else {
                if (currentSongs.size() > 1) {
                    if (mCurrentSong < currentSongs.size() - 1) {
                        mCurrentSong += 1;
                        mSpotifyPlayer.playUri(null, currentSongs.get(mCurrentSong).getUrlSpotify(), 0, 0);
                    } else if (repetitionState) {
                        mCurrentSong = 0;
                        mSpotifyPlayer.playUri(null, currentSongs.get(mCurrentSong).getUrlSpotify(), 0, 0);
                    }
                } else if (repetitionState) {
                    mSpotifyPlayer.playUri(null, currentSongs.get(mCurrentSong).getUrlSpotify(), 0, 0);
                }
            }
        }
    }

    @Override
    public void pause() {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn()) {
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
            } else {
                Log.d(TAG, "Pause");
                mSpotifyPlayer.pause(null);
            }
        }
    }

    @Override
    public void release() {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn()) {
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
            } else {
                mCurrentSong = 0;
                mSpotifyPlayer.shutdown();
            }
        }
    }

    @Override
    public void resume() {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn()) {
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
            } else {
                Log.d(TAG, "Resume");
                mSpotifyPlayer.resume(null);
            }
        }
    }

    @Override
    public boolean isPlaying() {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn()) {
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
                return false;
            } else {
                return mSpotifyPlayer.getPlaybackState().isPlaying;
            }
        } else {
            return false;
        }
    }

    @Override
    public int getCurrentTrack() {
        return mCurrentSong;
    }

    @Override
    public void createMediaPlayer(final String token, final Context context) {
        try {
            // Init Player
            Config playerConfig;
            playerConfig = new Config(context, token, CLIENT_ID);
            Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                @Override
                public void onInitialized(com.spotify.sdk.android.player.SpotifyPlayer spotifyPlayer) {
                    Log.i("CreateMediaPlayer", "Player initialized");
                    mSpotifyPlayer = spotifyPlayer;
                    mSpotifyPlayer.addConnectionStateCallback(AttunePlayer.this);
                    mSpotifyPlayer.addNotificationCallback(AttunePlayer.this);
                    MainActivity.disablePlayerState();
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.e("AttunePlayer", "Could not initialize player: " + throwable.getMessage());
                }
            });
        } catch (Exception ex) {
            Log.e("AttunePlayer", "Could not initialize player: " + ex.getMessage());
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("PlayBackEvent","");
        MainActivity.enablePlayerStatus();
    }

    @Override
    public void onLoggedOut() {
        Log.d("PlayBackEvent","");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("PlayBackEvent","");
    }

    @Override
    public void onTemporaryError() {
        Log.e("TemporaryError", "");
    }

    @Override
    public void onConnectionMessage(String s) {
        Log.d("PlayBackEvent","");
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyBecameActive)){
            MainActivity.play();
            MainActivity.enableSlidingPanel();
        }

        if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyBecameInactive)){
            MainActivity.pause();
            MainActivity.disableSlidingPanel();
        }

        if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyPause)){
            MainActivity.pause();
        }

        if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyPlay)){
            MainActivity.play();
            MainActivity.notifyPlayer(currentSongs.get(mCurrentSong));
            MainActivity.initializeProgressCircle(currentSongs.get(mCurrentSong));
            MainActivity.showPlayer();
        }

        if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyMetadataChanged)){
            MainActivity.notifyPlayer(currentSongs.get(mCurrentSong));
        }

        if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyTrackChanged)){
            if (mSpotifyPlayer != null) {
                if (!mSpotifyPlayer.isLoggedIn()) {
                    mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
                } else {
                    if ((mCurrentSong < currentSongs.size() - 1) && !mSpotifyPlayer.getPlaybackState().isPlaying) {
                        mCurrentSong += 1;
                        mSpotifyPlayer.playUri(null, currentSongs.get(mCurrentSong).getUrlSpotify(), 0, 0);
                    } else if ((mCurrentSong == currentSongs.size() - 1) && repetitionState && !mSpotifyPlayer.getPlaybackState().isPlaying) {
                        mCurrentSong = 0;
                        mSpotifyPlayer.playUri(null, currentSongs.get(mCurrentSong).getUrlSpotify(), 0, 0);
                    }
                }
            }
        }

        if (playerEvent.equals(PlayerEvent.kSpPlaybackNotifyLostPermission)) {
            MainActivity.LostPermissionMessage();
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("PlayBackEvent","");
        Toast.makeText(App.getContext(), error.name(), Toast.LENGTH_SHORT).show();
    }

    public boolean isInitialized(){
        return mSpotifyPlayer != null;
    }

    public long getPositionTrack(){
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn()) {
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
                return 0;
            } else {
                return mSpotifyPlayer.getPlaybackState().positionMs;
            }
        } else {
            return 0;
        }
    }

    public Song getCurrentSong() {
        if(!currentSongs.isEmpty()){
            return currentSongs.get(mCurrentSong);
        }else{
            return null;
        }
    }

    public Song getLastSong() {
        if(!currentSongs.isEmpty()){
            return  currentSongs.get(currentSongs.size() - 1);
        }else{
            return null;
        }
    }

    public PlaybackState getPlaybackState() {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn()) {
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
                return null;
            } else {
                return mSpotifyPlayer.getPlaybackState();
            }
        } else {
            return null;
        }
    }

    public boolean currentsSongEmpty() {
        return currentSongs.isEmpty();
    }

    public void seekToPosition(int progress) {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn()) {
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
            } else {
                long total = getCurrentSong().getDuration();
                long ms = (progress * 1000);
                mSpotifyPlayer.seekToPosition(null, Math.abs((int) ms));
            }
        }
    }

    public boolean getRepetition() {
        return repetitionState;
    }

    public void setRepetitionState(boolean state){
        repetitionState = state;
    }

    public void login(String newToken) {
        if (mSpotifyPlayer != null) {
            token = newToken;
            mSpotifyPlayer.login(token);
        }
    }

    public boolean isLogged() {
        return mSpotifyPlayer.isLoggedIn();
    }

    public void logout() {
        mSpotifyPlayer.logout();
    }
}
