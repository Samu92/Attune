package es.app.attune.attune.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import es.app.attune.attune.Activity.MainActivity;
import es.app.attune.attune.Classes.App;
import es.app.attune.attune.Classes.CredentialsHandler;
import es.app.attune.attune.Classes.SwapResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RenewService extends Service {
    private final IBinder mRenewBinder = new RenewBinder();
    private Handler renewHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mRenewBinder;
    }

    public class RenewBinder extends Binder {
        public RenewService getService() {
            // Return this instance of LocalService so clients can call public methods
            return RenewService.this;
        }
    }

    public RenewService() {
        super();
        renewHandler = new Handler();

        renewHandler.postDelayed(new Runnable() {
            public void run() {
                renewToken();
                renewHandler.postDelayed(this,600000);
            }
        }, 600000);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void renewToken() {
        // Inicializamos la llamada
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("https://attuneswap.herokuapp.com/")
                .build();
        final SwapService service = retrofit.create(SwapService.class);

        // Obtenemos el token de refresco
        final String refresh_token = CredentialsHandler.getRefreshToken(App.getContext());

        // Hacemos la llamada
        Call<String> call = service.refreshToken(refresh_token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Obtenemos el JSON
                    String responseString = response.body();
                    SwapResponse gson = new Gson().fromJson(responseString, SwapResponse.class);

                    // Obtenemos el nuevo token
                    String newToken = gson.getAccess_token();
                    String expiresIn = gson.getExpires_in();

                    CredentialsHandler.setNewToken(App.getContext(),newToken,Long.valueOf(expiresIn), TimeUnit.SECONDS);

                    // Inicializamos el listener con el nuevo token
                    MainActivity.initToken(newToken);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ERROR","ERROR");
            }
        });
    }
}
