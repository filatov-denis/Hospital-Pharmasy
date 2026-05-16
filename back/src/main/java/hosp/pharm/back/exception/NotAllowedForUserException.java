package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;

public class NotAllowedForUserException extends ServiceException {

    public NotAllowedForUserException() {
        super(ExceptionMessage.NOT_ALLOWED_FOR_USER.getValue());
    }

}
