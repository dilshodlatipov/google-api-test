package uz.pdp.googleapitest.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private boolean success;
    private String message;
    private T data;
    private List<ErrorData> errors;

    private Response() {
        this.success = true;
    }

    private Response(String message) {
        this();
        this.message = message;
    }

    private Response(String message, T data) {
        this(message);
        this.data = data;
    }

    private Response(T data, boolean isData) {
        this();
        this.data = data;
    }

    private Response(List<ErrorData> errors) {
        this.errors = errors;
    }

    private Response(String errorMsg, Integer errorCode) {
        this.errors = Collections.singletonList(new ErrorData(errorMsg, errorCode));
    }

    public static <T> Response<T> success() {
        return new Response<T>();
    }

    public static <T extends CharSequence> Response<T> successMessage(T message) {
        return new Response<>(message, true);
    }

    public static <T> Response<T> success(String message) {
        return new Response<>(message);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(data, true);
    }

    public static <T> Response<T> success(String message, T data) {
        return new Response<>(message, data);
    }

    public static Response<List<ErrorData>> errorResponse(String errorMsg, Integer errorCode) {
        return new Response<>(errorMsg, errorCode);
    }

    public static Response<List<ErrorData>> errorResponse(List<ErrorData> errors) {
        return new Response<>(errors);
    }
}
