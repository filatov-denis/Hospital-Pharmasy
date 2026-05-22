package hosp.pharm.back.exception.handler;

import hosp.pharm.back.exception.ServiceException;
import hosp.pharm.back.model.Message;
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
        log.error("Custom exception occurred - [{}], message - [{}]", ex.getClass().getName(), ex.getMessage());

        return super.handleExceptionInternal(ex, new Message(ex.getMessage()),
                new HttpHeaders(), ex.getStatusCode(), request);
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        log.error("Unexpected exception occurred - [{}], message - [{}]", ex.getClass().getName(), ex.getMessage());
        return super.handleExceptionInternal(ex, new Message(UNEXPECTED_ERROR.getValue()),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<Object> handleAuthConflict(RuntimeException ex, WebRequest request) {
        log.error("Authentication exception occurred - [{}], message - [{}]", ex.getClass().getName(), ex.getMessage());
        return super.handleExceptionInternal(ex, new Message(ex.getMessage()),
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

}