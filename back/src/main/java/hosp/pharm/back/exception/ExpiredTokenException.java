package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;
import org.springframework.security.core.AuthenticationException;

public class ExpiredTokenException extends AuthenticationException {

    public ExpiredTokenException() {
        super(ExceptionMessage.EXPIRED_TOKEN.getValue());
    }

}
