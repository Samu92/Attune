package es.app.attune.attune.AttunePlayer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;

/**
 * Created by Samuel on 08/03/2018.
 */

public class AttunePlayer implements Player, com.spotify.sdk.android.player.SpotifyPlayer.NotificationCallback, ConnectionStateCallback {
    private static final String TAG = AttunePlayer.class.getSimpleName();

    private com.spotify.sdk.android.player.SpotifyPlayer mSpotifyPlayer;
    private String mCurrentTrack;

    private static final String CLIENT_ID = "8bcf4a1c62f64325a456b1bee9e857d9";

    public AttunePlayer() {

    }

    @Override
    public void play(String url) {
        mSpotifyPlayer.playUri(null,url,0,0);
        mCurrentTrack = url;
    }

    @Override
    public void pause() {
        Log.d(TAG, "Pause");
        mSpotifyPlayer.pause(null);
    }

    @Override
    public void release() {
        mCurrentTrack = null;
        mSpotifyPlayer.shutdown();
    }

    @Override
    public void resume() {
        Log.d(TAG, "Resume");
        mSpotifyPlayer.resume(null);
    }

    @Override
    public boolean isPlaying() {
        return mSpotifyPlayer.isShutdown();
    }

    @Override
    @Nullable
    public String getCurrentTrack() {
        return mCurrentTrack;
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

    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(Error error) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {

    }

    @Override
    public void onPlaybackError(Error error) {

    }
}
