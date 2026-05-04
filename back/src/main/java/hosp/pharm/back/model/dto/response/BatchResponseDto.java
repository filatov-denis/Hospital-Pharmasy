package hosp.pharm.back.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BatchResponseDto {

    private ProductResponseDto product;

    private Integer count;

    private LocalDateTime manufactureDate;

    private LocalDateTime expirationTime;

}
