package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;

public class UnavailableBatchException extends ServiceException {

    public UnavailableBatchException() {
        super(ExceptionMessage.UNAVAILABLE_BATCH.getValue());
    }

}
