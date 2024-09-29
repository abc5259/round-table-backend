package com.roundtable.roundtable.global.exception;

import com.roundtable.roundtable.global.exception.errorcode.ErrorCode;
import com.roundtable.roundtable.global.response.FailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<FailResponse<?>> handleMethodArgumentNotValidException(final MissingServletRequestParameterException exception) {
        final String defaultErrorMessage = exception.getMessage();
        log.error(defaultErrorMessage,exception);

        return ResponseEntity.badRequest()
                .body(FailResponse.fail(defaultErrorMessage));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailResponse<?>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        final String defaultErrorMessage = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error(defaultErrorMessage,exception);

        return ResponseEntity.badRequest()
                .body(FailResponse.fail(defaultErrorMessage));
    }


    @ExceptionHandler(value = {
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<FailResponse<?>> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException exception) {
        log.error(exception.getMessage(), exception);

        return ResponseEntity.badRequest()
                .body(FailResponse.fail("잘못된 요청입니다."));
    }

    @ExceptionHandler(value = {
            CoreException.NotFoundEntityException.class
    })
    public ResponseEntity<FailResponse<?>> handleNotFoundException(final CoreException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.error(errorCode.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(FailResponse.fail(errorCode.getMessage(), errorCode.getCode()));
    }

    @ExceptionHandler(value = {
            MemberException.MemberUnAuthorizationException.class,
            AuthenticationException.class
    })
    public ResponseEntity<FailResponse<?>> handleAuthenticationException(final CoreException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.error(errorCode.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(FailResponse.fail(errorCode.getMessage(), errorCode.getCode()));
    }

    @ExceptionHandler(value = {
            CoreException.class,
    })
    public ResponseEntity<FailResponse<?>> handleCoreException(final CoreException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.error(errorCode.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(FailResponse.fail(errorCode.getMessage(), errorCode.getCode()));
    }

    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public ResponseEntity<FailResponse<?>> handleBadRequestException(final RuntimeException exception) {
        log.error("message", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(FailResponse.fail(exception.getMessage()));
    }

    @ExceptionHandler(value = {
            ApplicationException.class,
            RuntimeException.class
    })
    public ResponseEntity<FailResponse<?>> handleRuntimeException(final RuntimeException exception) {
        String message = exception.getMessage();
        if(message == null) {
            message = "예상치 못한 에러 발생";
        }
        log.error(message, exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(FailResponse.fail(message));
    }
}
