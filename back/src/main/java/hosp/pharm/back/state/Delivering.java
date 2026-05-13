package hosp.pharm.back.state;

import hosp.pharm.back.model.entity.RequestEntity;

public class Delivering extends RequestState {

    public Delivering(RequestEntity request) {
        super(request);
    }

    public void toCompleted() {

    }

    public void toCancelled() {

    }
}
