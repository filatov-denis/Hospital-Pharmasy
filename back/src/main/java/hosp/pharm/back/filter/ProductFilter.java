package hosp.pharm.back.filter;

import hosp.pharm.back.constant.ProductType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilter extends AbstractFilter {

    private String name;
    //todo allowable values
    private ProductType productType;

    private Long countryOfOriginId;

    private String manufacturer;
}
