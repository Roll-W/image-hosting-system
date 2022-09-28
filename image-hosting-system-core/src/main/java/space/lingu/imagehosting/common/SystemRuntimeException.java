/*
 * Copyright (C) 2022 Lingu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.lingu.imagehosting.common;

/**
 * @author RollW
 */
public abstract class SystemRuntimeException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public SystemRuntimeException(ErrorCode errorCode) {
        this(errorCode.toString(), errorCode);
    }

    public SystemRuntimeException(String message, ErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
        this.message = message;
    }

    public SystemRuntimeException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.message = message;
        this.errorCode = errorCode;
    }

    public SystemRuntimeException(Throwable cause) {
        super(cause);
        this.errorCode = ErrorCode.getErrorFromThrowable(cause);
        this.message = cause.toString();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
