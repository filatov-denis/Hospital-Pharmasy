package hosp.pharm.back.model.dto.analytic;

import hosp.pharm.back.model.dto.response.RequestShortResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class RequestAnalyticDto {

    private Map<String, Integer> histogram;

    private List<RequestShortResponseDto> lines;

}
