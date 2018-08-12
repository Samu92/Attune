package es.app.attune.attune.AttunePlayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.app.attune.attune.Activity.MainActivity;
import es.app.attune.attune.Classes.App;
import es.app.attune.attune.Classes.CredentialsHandler;
import es.app.attune.attune.Database.AttPlaylist;
import es.app.attune.attune.Database.Song;

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

    AttunePlayer() {
        currentSongs = new ArrayList<Song>();
        Intent switchIntent = new Intent("es.app.attune.ACTION_PLAY");
        repetitionState = false;
    }

    @Override
    public void play(Song item) {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn())
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
            currentSongs.clear();
            currentSongs.add(item);
            mSpotifyPlayer.playUri(null, item.getUrlSpotify(), 0, 0);
            mCurrentSong = 0;
        }
    }

    @Override
    public void setQueue(AttPlaylist item) {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn())
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
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
            mSpotifyPlayer.playUri(null, currentSongs.get(0).getUrlSpotify(), 0, 0);
            mCurrentSong = 0;
        }
    }

    @Override
    public void setQueue(AttPlaylist item, Song song) {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn())
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
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
            mSpotifyPlayer.playUri(null, currentSongs.get(currentPosition).getUrlSpotify(), 0, 0);
            mCurrentSong = currentPosition;
        }
    }

    public void skipToPreviousSong() {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn())
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
            if (mCurrentSong != 0) {
                mCurrentSong -= 1;
                mSpotifyPlayer.playUri(null, currentSongs.get(mCurrentSong).getUrlSpotify(), 0, 0);
            }
        }
    }

    public void skipToNextSong() {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn())
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
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

    @Override
    public void pause() {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn())
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
            Log.d(TAG, "Pause");
            mSpotifyPlayer.pause(null);
        }
    }

    @Override
    public void release() {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn())
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
            mCurrentSong = 0;
            mSpotifyPlayer.shutdown();
        }
    }

    @Override
    public void resume() {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn())
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
            Log.d(TAG, "Resume");
            mSpotifyPlayer.resume(null);
        }
    }

    @Override
    public boolean isPlaying() {
        if (mSpotifyPlayer != null) {
            if (!mSpotifyPlayer.isLoggedIn())
                mSpotifyPlayer.login(CredentialsHandler.getToken(App.getContext()));
            return mSpotifyPlayer.getPlaybackState().isPlaying;
        } else {
            return false;
        }
    }

    @Override
    public int getCurrentTrack() {
        return mCurrentSong;
    }

    @Override
    public void createMediaPlayer(String token, final Context context){
        // Init Player
        Config playerConfig;
        playerConfig = new Config(context, token, CLIENT_ID);
        Spotify.getPlayer(playerConfig, this, new com.spotify.sdk.android.player.SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(com.spotify.sdk.android.player.SpotifyPlayer spotifyPlayer) {
                mSpotifyPlayer = spotifyPlayer;
                mSpotifyPlayer.addConnectionStateCallback(AttunePlayer.this);
                mSpotifyPlayer.addNotificationCallback(AttunePlayer.this);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
            }
        });
    }

    @Override
    public void onLoggedIn() {
        Log.d("PlayBackEvent","");
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

        if(playerEvent.equals(PlayerEvent.kSpPlaybackEventAudioFlush)){

        }

        if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyTrackChanged)){
            if (mSpotifyPlayer != null) {
                if ((mCurrentSong < currentSongs.size() - 1) && !mSpotifyPlayer.getPlaybackState().isPlaying) {
                    mCurrentSong += 1;
                    mSpotifyPlayer.playUri(null, currentSongs.get(mCurrentSong).getUrlSpotify(), 0, 0);
                } else if ((mCurrentSong == currentSongs.size() - 1) && repetitionState && !mSpotifyPlayer.getPlaybackState().isPlaying) {
                    mCurrentSong = 0;
                    mSpotifyPlayer.playUri(null, currentSongs.get(mCurrentSong).getUrlSpotify(), 0, 0);
                }
            }
        }

        if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyTrackDelivered)){

        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("PlayBackEvent","");
    }

    public boolean isInitialized(){
        return mSpotifyPlayer != null;
    }

    public long getPositionTrack(){
        if (mSpotifyPlayer != null) {
            return mSpotifyPlayer.getPlaybackState().positionMs;
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
            return mSpotifyPlayer.getPlaybackState();
        } else {
            return null;
        }
    }

    public boolean currentsSongEmpty() {
        return currentSongs.isEmpty();
    }

    public void seekToPosition(int progress) {
        if (mSpotifyPlayer != null) {
            long total = getCurrentSong().getDuration();
            long ms = (progress * 1000);
            mSpotifyPlayer.seekToPosition(null, Math.abs((int) ms));
        }
    }

    public boolean getRepetition() {
        return repetitionState;
    }

    public void setRepetitionState(boolean state){
        repetitionState = state;
    }

    public void logout() {
        if (mSpotifyPlayer != null) {
            mSpotifyPlayer.logout();
        }
    }
}
