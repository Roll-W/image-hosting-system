package space.lingu.imagehosting.data.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * @author RollW
 */
public class HttpResponseEntity<D> extends ResponseEntity<HttpResponseBody<D>> {
    public HttpResponseEntity(HttpStatus status) {
        super(status);
    }

    public HttpResponseEntity(HttpResponseBody<D> body) {
        super(body, null, body.getStatus());
    }

    public HttpResponseEntity(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    public HttpResponseEntity(HttpResponseBody<D> body, MultiValueMap<String, String> headers) {
        super(body, headers, body.getStatus());
    }

    public static <D> HttpResponseEntity<D> create(HttpResponseBody<D> body) {
        return new HttpResponseEntity<>(body);
    }
}
