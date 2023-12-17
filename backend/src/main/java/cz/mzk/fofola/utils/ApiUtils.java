package cz.mzk.fofola.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class ApiUtils {

    public static String buildUri(final String url, final Map<String, ?> params) {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        params.forEach(builder::queryParam);
        return builder.encode().build().toUri().toString();
    }

    public static String buildUri(final String url, final Object objectParams) {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.convertValue(objectParams, new TypeReference<Map<String,String>>(){}).forEach(builder::queryParam);
        return builder.encode().build().toUri().toString();
    }
}
