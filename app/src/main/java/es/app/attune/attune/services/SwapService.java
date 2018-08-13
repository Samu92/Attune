package es.app.attune.attune.services;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SwapService {
    @POST("api/token")
    Call<String> getToken(@Query("code") String code);

    @POST("api/refresh_token")
    Call<String> refreshToken(@Query("refresh_token") String refresh_token);
}
