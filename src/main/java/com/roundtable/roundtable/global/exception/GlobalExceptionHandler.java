package com.roundtable.roundtable.global.exception;

import com.roundtable.roundtable.global.response.FailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailResponse<?>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        final String defaultErrorMessage = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn(defaultErrorMessage);

        return ResponseEntity.badRequest()
                .body(FailResponse.fail(defaultErrorMessage));
    }


    @ExceptionHandler(value = {
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<FailResponse<?>> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException exception) {
        log.warn(exception.getMessage());

        return ResponseEntity.badRequest()
                .body(FailResponse.fail("잘못된 요청입니다."));
    }

    @ExceptionHandler(value = {
            MemberException.MemberNotFoundException.class,
            HouseException.HouseNotFoundException.class
    })
    public ResponseEntity<FailResponse<?>> handleNotFoundException(final RuntimeException exception) {
        String message = exception.getMessage();
        log.warn(message);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(FailResponse.fail(message));
    }

    @ExceptionHandler(value = {
            MemberException.MemberUnAuthorizationException.class,
    })
    public ResponseEntity<FailResponse<?>> handleAuthenticationException(final RuntimeException exception) {
        String message = exception.getMessage();
        log.warn(message);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(FailResponse.fail(message));
    }

    @ExceptionHandler(value = {
            ApplicationException.class,
    })
    public ResponseEntity<FailResponse<?>> handleAuthenticationException(final ApplicationException exception) {
        String message = exception.getMessage();
        log.warn(message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(FailResponse.fail(message));
    }

    @ExceptionHandler(value = {
            RuntimeException.class,
    })
    public ResponseEntity<FailResponse<?>> handleRuntimeException(final RuntimeException exception) {
        String message = exception.getMessage();
        if(message == null) {
            message = "예상치 못한 에러 발생";
        }
        log.warn(message);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(FailResponse.fail(message));
    }

}
