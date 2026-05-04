package hosp.pharm.back.model.dto.update;

import hosp.pharm.back.constant.StatusType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestUpdateDto {

    private Long handlerId;

    private StatusType status;

}
