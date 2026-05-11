package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;

public class NullIdentifierException extends ServiceException {

    public NullIdentifierException() {
        super(ExceptionMessage.INVALID_TOKEN.getValue());
    }

}
