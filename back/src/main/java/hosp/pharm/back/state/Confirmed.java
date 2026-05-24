package hosp.pharm.back.state;

import hosp.pharm.back.constant.StatusType;
import hosp.pharm.back.exception.NotEnoughProductException;
import hosp.pharm.back.model.entity.RequestEntity;

public class Confirmed extends RequestState {

    public Confirmed(final RequestEntity request) {
        super(request);
    }

    public void toDelivering() {
        request.setStatus(StatusType.DELIVERING);
        int deliveryCount = request.getRequestBatch().getCount();
        int sourceCount = request.getRequestBatch().getSourceBatch().getCount();
        int sourceReserved = request.getRequestBatch().getSourceBatch().getTotalReservedCount();

        if(deliveryCount > sourceCount) {
             throw new NotEnoughProductException();
        }

        request.getRequestBatch().getSourceBatch().setCount(sourceCount - deliveryCount);
        request.getRequestBatch().getSourceBatch().setTotalReservedCount(sourceReserved - deliveryCount);
    }

    public void toCancelled() {
        request.setStatus(StatusType.CANCELLED);
        int deliveryCount = request.getRequestBatch().getCount();
        int sourceReserved = request.getRequestBatch().getSourceBatch().getTotalReservedCount();

        request.getRequestBatch().getSourceBatch().setTotalReservedCount(sourceReserved - deliveryCount);
    }

}
