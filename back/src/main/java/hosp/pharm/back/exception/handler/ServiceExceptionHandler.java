package hosp.pharm.back.exception.handler;

import hosp.pharm.back.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static hosp.pharm.back.constant.ExceptionMessage.UNEXPECTED_ERROR;


@Slf4j
@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    ResponseEntity<Object> handleCustomConflict(ServiceException ex, WebRequest request) {
        log.error("Custom exception occured - [{}], message - [{}]", ex.getClass().getName(), ex.getMessage());

        return super.handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), ex.getStatusCode(), request);
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        log.error("Unexpected exception occured - [{}], message - [{}]", ex.getClass().getName(), ex.getMessage());

        return super.handleExceptionInternal(ex, UNEXPECTED_ERROR.getValue(),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthException(RuntimeException ex) {
        log.error("Exception in authorization process - [{}], message - [{}]", ex.getClass().getName(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

}