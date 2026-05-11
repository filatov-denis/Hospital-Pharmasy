package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;
import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException() {
        super(ExceptionMessage.INVALID_TOKEN.getValue());
    }

}
