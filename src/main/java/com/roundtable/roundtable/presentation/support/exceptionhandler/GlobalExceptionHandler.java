package com.roundtable.roundtable.presentation.support.exceptionhandler;

import com.roundtable.roundtable.implement.common.BusinessException;
import com.roundtable.roundtable.presentation.support.response.ErrorResponse;
import com.roundtable.roundtable.implement.house.HouseException;
import com.roundtable.roundtable.implement.member.MemberException;
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
    public ResponseEntity<ErrorResponse<?>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        final String defaultErrorMessage = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn(defaultErrorMessage);

        return ResponseEntity.badRequest()
                .body(ErrorResponse.fail(defaultErrorMessage));
    }


    @ExceptionHandler(value = {
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse<?>> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException exception) {
        log.warn(exception.getMessage());

        return ResponseEntity.badRequest()
                .body(ErrorResponse.fail("잘못된 요청입니다."));
    }

    @ExceptionHandler(value = {
            MemberException.MemberNotFoundException.class,
            HouseException.HouseNotFoundException.class
    })
    public ResponseEntity<ErrorResponse<?>> handleNotFoundException(final RuntimeException exception) {
        String message = exception.getMessage();
        log.warn(message);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.fail(message));
    }

    @ExceptionHandler(value = {
            MemberException.MemberUnAuthorizationException.class,
    })
    public ResponseEntity<ErrorResponse<?>> handleAuthenticationException(final RuntimeException exception) {
        String message = exception.getMessage();
        log.warn(message);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.fail(message));
    }

    @ExceptionHandler(value = {
            BusinessException.class,
    })
    public ResponseEntity<ErrorResponse<?>> handleAuthenticationException(final BusinessException exception) {
        String message = exception.getMessage();
        log.warn(message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.fail(message));
    }



}
