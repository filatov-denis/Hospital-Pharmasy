package hosp.pharm.back.model.dto.update;

import hosp.pharm.back.constant.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Модель обновления продукта")
public class ProductUpdateDto {

    @Schema(description = "Идентификатор продукта", example = "1008")
    @NotNull(message = "Идентификатор продукта не может быть пустым")
    private Long id;

    @Schema(description = "Тип продукта", example = "SYRUP")
    @NotNull(message = "Тип продукта не может быть пустым")
    private ProductType productType;

    @Schema(description = "Наименование продукта", example = "Glicerol")
    private String name;

    @Schema(description = "Описание", example = "Description")
    private String description;

    @Schema(description = "Идентификатор изображения", example = "AB50C41E-3814-4533-8F68-A691B4DA9043")
    private String imageId;

    @Schema(description = "Необходимость рецепта", allowableValues = {"true", "false"})
    private Boolean isRequiredRecipe;

    @Schema(description = "Производитель", example = "Bayer")
    private String manufacturer;

    @Schema(description = "Идентификатор страны производства", example = "1008")
    private Long countryId;


}
