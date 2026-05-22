package hosp.pharm.back.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BatchFilter extends AbstractFilter {

    private Long storageId;

}
