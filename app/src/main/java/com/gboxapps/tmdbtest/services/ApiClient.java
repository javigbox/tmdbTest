package com.gboxapps.tmdbtest.services;

import android.content.Context;

import com.gboxapps.tmdbtest.util.Constants;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class ApiClient {

    private static final String TMDB_SERVICE = "tmdb_service";

    private static RestAdapter tmdbRestAdapter;

    private static MovieApiInterfaceV3 movieApiInterfaceV3;
    private static MovieApiInterfaceV4 movieApiInterfaceV4;

    private ApiClient() {
    }

    private static RestAdapter getRestAdapter(Context context, String service, int api_version) {

        RestAdapter restAdapter = null;

        if (TMDB_SERVICE.equals(service)) {

            if (tmdbRestAdapter == null) {
                tmdbRestAdapter = createRestAdapter(context, service, api_version);
            }
            restAdapter = tmdbRestAdapter;
        }
        return restAdapter;
    }

    /**
     * Create the REST adapter instance to adapt a Java interface to a REST API.
     *
     * @param context context to access the application resources
     * @return A {@link RestAdapter} adapter instance.
     */
    private static RestAdapter createRestAdapter(Context context, String service, int api_version) {

        String urlService = null;

        if (TMDB_SERVICE.compareTo(service) == 0) {
            if(api_version == 3)
                urlService = Constants.BASE_URL_V3;
            else
                urlService = Constants.BASE_URL_V4;
        }


        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(Constants.CONNECTION_TIME_OUT, TimeUnit.SECONDS);
        client.setReadTimeout(Constants.CONNECTION_TIME_OUT, TimeUnit.SECONDS);

        // Create network interceptor to fix EOFException when a empty body response with
        // Content-Encoding gzip is received (NXT-841). We consider a empty body when the server
        // send Content-Length to 0 so the only thing to do it's to replace Content-Encondig gzip
        // to identity.
        client.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                String contentLengthHeader = originalResponse.header("Content-Length");
                if(contentLengthHeader != null && contentLengthHeader.equals("0")) {
                    return originalResponse.newBuilder()
                            .header("Content-Encoding", "application/json;charset=utf-8")
                            .build();
                }
                return originalResponse;
            }
        });

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(client))
                .setEndpoint(urlService)
                .build();

        return restAdapter;
    }

    /**
     * Return an implementation of the API defined by {@link MovieApiInterfaceV3}
     *
     * @param context context to access the application resources
     * @return a web service interface implementation of <code>MovieApiInterfaceV3</code>
     */
    public static MovieApiInterfaceV3 getMovieV3ServiceInterface(Context context) {
        if (movieApiInterfaceV3 == null) {
            movieApiInterfaceV3 = getRestAdapter(context, TMDB_SERVICE, 3).create(MovieApiInterfaceV3.class);
        }
        return movieApiInterfaceV3;
    }

    /**
     * Return an implementation of the API defined by {@link MovieApiInterfaceV4}
     *
     * @param context context to access the application resources
     * @return a web service interface implementation of <code>MovieApiInterfaceV4</code>
     */
    public static MovieApiInterfaceV4 getMovieV4ServiceInterface(Context context) {
        if (movieApiInterfaceV4 == null) {
            movieApiInterfaceV4 = getRestAdapter(context, TMDB_SERVICE, 4).create(MovieApiInterfaceV4.class);
        }
        return movieApiInterfaceV4;
    }
}
