package es.app.attune.attune.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.concurrent.TimeUnit;

import es.app.attune.attune.Classes.Constants;
import es.app.attune.attune.Classes.CredentialsHandler;
import es.app.attune.attune.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @SuppressWarnings("SpellCheckingInspection")
    private static final String CLIENT_ID = "8bcf4a1c62f64325a456b1bee9e857d9";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String REDIRECT_URI = "attune://callback";

    private static final int REQUEST_CODE = 1337;

    private static ConnectivityManager manager;

    private static final String[] scopes = new String[]{"user-read-private","user-read-email","playlist-read","streaming","user-read-playback-state","user-read-currently-playing",
            "user-modify-playback-state","user-library-read","playlist-read-private",
            "user-library-modify","playlist-modify-public","playlist-modify-private"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isOnline(this)){
            Intent intent = getIntent();
            boolean logout =  intent.getBooleanExtra("logout", false);

            String token = CredentialsHandler.getToken(this);
            if (token == null) {
                setContentView(R.layout.activity_login);
            } else {
                if(!logout){
                    startMainActivity(token);
                }else{
                    setContentView(R.layout.activity_login);
                    CredentialsHandler.setToken(this,"",1, TimeUnit.SECONDS);
                }
            }
        }else{
            setContentView(R.layout.activity_login);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    public static Intent createIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }


    public void onLoginButtonClicked(View view) {
        if(isOnline(this)){
            final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                    .setShowDialog(true)
                    .setScopes(scopes)
                    .build();

            AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    logMessage("Got token: " + response.getAccessToken());
                    CredentialsHandler.setToken(this, response.getAccessToken(), response.getExpiresIn(), TimeUnit.SECONDS);
                    startMainActivity(response.getAccessToken());
                    break;

                // Auth flow returned an error
                case ERROR:
                    logError("Auth error: " + response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    logError("Auth result: " + response.getType());
            }
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    private void startMainActivity(String token) {
        Intent intent = MainActivity.createIntent(this);
        intent.putExtra(MainActivity.EXTRA_TOKEN, token);
        intent.setAction(Constants.ACTION.MAIN_ACTION);
        startActivity(intent);
        finish();
    }

    private void logError(String msg) {
        Toast.makeText(this, "Error: " + msg, Toast.LENGTH_SHORT).show();
        Log.e(TAG, msg);
    }

    private void logMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, msg);
    }
}
