package es.app.attune.attune.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import es.app.attune.attune.R;

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "es.attune.customnotification.action.main";
        public static String INIT_ACTION = "es.attune.customnotification.action.init";
        public static String PREV_ACTION = "es.attune.customnotification.action.prev";
        public static String PLAY_ACTION = "es.attune.customnotification.action.play";
        public static String NEXT_ACTION = "es.attune.customnotification.action.next";
        public static String STARTFOREGROUND_ACTION = "es.attune.customnotification.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "es.attune.customnotification.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.mipmap.ic_launcher, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }

}