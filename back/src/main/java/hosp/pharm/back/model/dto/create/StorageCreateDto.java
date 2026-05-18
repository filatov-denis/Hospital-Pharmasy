package hosp.pharm.back.model.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Модель создания склада")
public class StorageCreateDto {

    @Schema(description = "Наименование склада", example = "Склад")
    @NotBlank(message = "Наименование склада не может быть пустым")
    private String name;

}
