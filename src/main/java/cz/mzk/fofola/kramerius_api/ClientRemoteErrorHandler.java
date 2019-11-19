package cz.mzk.fofola.kramerius_api;

import org.apache.http.HttpException;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by holmanj on 16.6.15.
 */
public class ClientRemoteErrorHandler implements ErrorHandler {

    @Override
    public Throwable handleError(RetrofitError cause) {
        Response r = cause.getResponse();
        if (r != null && (r.getStatus() == 500 || r.getStatus() == 404)) {
            return new HttpException("Request " + cause.getKind() + " " + cause.getUrl() + " returned status " + r.getStatus());
        }

        if (r != null && r.getStatus() == 409) {
            return new HttpException("Request " +  cause.getUrl() + " returned status " + r.getStatus() + ". Only running processes can be stopped.");
        }
        return cause;
    }
}
