package hosp.pharm.back.model.entity;

import hosp.pharm.back.constant.ProductType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductEntity extends AbstractEntity {

    @Enumerated(value = EnumType.STRING)
    private ProductType type;

    private String name;

    private String description;

    private UUID imageId;

    private Boolean isRequiredRecipe;

    private String manufacturer;

    private Long countryOfOriginId;

    private String countryOfOriginName;

}
