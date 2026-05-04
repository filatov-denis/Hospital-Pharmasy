package hosp.pharm.back.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {

    private Long id;

    private String name;

    private String description;

    private String image_id;

    private Boolean isRequiredRecipe;

    private String manufacturer;

    private String countryOfOriginName;

}
