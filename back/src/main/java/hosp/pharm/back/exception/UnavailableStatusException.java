package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;

public class UnavailableStatusException extends ServiceException {

    public UnavailableStatusException() {
        super(ExceptionMessage.UNAVAILABLE_STATUS_EXCEPTION.getValue());
    }

}
