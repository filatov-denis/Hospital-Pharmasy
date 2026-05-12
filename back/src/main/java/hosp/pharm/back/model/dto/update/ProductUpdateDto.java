package hosp.pharm.back.model.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "Модель обновления продукта")
public class ProductUpdateDto {

    @Schema(description = "Идентификатор продукта", example = "1008")
    @NotNull(message = "Идентификатор продукта не может быть пустым")
    private Long id;

    @Schema(description = "Наименование продукта", example = "Glicerol")
    private String name;

    @Schema(description = "Описание", example = "Description")
    private String description;

    @Schema(description = "Идентификатор изображения", example = "8123as489a3df0c571")
    private UUID imageId;

    @Schema(description = "Необходимость рецепта", allowableValues = {"true", "false"})
    private Boolean isRequiredRecipe;

    @Schema(description = "Производитель", example = "Bayer")
    private String manufacturer;

    @Schema(description = "Идентификатор страны производства", example = "1008")
    private Long countryId;


}
