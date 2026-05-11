package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;

public class UserAlreadyExistException extends ServiceException {

    public UserAlreadyExistException() {
        super(ExceptionMessage.INVALID_TOKEN.getValue());
    }

}
