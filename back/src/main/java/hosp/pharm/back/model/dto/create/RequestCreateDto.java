package hosp.pharm.back.model.dto.create;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCreateDto {

    private Long sourceBatchId;

    private Long targetBatchId;

    private Integer count;

}
