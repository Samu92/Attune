package es.app.attune.attune.Classes;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;


/**
 * Created by Samuel on 02/11/2017.
 * Clase creada para el uso de la API de SpotifyCom
 * Objeto SpotifyCom para la comunicación con la API REST de SpotifyCom
 */

public class SpotifyCom extends Application {
    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "8bcf4a1c62f64325a456b1bee9e857d9";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "attune://callback";
    // Request code that will be used to verify if the result comes from correct activity
    private static final int REQUEST_CODE = 1337;

    // Token
    private String token;

    SpotifyService service;
    SpotifyApi api;
    Context context;

    public SpotifyCom(Context context) {
        this.context = context;
        api = new SpotifyApi();
    }

    public void setAccessToken(String accessToken) {
        if(accessToken != ""){
            token = accessToken;
            api.setAccessToken(accessToken);
            service = api.getService();
        }
    }

    public String getAccessToken() {
        return token;
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
