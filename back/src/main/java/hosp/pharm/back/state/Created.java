package hosp.pharm.back.state;

import hosp.pharm.back.constant.StatusType;
import hosp.pharm.back.model.entity.RequestEntity;

public class Created extends RequestState {

    public Created(final RequestEntity request) {
        super(request);
    }

    public void toConfirmed() {
        request.setStatus(StatusType.CONFIRMED);
    }

    public void toCancelled() {
        request.setStatus(StatusType.CANCELLED);
        int deliveryCount = request.getRequestBatch().getCount();
        int sourceReserved = request.getRequestBatch().getSourceBatch().getCount();

        request.getRequestBatch().getSourceBatch().setTotalReservedCount(sourceReserved - deliveryCount);
    }

}
