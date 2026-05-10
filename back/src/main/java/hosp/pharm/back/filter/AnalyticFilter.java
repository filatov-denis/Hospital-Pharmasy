package hosp.pharm.back.filter;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnalyticFilter {

    private LocalDateTime dateFrom;

    private LocalDateTime dateTo;

}
