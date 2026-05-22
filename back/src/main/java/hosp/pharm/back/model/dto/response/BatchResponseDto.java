package hosp.pharm.back.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Модель партии продукта")
public class BatchResponseDto {

    @Schema(description = "Идентификатор партии", example = "7")
    @NotNull(message = "Идентификатор партии не может быть пустым")
    private Long name;

    private ProductResponseDto product;

    @Schema(description = "Количество продукта", example = "8")
    @NotNull(message = "Количество продукта не может быть пустым")
    private Integer count;

    @Schema(description = "Дата производства", example = "10")
    @NotNull(message = "Дата производства не может быть пустой")
    private LocalDate manufactureDate;

    @Schema(description = "Дата истечения", example = "10")
    @NotNull(message = "Дата истечения не может быть пустой")
    private LocalDate expirationDate;

}
