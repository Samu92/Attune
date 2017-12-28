package es.app.attune.attune.Classes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import de.hdodenhof.circleimageview.CircleImageView;
import es.app.attune.attune.R;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Samuel on 02/11/2017.
 * Clase creada para el uso de la API de SpotifyCom
 * Objeto SpotifyCom para la comunicación con la API REST de SpotifyCom
 */

public class SpotifyCom {
    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "8bcf4a1c62f64325a456b1bee9e857d9";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "attune://callback";
    // Request code that will be used to verify if the result comes from correct activity
    private static final int REQUEST_CODE = 1337;
    SpotifyService service;
    SpotifyApi api;
    Context context;

    public SpotifyCom(Context context) {
        this.context = context;
        api = new SpotifyApi();
    }

    public void setAccessToken(String accessToken) {
        if(accessToken != ""){
            api.setAccessToken(accessToken);
            service = api.getService();
        }
    }

    public void Authenticate(final Activity activity){
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(activity, REQUEST_CODE, request);

    }

    public String getClientID(){
        return CLIENT_ID;
    }

    public SpotifyService getService(){
        return service;
    }
}
