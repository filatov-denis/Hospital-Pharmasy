package hosp.pharm.back.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

        return new ResponseEntity<>(formErrorMessage(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private String formErrorMessage(List<String> errors) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Возникли следующие ошибки валидации при обработке запроса:" );
        int count = 0;
        for(final String error : errors) {
            count++;

            if(count > 1) builder.append(", ");
            builder.append(error);
        }

        return builder.toString();
    }

}