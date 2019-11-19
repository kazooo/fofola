package cz.mzk.fofola.kramerius_api;

import com.squareup.okhttp.OkHttpClient;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by holmanj on 16.11.15.
 */
public class KrameriusProcessRemoteApiFactory {

    private static final String PROTOCOL = "http://";
    private static final String KRAMERIUS_CLIENT_API = "/search/api/v4.6";

    public static ProcessRemoteApi getProcessRemoteApi(String krameriusHostUrl, String login, String password) {

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);

        // hlavičky Authorization a User-Agent (pro identifikaci v logu)
        final AuthenticationInterceptor authInterceptor = new AuthenticationInterceptor(login, password);

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setRequestInterceptor(authInterceptor)
                .setClient(new OkClient(okHttpClient))
                .setEndpoint(PROTOCOL + krameriusHostUrl + KRAMERIUS_CLIENT_API)
                .setErrorHandler(new ClientRemoteErrorHandler());

        ProcessRemoteApiRetrofit api = builder.build().create(ProcessRemoteApiRetrofit.class);
        ProcessRemoteApi remoteApi = new ProcessRemoteApi(api);
        return remoteApi;
    }
}