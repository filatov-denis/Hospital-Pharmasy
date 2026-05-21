package hosp.pharm.back.model.dto.analytic;

import hosp.pharm.back.model.dto.response.RequestShortResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RequestAnalyticDto {

    private List<RequestShortResponseDto> lines;

}
