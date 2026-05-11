package hosp.pharm.back.model.dto.create;

import hosp.pharm.back.constant.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Модель создания продукта")
public class ProductCreateDto {

    @Schema(description = "Тип продукта", example = "NOSE_SPRAY")
    @NotNull(message = "Тип продукта не может быть пустым")
    private ProductType productType;

    @Schema(description = "Наименование продукта", example = "Glicerol")
    @NotNull(message = "Наименование продукта не может быть пустым")
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

}
