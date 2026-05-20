package hosp.pharm.back.model.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Модель обновления склада")
public class StorageUpdateDto {

    @Schema(description = "Идентификатор склада", example = "2")
    @NotNull(message = "Идентификатор склада не может быть пустым")
    private Long id;

    @Schema(description = "Наименование склада", example = "Склад")
    private String name;

}
