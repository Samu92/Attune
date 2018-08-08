package es.app.attune.attune.AttunePlayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.spotify.sdk.android.player.PlaybackState;

import es.app.attune.attune.Activity.LoginActivity;
import es.app.attune.attune.Activity.MainActivity;
import es.app.attune.attune.Classes.App;
import es.app.attune.attune.Classes.Constants;
import es.app.attune.attune.Classes.CredentialsHandler;
import es.app.attune.attune.Database.AttPlaylist;
import es.app.attune.attune.Database.Song;
import es.app.attune.attune.R;

import static es.app.attune.attune.Activity.MainActivity.isOnline;

public class PlayerService extends Service {

    Notification status;
    private static final String TAG = "PlayerService";
    private final IBinder mBinder = new PlayerBinder();
    private AttunePlayer mPlayer = new AttunePlayer();
    private RemoteViews bigViews;
    private RemoteViews views;
    private AudioManager audio;
    private boolean doingEffect;

    public static Intent getIntent(Context context) {
        return new Intent(context, PlayerService.class);
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class PlayerBinder extends Binder {
        public PlayerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return PlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        doingEffect = false;
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(null == intent) {
            String source = null == intent ? "intent" : "action";
            Log.e(TAG, source + " was null, flags=" + flags + " bits=" + Integer.toBinaryString(flags));
        }else{
            if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {

            } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
                Log.i(TAG, "Clicked Previous");
                skipToPreviousSong();
            } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
                Log.i(TAG, "Clicked Play");
                if(mPlayer.isPlaying()){
                    mPlayer.pause();
                    views.setImageViewResource(R.id.status_bar_play,
                            R.drawable.ic_play_arrow_white_36dp);
                    bigViews.setImageViewResource(R.id.status_bar_play,
                            R.drawable.ic_play_arrow_white_36dp);
                }else{
                    mPlayer.resume();
                    views.setImageViewResource(R.id.status_bar_play,
                            R.drawable.ic_pause_black_36dp);
                    bigViews.setImageViewResource(R.id.status_bar_play,
                            R.drawable.ic_pause_black_36dp);
                }

                status = new Notification.Builder(App.getContext()).build();
                status.contentView = views;
                status.bigContentView = bigViews;
                status.flags = Notification.FLAG_ONGOING_EVENT;
                status.icon = R.mipmap.ic_launcher;
                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);

            } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
                Log.i(TAG, "Clicked Next");
                skipToNextSong();
            } else if (intent.getAction().equals(
                    Constants.ACTION.STOPFOREGROUND_ACTION)) {
                Log.i(TAG, "Received Stop Foreground Intent");
                stopForeground(true);
                mPlayer.pause();
            } else if(intent.getAction().equals(Constants.ACTION.MAIN_ACTION)){
                if(CredentialsHandler.getToken(getApplicationContext()) != null ){
                    mPlayer.createMediaPlayer(CredentialsHandler.getToken(getApplicationContext()),PlayerService.this);
                    MainActivity.showPlayer();
                }
            }
        }
        return START_STICKY;
    }

    private void showNotification(final Song item) {
        // Using RemoteViews to bind custom layouts into Notification
        views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);

        // showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);

        Intent notificationIntent = new Intent(this, LoginActivity.class);
        notificationIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, PlayerService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, PlayerService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, PlayerService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, PlayerService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setImageViewResource(R.id.status_bar_play,
                R.drawable.ic_pause_black_36dp);
        bigViews.setImageViewResource(R.id.status_bar_play,
                R.drawable.ic_pause_black_36dp);

        views.setTextViewText(R.id.status_bar_track_name, item.getName());
        bigViews.setTextViewText(R.id.status_bar_track_name, item.getName());

        views.setTextViewText(R.id.status_bar_artist_name, item.getArtist());
        bigViews.setTextViewText(R.id.status_bar_artist_name, item.getArtist());

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Glide.with(App.getContext())
                        .asBitmap()
                        .load(item.getImage())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                bigViews.setImageViewBitmap(R.id.status_bar_album_art, resource);
                                views.setImageViewBitmap(R.id.status_bar_album_art,resource);
                                views.setImageViewBitmap(R.id.status_bar_icon,resource);
                                status = new Notification.Builder(App.getContext()).build();
                                status.contentView = views;
                                status.bigContentView = bigViews;
                                status.flags = Notification.FLAG_ONGOING_EVENT;
                                status.icon = R.mipmap.ic_launcher;
                                status.contentIntent = pendingIntent;
                                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
                            }
                        });
            }
        });
    }

    public boolean isInitialized() {
        return mPlayer.isInitialized();
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public void playSong(Song item){
        if(MainActivity.isAuthorized()){
            mPlayer.play(item);
            showNotification(item);
        }
    }

    public void playPlaylist(AttPlaylist item) {
        if(MainActivity.isAuthorized()){
            mPlayer.setQueue(item);
            showNotification(getCurrentSong());
        }
    }

    public void playSongFromPlaylist(AttPlaylist selected_playlist, Song item) {
        if(MainActivity.isAuthorized()){
            mPlayer.setQueue(selected_playlist,item);
            showNotification(getCurrentSong());
        }
    }


    public boolean playPauseState(){
        if(MainActivity.isAuthorized()) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                return false;
            } else {
                mPlayer.resume();
                return true;
            }
        }else{
            return false;
        }
    }

    public long getTrackPosition() {
        return mPlayer.getPositionTrack();
    }

    public Song getCurrentTrack() {
        return mPlayer.getCurrentSong();
    }

    public void seekToPosition(int progress) {
        mPlayer.seekToPosition(progress);
    }

    public void skipToPreviousSong() {
        if(isOnline(App.getContext())){
            mPlayer.skipToPreviousSong();
            showNotification(getCurrentSong());
        }
    }

    public void skipToNextSong() {
        if(isOnline(App.getContext())){
            mPlayer.skipToNextSong();
            showNotification(getCurrentSong());
        }
    }

    public PlaybackState getPlaybackState() {
       return  mPlayer.getPlaybackState();
    }

    public boolean currentsSongEmpty() {
        return mPlayer.currentsSongEmpty();
    }

    private boolean isLastSong() {
        Song lastSong = mPlayer.getLastSong();
        if(getCurrentSong().equals(lastSong)){
            return true;
        }else{
            return false;
        }
    }


    public Song getCurrentSong() {
        return mPlayer.getCurrentSong();
    }

    public void doEffect(int effect_type) {
        if(mPlayer.isPlaying()){
            if(effect_type == 1){
                if(!doingEffect){
                    doingEffect = true;
                    crossfade(2000);
                }
            }else if(effect_type == 2){
                if(!doingEffect){
                    doingEffect = true;
                    overlap();
                }
            }
        }
    }

    private void overlap() {
        skipToNextSong();
        doingEffect = false;
    }

    private void crossfade(final int duration) {
        // Iniciamos fadeOut
        final int deviceVolume = getDeviceVolume();
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            private int time = duration;
            private int volume = 0;

            @Override
            public void run() {
                // can call h again after work!
                time -= 100;
                volume = (deviceVolume * time) / duration;
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, volume,0);
                if (time > 0)
                    h.postDelayed(this, 100);
                else{
                    if(!isLastSong()){
                        skipToNextSong();
                        fadeIn(2000, deviceVolume);
                    }else{
                        playPauseState();
                        doingEffect = false;
                        audio.setStreamVolume(AudioManager.STREAM_MUSIC, deviceVolume,0);
                        mPlayer.seekToPosition(0);
                    }
                }
            }
        }, 100); // 1 second delay (takes millis)
    }

    private void fadeIn(final int duration, int previousVolume) {
        final int deviceVolume = previousVolume;
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            private int time = 0;
            private int volume = 0;

            @Override
            public void run() {
                time += 100;
                volume = (deviceVolume * time) / duration;
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, volume,0);
                if (time < duration)
                    h.postDelayed(this, 100);
                else
                    doingEffect = false;
            }
        }, 100); // 1 second delay (takes millis)
    }

    public int getDeviceVolume() {
        int volumeLevel = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        return volumeLevel;
    }

    public int getDeviceMaxVolume(){
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return maxVolume;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onDestroy() {
        mPlayer.release();
        super.onDestroy();
    }
}
