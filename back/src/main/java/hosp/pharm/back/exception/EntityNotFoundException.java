package hosp.pharm.back.exception;

import hosp.pharm.back.constant.ExceptionMessage;

public class EntityNotFoundException extends ServiceException {

    public EntityNotFoundException() {
        super(ExceptionMessage.ENTITY_NOT_FOUND.getValue());
    }

}
