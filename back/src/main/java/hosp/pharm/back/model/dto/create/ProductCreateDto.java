package hosp.pharm.back.model.dto.create;

import hosp.pharm.back.constant.ProductType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateDto {

    private ProductType productType;

    private String name;

    private String description;

    private String image_id;

    private Boolean isRequiredRecipe;

    private String manufacturer;

    private Long countryId;

}
