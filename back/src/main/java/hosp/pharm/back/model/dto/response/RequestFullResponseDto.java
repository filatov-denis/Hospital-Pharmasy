package hosp.pharm.back.model.dto.response;

import hosp.pharm.back.constant.StatusType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestFullResponseDto {

    private Long number;

    private String creatorName;

    private String handlerName;

    private StatusType status;

    private LocalDateTime creationDate;

    private RequestBatchResponseDto batch;

}
