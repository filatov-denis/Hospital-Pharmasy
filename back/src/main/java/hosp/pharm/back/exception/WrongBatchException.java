package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;

public class WrongBatchException extends ServiceException {
    public WrongBatchException() {
        super(ExceptionMessage.WRONG_BATCH.getValue());
    }
}
