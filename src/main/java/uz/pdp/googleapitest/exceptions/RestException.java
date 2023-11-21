package uz.pdp.googleapitest.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class RestException extends RuntimeException {
    private int status;

    private RestException(String message, HttpStatus status) {
        super(message);
        this.status = status.value();
    }

    public static RestException restThrow(String message) {
        return new RestException(message, HttpStatus.BAD_REQUEST);
    }

    public static RestException restThrow(String message, HttpStatus status) {
        return new RestException(message, status);
    }
}
