package cz.mzk.integrity.kramerius_api;

import org.apache.commons.codec.binary.*;
import retrofit.*;

/**
 * Created by holmanj on 8.2.15.
 */
public class AuthenticationInterceptor implements RequestInterceptor {

    private String login;
    private String password;

    public AuthenticationInterceptor(String login, String password) {
        this.login = login;
        this.password = password;
    }

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