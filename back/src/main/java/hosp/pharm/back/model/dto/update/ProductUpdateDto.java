package hosp.pharm.back.model.dto.update;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateDto {

    private Long id;

    private String name;

    private String description;

    private String image_id;

    private Boolean isRequiredRecipe;

    private String manufacturer;

    private Long countryId;

}
