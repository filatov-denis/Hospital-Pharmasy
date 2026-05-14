package hosp.pharm.back.state;

import hosp.pharm.back.constant.StatusType;
import hosp.pharm.back.model.entity.RequestEntity;

public class Delivering extends RequestState {

    public Delivering(final RequestEntity request) {
        super(request);
    }

    public void toCompleted() {
        request.setStatus(StatusType.COMPLETED);
        int deliveryCount = request.getRequestBatch().getCount();
        int targetCount = request.getRequestBatch().getTargetBatch().getCount();

        request.getRequestBatch().getTargetBatch().setCount(targetCount + deliveryCount);
    }

    public void toCancelled() {
        request.setStatus(StatusType.CANCELLED);
        int deliveryCount = request.getRequestBatch().getCount();
        int sourceCount = request.getRequestBatch().getSourceBatch().getCount();

        request.getRequestBatch().getSourceBatch().setCount(sourceCount + deliveryCount);
    }
}
