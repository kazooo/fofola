package cz.mzk.fofola.processes.utils;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.core.util.Base64;

import java.util.ArrayList;
import java.util.List;

public class BasicAuthenticationFilter extends ClientFilter {

    private final String username;
    private final String password;

    public BasicAuthenticationFilter(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
        byte[] encoded = Base64.encode((username + ":" + password).getBytes());
        List<Object> headerValue = new ArrayList<Object>();
        headerValue.add("Basic " + new String(encoded));
        cr.getMetadata().put("Authorization", headerValue);
        return getNext().handle(cr);
    }
}
