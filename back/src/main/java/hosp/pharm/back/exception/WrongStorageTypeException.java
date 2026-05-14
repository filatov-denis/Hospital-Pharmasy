package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;

public class WrongStorageTypeException extends ServiceException {
    public WrongStorageTypeException() {
        super(ExceptionMessage.WRONG_STORAGE_TYPE.getValue());
    }
}
