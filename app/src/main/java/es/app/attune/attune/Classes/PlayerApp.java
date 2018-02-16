package es.app.attune.attune.Classes;

import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.Player;

import es.app.attune.attune.Activity.MainActivity;

/**
 * Created by Samuel on 30/01/2018.
 */

public class PlayerApp {
    private Player mPlayer;

    public PlayerApp(){

    }

    public PlayerApp(Player mPlayer) {
        this.mPlayer = mPlayer;
    }

    public Player getmPlayer() {
        return mPlayer;
    }

    public void setmPlayer(Player mPlayer) {
        this.mPlayer = mPlayer;
    }

    public void addConnectionStateCallback(MainActivity activity) {
        this.mPlayer.addConnectionStateCallback(activity);
    }

    public void addNotificationCallback(MainActivity activity) {
        this.mPlayer.addNotificationCallback(activity);
    }

    public void play_song(){
        this.mPlayer.playUri(null, "spotify:track:2TpxZ7JUBn3uw46aR7qd6V", 0, 0);
    }
}
