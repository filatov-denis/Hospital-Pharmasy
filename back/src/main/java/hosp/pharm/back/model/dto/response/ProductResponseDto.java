package hosp.pharm.back.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Модель продукта")
public class ProductResponseDto {

    @Schema(description = "Идентификатор продукта", example = "4")
    @NotNull(message = "Идентификатор продукта не может быть пустым")
    private Long id;

    @Schema(description = "Наименование продукта", example = "Glicerol")
    @NotBlank(message = "Наименование продукта не может быть пустым")
    private String name;

    @Schema(description = "Описание", example = "Description")
    private String description;

    @Schema(description = "Идентификатор изображения", example = "8123as489a3df0c571")
    private String image_id;

    @Schema(description = "Необходимость рецепта", allowableValues = {"true", "false"})
    @NotNull(message = "Необходимость рецепта не может быть пустой")
    private Boolean isRequiredRecipe;

    @Schema(description = "Производитель", example = "Bayer")
    private String manufacturer;

    @Schema(description = "Идентификатор страны производства", example = "1008")
    @NotNull(message = "Идентификатор страны производства не может быть пустым")
    private Long countryId;

    @Schema(description = "Наименование страны производства", example = "Ангола")
    @NotNull(message = "Наименование страны производства не может быть пустым")
    private Long countryName;

}
