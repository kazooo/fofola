package cz.mzk.fofola.kramerius_api;

import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.*;
import retrofit.*;

/**
 * Created by holmanj on 8.2.15.
 */
@AllArgsConstructor
public class AuthenticationInterceptor implements RequestInterceptor {

    private String login;
    private String password;

    @Override
    public void intercept(RequestFacade request) {
        final String authorizationValue = encodeCredentialsForBasicAuthorization();
        request.addHeader("Authorization", authorizationValue);
        request.addHeader("User-Agent", "K4-tools skript");
    }

    private String encodeCredentialsForBasicAuthorization() {
        final String userAndPassword = login + ":" + password;
        return "Basic " + new String(Base64.encodeBase64(userAndPassword.getBytes()));
    }

}