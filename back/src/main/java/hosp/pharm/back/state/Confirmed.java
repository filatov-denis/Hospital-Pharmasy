package hosp.pharm.back.state;

import hosp.pharm.back.model.entity.RequestEntity;

public class Confirmed extends RequestState {

    public Confirmed(RequestEntity request) {
        super(request);
    }

    public void toDelivering() {

    }

    public void toCancelled() {

    }

}
