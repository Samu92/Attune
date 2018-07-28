package es.app.attune.attune.AttunePlayer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;

import java.util.ArrayList;
import java.util.List;

import es.app.attune.attune.Activity.MainActivity;
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

    public AttunePlayer() {
        currentSongs = new ArrayList<Song>();
    }

    @Override
    public void play(Song item) {
        currentSongs.clear();
        currentSongs.add(item);
        mSpotifyPlayer.playUri(null,item.getUrlSpotify(),0,0);
        mCurrentSong = 0;
    }

    @Override
    public void setQueue(AttPlaylist item) {
        currentSongs.clear();
        currentSongs.addAll(item.getSongs());
        mSpotifyPlayer.playUri(null, currentSongs.get(0).getUrlSpotify(),0,0);
        mCurrentSong = 0;
    }

    public void skipToPreviousSong() {
        if(mCurrentSong != 0){
            mCurrentSong -= 1;
            mSpotifyPlayer.playUri(null, currentSongs.get(mCurrentSong).getUrlSpotify(),0,0);
        }
    }

    public void skipToNextSong() {
        if(currentSongs.size() > 1){
            if(mCurrentSong < currentSongs.size() - 1){
                mCurrentSong += 1;
                mSpotifyPlayer.playUri(null, currentSongs.get(mCurrentSong).getUrlSpotify(),0,0);
            }else{
                mCurrentSong = 0;
                mSpotifyPlayer.playUri(null, currentSongs.get(mCurrentSong).getUrlSpotify(),0,0);
            }
        }
    }

    @Override
    public void pause() {
        Log.d(TAG, "Pause");
        mSpotifyPlayer.pause(null);
    }

    @Override
    public void release() {
        mCurrentSong = 0;
        mSpotifyPlayer.shutdown();
    }

    @Override
    public void resume() {
        Log.d(TAG, "Resume");
        mSpotifyPlayer.resume(null);
    }

    @Override
    public boolean isPlaying() {
        return mSpotifyPlayer.getPlaybackState().isPlaying;
    }

    @Override
    @Nullable
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

    }

    @Override
    public void onConnectionMessage(String s) {
        Log.d("PlayBackEvent","");
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyBecameActive)){
            MainActivity.play();
        }

        if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyBecameInactive)){
            MainActivity.pause();
        }

        if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyPause)){
            MainActivity.pause();
            MainActivity.closePanel();
        }

        if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyPlay)){
            MainActivity.play();
            MainActivity.notifyPlayer(currentSongs.get(mCurrentSong));
            MainActivity.openPanel();
        }

        if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyMetadataChanged)){
            MainActivity.notifyPlayer(currentSongs.get(mCurrentSong));
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("PlayBackEvent","");
    }
}
