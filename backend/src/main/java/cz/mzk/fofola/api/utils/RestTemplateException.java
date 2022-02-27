package cz.mzk.fofola.api.utils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;


@Setter
@Getter
@ToString
public class RestTemplateException extends RuntimeException {

    private HttpStatus statusCode;
    private String error;

    public RestTemplateException(HttpStatus statusCode, String error) {
        super(error);
        this.statusCode = statusCode;
        this.error = error;
    }
}
