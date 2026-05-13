package hosp.pharm.back.state;

import hosp.pharm.back.model.entity.RequestEntity;

public class Created extends RequestState {

    public Created(RequestEntity request) {
        super(request);
    }

    public void toConfirmed() {

    }

    public void toCancelled() {

    }

}
