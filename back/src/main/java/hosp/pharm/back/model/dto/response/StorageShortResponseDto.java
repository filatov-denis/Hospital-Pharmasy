package hosp.pharm.back.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Упрощённая модель склада")
public class StorageShortResponseDto {

    @Schema(description = "Идентификатор склада", example = "2")
    @NotNull(message = "Идентификатор склада не может быть пустым")
    private Long id;

    @Schema(description = "Наименование склада", example = "Склад")
    @NotBlank(message = "Наименование склада не может быть пустым")
    private String name;

    @Schema(description = "Основной склад", allowableValues = {"true", "false"})
    @NotNull(message = "Параметр 'Основной склад' не может быть пустой")
    private Boolean isPharmacyStorage;

}
