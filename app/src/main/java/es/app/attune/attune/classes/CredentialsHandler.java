package es.app.attune.attune.classes;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.models.UserPrivate;

/**
 * Created by Samuel on 06/03/2018.
 */

public class CredentialsHandler {
    private static final String ACCESS_TOKEN_NAME = "webapi.credentials.access_token";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String EXPIRES_AT = "expires_at";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String CODE = "code";
    private static final String USERID = "userid";
    private static final String USEREMAIL = "useremail";
    private static final String USERIMAGE = "userimage";
    private static final String USERDISPLAYNAME = "userdisplayname";
    private static final String USERPRODUCT = "userproduct";

    public static void setToken(Context context, String token, long expiresIn, TimeUnit unit, String refreshToken, String code) {
        Context appContext = context.getApplicationContext();

        long now = System.currentTimeMillis();
        long expiresAt = now + unit.toMillis(expiresIn);

        SharedPreferences sharedPref = getSharedPreferences(appContext);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ACCESS_TOKEN, token);
        editor.putLong(EXPIRES_AT, expiresAt);
        editor.putString(REFRESH_TOKEN, refreshToken);
        editor.putString(CODE, code);
        editor.apply();
    }

    public static void setNewToken(Context context, String newToken, long expiresIn, TimeUnit unit){
        Context appContext = context.getApplicationContext();

        long now = System.currentTimeMillis();
        long expiresAt = now + unit.toMillis(expiresIn);

        SharedPreferences sharedPref = getSharedPreferences(appContext);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(ACCESS_TOKEN, newToken);
        editor.putLong(EXPIRES_AT, expiresAt);
        editor.apply();
    }

    private static SharedPreferences getSharedPreferences(Context appContext) {
        return appContext.getSharedPreferences(ACCESS_TOKEN_NAME, Context.MODE_PRIVATE);
    }

    public static String getToken(Context context) {
        Context appContext = context.getApplicationContext();
        SharedPreferences sharedPref = getSharedPreferences(appContext);

        String token = sharedPref.getString(ACCESS_TOKEN, null);
        long expiresAt = sharedPref.getLong(EXPIRES_AT, 0L);

        return token;
    }

    public static String getRefreshToken(Context context){
        Context appContext = context.getApplicationContext();
        SharedPreferences sharedPref = getSharedPreferences(appContext);

        return sharedPref.getString(REFRESH_TOKEN, null);
    }

    public static String getCode(Context context){
        Context appContext = context.getApplicationContext();
        SharedPreferences sharedPref = getSharedPreferences(appContext);

        String code = sharedPref.getString(CODE, null);

        if (code == null) {
            return null;
        }

        return code;
    }

    public static void setUserCredentials(Context context, UserPrivate user){
        SharedPreferences sharedPref = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(USERID, user.id);
        editor.putString(USEREMAIL, user.email);
        if (user.images.size() > 0) {
            editor.putString(USERIMAGE, user.images.get(0).url);
        } else {
            editor.putString(USERIMAGE, "");
        }
        editor.putString(USERDISPLAYNAME, user.display_name);
        editor.putString(USERPRODUCT, user.product);
        editor.apply();
    }

    public static boolean hasAccess(Context context) {
        Context appContext = context.getApplicationContext();
        SharedPreferences sharedPref = getSharedPreferences(appContext);

        String refresh_code = sharedPref.getString(REFRESH_TOKEN, null);

        return refresh_code != null;
    }

    public static boolean hasUser(Context context){
        Context appContext = context.getApplicationContext();
        SharedPreferences sharedPref = getSharedPreferences(appContext);

        String userid = sharedPref.getString(USERID, null);

        return userid != null;
    }

    public static String getUserId(Context context){
        Context appContext = context.getApplicationContext();
        SharedPreferences sharedPref = getSharedPreferences(appContext);

        String userid = sharedPref.getString(USERID, null);

        if(userid == null){
            return  "";
        }else{
            return  userid;
        }
    }

    public static String getUserEmail(Context context){
        Context appContext = context.getApplicationContext();
        SharedPreferences sharedPref = getSharedPreferences(appContext);

        String useremail = sharedPref.getString(USEREMAIL, null);

        if(useremail == null){
            return  "";
        }else{
            return  useremail;
        }
    }

    public static String getUserImage(Context context){
        Context appContext = context.getApplicationContext();
        SharedPreferences sharedPref = getSharedPreferences(appContext);

        String userimage = sharedPref.getString(USERIMAGE, null);

        if(userimage == null){
            return  "";
        }else{
            return  userimage;
        }
    }

    public static String getUserDisplayName(Context context){
        Context appContext = context.getApplicationContext();
        SharedPreferences sharedPref = getSharedPreferences(appContext);

        String userdisplayname = sharedPref.getString(USERDISPLAYNAME, null);

        if(userdisplayname == null){
            return  "";
        }else{
            return  userdisplayname;
        }
    }

    public static String getUserProduct(Context context){
        Context appContext = context.getApplicationContext();
        SharedPreferences sharedPref = getSharedPreferences(appContext);

        String userProduct = sharedPref.getString(USERPRODUCT, null);

        if(userProduct == null){
            return  "";
        }else{
            return  userProduct;
        }
    }

    public static void removeCredentials(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
