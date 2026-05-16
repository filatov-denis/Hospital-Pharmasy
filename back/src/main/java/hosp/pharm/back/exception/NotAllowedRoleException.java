package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;

public class NotAllowedRoleException extends ServiceException {

    public NotAllowedRoleException() {
        super(ExceptionMessage.NOT_ALLOWED_ROLE.getValue());
    }

}
