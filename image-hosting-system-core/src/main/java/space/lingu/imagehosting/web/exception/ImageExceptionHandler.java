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

package space.lingu.imagehosting.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import space.lingu.imagehosting.common.ErrorCode;
import space.lingu.imagehosting.common.SystemRuntimeException;
import space.lingu.imagehosting.data.dto.HttpResponseBody;
import space.lingu.imagehosting.data.dto.HttpResponseEntity;
import space.lingu.light.LightRuntimeException;

/**
 * Handle {@link LightRuntimeException}
 *
 * @author RollW
 */
@ControllerAdvice
public class ImageExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(ImageExceptionHandler.class);

    @ExceptionHandler(LightRuntimeException.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(LightRuntimeException e) {
        logger.error("[SQL] Suspected SQL error. %s".formatted(e.toString()), e);
        return HttpResponseEntity.create(HttpResponseBody.failure(
                e.getMessage(),
                ErrorCode.getErrorFromThrowable(e),
                e.toString())
        );
    }

    @ExceptionHandler(SystemRuntimeException.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(SystemRuntimeException e) {
        logger.error("System runtime error: %s".formatted(e.toString()), e);
        return HttpResponseEntity.create(HttpResponseBody.failure(
                e.getMessage(),
                e.getErrorCode(),
                e.toString())
        );
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(NullPointerException e) {
        logger.error("Null exception : %s".formatted(e.toString()), e);
        return HttpResponseEntity.create(HttpResponseBody.failure(
                e.getMessage(),
                ErrorCode.ERROR_NULL,
                e.toString())
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(Exception e) {
        logger.error("Error: %s".formatted(e.toString()), e);
        return HttpResponseEntity.create(HttpResponseBody.failure(
                HttpStatus.SERVICE_UNAVAILABLE,
                e.getMessage(),
                ErrorCode.getErrorFromThrowable(e),
                e.toString())
        );
    }
}
