package es.app.attune.attune.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import es.app.attune.attune.R;

public class Constants {
    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.mipmap.ic_launcher, options);
        } catch (Error | Exception ignored) {
        }
        return bm;
    }

    public interface ACTION {
        String MAIN_ACTION = "es.attune.customnotification.action.main";
        String INIT_ACTION = "es.attune.customnotification.action.init";
        String PREV_ACTION = "es.attune.customnotification.action.prev";
        String PLAY_ACTION = "es.attune.customnotification.action.play";
        String NEXT_ACTION = "es.attune.customnotification.action.next";
        String STARTFOREGROUND_ACTION = "es.attune.customnotification.action.startforeground";
        String STOPFOREGROUND_ACTION = "es.attune.customnotification.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }

}