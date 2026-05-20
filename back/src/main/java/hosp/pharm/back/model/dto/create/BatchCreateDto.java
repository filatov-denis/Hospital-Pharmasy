package hosp.pharm.back.model.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Запрос на добавление партии")
public class BatchCreateDto {

    @Schema(description = "Идентификатор продукта", example = "8")
    @NotNull(message = "Идентификатор продукта не может быть пустой")
    private Long productId;

    @Schema(description = "Количество продукта", example = "8")
    @NotNull(message = "Количество продукта не может быть пустым")
    private Integer count;

    @Schema(description = "Дата производства", example = "2025-02-01")
    @NotNull(message = "Дата производства не может быть пустой")
    private LocalDate manufactureDate;

    @Schema(description = "Дата истечения", example = "2025-02-01")
    @NotNull(message = "Идентификатор склада не может быть пустой")
    private LocalDate expirationDate;

}
