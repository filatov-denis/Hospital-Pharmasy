package hosp.pharm.back.model.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Модель обновления партии")
public class BatchUpdateDto {

    @Schema(description = "Идентификатор партии", example = "2")
    @NotNull(message = "Идентификатор партии не может быть пустым")
    private Long id;

    @Schema(description = "Количество продукта", example = "10")
    private Integer count;

    @Schema(description = "Дата производства", example = "2025-02-01")
    private LocalDate manufactureDate;

    @Schema(description = "Дата истечения", example = "2025-02-01")
    private LocalDate expirationDate;

}
