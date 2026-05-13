package hosp.pharm.back.state;

import hosp.pharm.back.exception.UnavailableStatusException;
import hosp.pharm.back.model.entity.RequestEntity;

public abstract class RequestState {

    protected final RequestEntity request;

    public RequestState(final RequestEntity request) {
        this.request = request;
    }

    public void toConfirmed() {
        throw new UnavailableStatusException();
    }

    public void toCancelled() {
        throw new UnavailableStatusException();
    }

    public void toDelivering() {
        throw new UnavailableStatusException();
    }

    public void toCompleted() {
        throw new UnavailableStatusException();
    }

}
