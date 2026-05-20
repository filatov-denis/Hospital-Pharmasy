package hosp.pharm.back.model.dto.response;

import hosp.pharm.back.constant.ProductType;
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

    @Schema(description = "Тип продукта", example = "SYRUP")
    @NotNull(message = "Тип продукта не может быть пустым")
    private ProductType productType;

    @Schema(description = "Описание", example = "Description")
    private String description;

    @Schema(description = "Идентификатор изображения", example = "AB50C41E-3814-4533-8F68-A691B4DA9043")
    private String imageId;

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
    private String countryName;

}
