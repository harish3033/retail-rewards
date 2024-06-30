package com.retail.rewards.config;

import com.retail.rewards.model.exception.AppException;
import com.retail.rewards.model.exception.ErrorResponseModel;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private HttpServletResponse httpServletResponse;

    private ErrorResponseModel buildBaseErrorResponseModel() {
        return ErrorResponseModel.builder()
                .message("Internal server error. Contact your Admin")
                .build();
    }

    /**
     * App exception handler
     * @param appException {@link AppException}
     * @param webRequest {@link WebRequest}
     * @return {@link ResponseEntity}&lt;{@link ErrorResponseModel}&gt;
     */
    @SneakyThrows
    @ExceptionHandler(value = {AppException.class})
    protected ResponseEntity<ErrorResponseModel> handleException(AppException appException, WebRequest webRequest) {
        ErrorResponseModel errorResponseModel = null;
        try {
            log.error("AppException handler", appException);
            // httpServletResponse.setHeader(TX_ID, ThreadContext.get(TX_ID));
            errorResponseModel = ErrorResponseModel.builder()
                    .message(appException.getMessage())
                    .build();
        } catch (Exception e) {
            log.error("Exception while handling exception", e);
            errorResponseModel = buildBaseErrorResponseModel();
        }

        return new ResponseEntity<>(errorResponseModel, appException.getHttpStatus());
    }

    /**
     * Generic exception handler
     * @param exception {@link Exception}
     * @param webRequest {@link WebRequest}
     * @return {@link ResponseEntity}&lt;{@link ErrorResponseModel}&gt;
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorResponseModel> otherExceptions(Exception exception, WebRequest webRequest) {
        log.error("Exception handler", exception);
        ErrorResponseModel errorResponseModel = buildBaseErrorResponseModel();
        return new ResponseEntity<>(errorResponseModel, INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseModel errorResponseModel = buildBaseErrorResponseModel();
        return new ResponseEntity<>(errorResponseModel, INTERNAL_SERVER_ERROR);
    }

}