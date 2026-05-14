package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;

public class BatchAlreadyExpiredException extends ServiceException {
    public BatchAlreadyExpiredException() {
        super(ExceptionMessage.BATCH_ALREADY_EXPIRED.getValue());
    }
}
