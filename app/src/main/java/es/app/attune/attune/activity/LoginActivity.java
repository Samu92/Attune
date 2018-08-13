package es.app.attune.attune.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.concurrent.TimeUnit;

import es.app.attune.attune.R;
import es.app.attune.attune.classes.App;
import es.app.attune.attune.classes.Constants;
import es.app.attune.attune.classes.CredentialsHandler;
import es.app.attune.attune.classes.SwapResponse;
import es.app.attune.attune.services.SwapService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @SuppressWarnings("SpellCheckingInspection")
    private static final String CLIENT_ID = "8bcf4a1c62f64325a456b1bee9e857d9";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String REDIRECT_URI = "attune://callback";
    private static final int REQUEST_CODE = 1337;
    private static ConnectivityManager manager;
    private MaterialDialog offline;
    private MaterialDialog progress;
    private static final String[] scopes = new String[]{"user-read-private","user-read-email","playlist-read","streaming","user-read-playback-state","user-read-currently-playing",
            "user-modify-playback-state","user-library-read","playlist-read-private",
            "user-library-modify","playlist-modify-public","playlist-modify-private"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isOnline(this)){
            if(CredentialsHandler.hasAccess(this)){
                startMainActivity();
            }else{
                setContentView(R.layout.activity_login);
            }
        }else{
            setContentView(R.layout.activity_login);
            offline = new MaterialDialog.Builder(this)
                    .customView(R.layout.error_layout, false)
                    .cancelable(true)
                    .positiveText(R.string.agree)
                    .build();

            TextView txt_offline = offline.getView().findViewById(R.id.txt_error);
            txt_offline.setText(R.string.txt_no_connection);
            offline.show();
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
            final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.CODE, REDIRECT_URI)
                    .setShowDialog(true)
                    .setScopes(scopes)
                    .build();

            AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        }else{
            offline.show();
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
                case CODE:
                    logMessage("Got code: " + response.getCode());
                    progress = new MaterialDialog.Builder(this)
                            .customView(R.layout.loading_layout, false)
                            .cancelable(false)
                            .build();
                    TextView txt_loading = progress.getView().findViewById(R.id.txt_loading);
                    txt_loading.setText(R.string.txt_loading_session);
                    progress.show();


                    final String code = response.getCode();

                    Retrofit retrofit = new Retrofit.Builder()
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .baseUrl("https://attuneswap.herokuapp.com/")
                            .build();

                    final SwapService service = retrofit.create(SwapService.class);
                    Call<String> call = service.getToken(code);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                String responseString = response.body();
                                SwapResponse gson = new Gson().fromJson(responseString, SwapResponse.class);
                                CredentialsHandler.setToken(App.getContext(),gson.getAccess_token(),Integer.valueOf(gson.getExpires_in()),TimeUnit.SECONDS, gson.getRefresh_token(), code);
                                startMainActivity();
                                progress.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("ERROR","ERROR");
                            progress.dismiss();
                        }
                    });
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

    private void startMainActivity() {
        Intent intent = MainActivity.createIntent(this);
        //intent.putExtra(MainActivity.EXTRA_TOKEN, token);
        intent.setAction(Constants.ACTION.MAIN_ACTION);
        startActivity(intent);
        finish();
    }

    private void logError(String msg) {
        //Toast.makeText(this, "Error: " + msg, Toast.LENGTH_SHORT).show();
        Log.e(TAG, msg);
    }

    private void logMessage(String msg) {
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, msg);
    }
}
