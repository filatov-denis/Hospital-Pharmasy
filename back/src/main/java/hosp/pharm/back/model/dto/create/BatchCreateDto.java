package hosp.pharm.back.model.dto.create;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchCreateDto {

    private Long productId;

    private Integer count;

    private Long storageId;

}
