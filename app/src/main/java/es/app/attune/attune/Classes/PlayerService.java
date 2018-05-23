package es.app.attune.attune.Classes;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import es.app.attune.attune.Interfaces.Player;

/**
 * Created by Samuel on 08/03/2018.
 */

public class PlayerService extends Service {
    private final IBinder mBinder = new PlayerBinder();
    private SpotifyPlayer mPlayer = new SpotifyPlayer();

    public static Intent getIntent(Context context) {
        return new Intent(context, PlayerService.class);
    }

    public class PlayerBinder extends Binder {
        public Player getService() {
            return mPlayer;
        }
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
