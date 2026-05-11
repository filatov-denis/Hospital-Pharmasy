package hosp.pharm.back.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Модель страны")
public class CountryResponseDto {

    @Schema(description = "Идентификатор страны", example = "7")
    @NotNull(message = "Идентификатор страны не может быть пустым")
    private Long id;

    @Schema(description = "Наименование страны", example = "Болгария")
    @NotNull(message = "Наименование страны не может быть пустым")
    private String name;

}
