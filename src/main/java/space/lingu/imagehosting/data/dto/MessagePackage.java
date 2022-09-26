package space.lingu.imagehosting.data.dto;

import space.lingu.imagehosting.constant.ErrorCode;

/**
 * @author RollW
 */
public record MessagePackage<D>(
        ErrorCode errorCode,
        String message,
        D data
) {
    public MessagePackage(ErrorCode code, D data) {
        this(code, code.toString(), data);
    }

    public HttpResponseBody<D> toResponseBody() {
        if (errorCode.getState()) {
            return HttpResponseBody.success(message(), data());
        }
        return HttpResponseBody.failure(message(), errorCode(), data());
    }
}
