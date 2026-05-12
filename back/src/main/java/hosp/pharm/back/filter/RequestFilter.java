package hosp.pharm.back.filter;

import hosp.pharm.back.constant.StatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RequestFilter extends AbstractFilter {

    private Long creatorId;

    private StatusType status;

    private LocalDateTime creationDate;

    private Long batchId;

    private Long productId;

}
