package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;
import org.springframework.security.core.AuthenticationException;

public class InvalidCredentialsException extends AuthenticationException {

    public InvalidCredentialsException() {
        super(ExceptionMessage.INVALID_CREDENTIALS.getValue());
    }

}
