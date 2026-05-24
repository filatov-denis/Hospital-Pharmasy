package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;

public class WrongStorageException extends ServiceException {
    public WrongStorageException() {
        super(ExceptionMessage.WRONG_STORAGE.getValue());
    }
}
