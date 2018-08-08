package es.app.attune.attune.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

import java.util.concurrent.TimeUnit;

/**
 * Created by Samuel on 06/03/2018.
 */

public class CredentialsHandler {
    private static final String ACCESS_TOKEN_NAME = "webapi.credentials.access_token";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String EXPIRES_AT = "expires_at";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String CODE = "code";

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

        if (token == null || expiresAt < System.currentTimeMillis()) {
            return null;
        }

        return token;
    }

    public static String getRefreshToken(Context context){
        Context appContext = context.getApplicationContext();
        SharedPreferences sharedPref = getSharedPreferences(appContext);

        String refresh_token = sharedPref.getString(REFRESH_TOKEN, null);
        long expiresAt = sharedPref.getLong(EXPIRES_AT, 0L);

        if (refresh_token == null || expiresAt < System.currentTimeMillis()) {
            return null;
        }

        return refresh_token;
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

}
