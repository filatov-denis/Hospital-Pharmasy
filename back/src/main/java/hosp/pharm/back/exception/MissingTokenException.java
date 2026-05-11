package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;
import org.springframework.security.core.AuthenticationException;

public class MissingTokenException extends AuthenticationException {

    public MissingTokenException() {
        super(ExceptionMessage.MISSING_TOKEN.getValue());
    }

}
