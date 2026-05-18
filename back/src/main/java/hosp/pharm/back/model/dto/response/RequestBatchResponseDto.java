package hosp.pharm.back.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Модель партии в запросе")
public class RequestBatchResponseDto {

    private ProductResponseDto product;

    @Schema(description = "Количество продукта", example = "8")
    @NotNull(message = "Количество продукта не может быть пустым")
    private Integer count;

    @Schema(description = "Дата производства", example = "2025-02-01")
    @NotNull(message = "Дата производства не может быть пустой")
    private LocalDate manufactureDate;

    @Schema(description = "Дата истечения", example = "2025-02-01")
    @NotNull(message = "Дата истечения не может быть пустой")
    private LocalDate expirationDate;

    @Schema(description = "Наименование склада отправителя", example = "Общий склад")
    @NotBlank(message = "Наименование склада отправителя не может быть пустым")
    private String sourceStorageName;

    @Schema(description = "Наименование склада получателя", example = "Отделение")
    @NotBlank(message = "Наименование склада получателя не может быть пустым")
    private String targetStorageName;

}
