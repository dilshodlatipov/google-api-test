package uz.pdp.googleapitest.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.pdp.googleapitest.payload.Response;

@RestControllerAdvice
@Order(value = Integer.MIN_VALUE)
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceptionHelper {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Response<?>> handle(Exception ex) {
        return ResponseEntity.internalServerError().body(
                Response.errorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }

    @ExceptionHandler(value = RestException.class)
    public ResponseEntity<Response<?>> handle(RestException ex) {
        return ResponseEntity.status(ex.getStatus()).body(
                Response.errorResponse(ex.getMessage(), ex.getStatus())
        );
    }
}
