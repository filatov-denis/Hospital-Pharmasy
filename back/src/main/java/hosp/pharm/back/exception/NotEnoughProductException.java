package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;

public class NotEnoughProductException extends ServiceException {
    public NotEnoughProductException() {
        super(ExceptionMessage.NOT_ENOUGH_PRODUCT.getValue());
    }
}
