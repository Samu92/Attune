package es.app.attune.attune.AttunePlayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class PlayerService extends Service {

    private static final String TAG = "PlayerService";
    private final IBinder mBinder = new PlayerBinder();
    private AttunePlayer mPlayer = new AttunePlayer();

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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(null == intent){
            String source = null == intent ? "intent" : "action";
            Log.e (TAG, source + " was null, flags=" + flags + " bits=" + Integer.toBinaryString (flags));
        }else{
            mPlayer.createMediaPlayer(intent.getExtras().getString("token"),PlayerService.this);
        }
        return START_STICKY;
    }

    public void playSong(String url){
        mPlayer.play(url);
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
