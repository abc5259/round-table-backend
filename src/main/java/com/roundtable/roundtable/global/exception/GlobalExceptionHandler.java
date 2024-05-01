package com.roundtable.roundtable.global.exception;

import com.roundtable.roundtable.global.exception.errorcode.ErrorCode;
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
        log.warn(defaultErrorMessage,exception);

        return ResponseEntity.badRequest()
                .body(FailResponse.fail(defaultErrorMessage));
    }


    @ExceptionHandler(value = {
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<FailResponse<?>> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException exception) {
        log.warn(exception.getMessage(), exception);

        return ResponseEntity.badRequest()
                .body(FailResponse.fail("잘못된 요청입니다."));
    }

    @ExceptionHandler(value = {
            CoreException.NotFoundEntityException.class
    })
    public ResponseEntity<FailResponse<?>> handleNotFoundException(final CoreException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.warn(errorCode.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(FailResponse.fail(errorCode.getMessage()));
    }

    @ExceptionHandler(value = {
            MemberException.MemberUnAuthorizationException.class,
            AuthenticationException.class
    })
    public ResponseEntity<FailResponse<?>> handleAuthenticationException(final CoreException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.warn(errorCode.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(FailResponse.fail(errorCode.getMessage()));
    }

    @ExceptionHandler(value = {
            CoreException.class,
    })
    public ResponseEntity<FailResponse<?>> handleCoreException(final CoreException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.warn(errorCode.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(FailResponse.fail(errorCode.getMessage()));
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
        log.warn(message, exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(FailResponse.fail(message));
    }

}

//        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTcxNDUzODU2NCwiZXhwIjoxNzE0NTQwMzY0fQ.uErU99BpN8YJxypewAnaNFw9rzRlLUP1iosv6idc9QE",
//        "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTcxNDUzODU2NCwiZXhwIjoxNzE3MTMwNTY0fQ.C-PC7s8isfdha0YucSZn4Ry-uvbt8oeXHxcGYyn10sY"
