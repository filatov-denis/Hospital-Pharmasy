package hosp.pharm.back.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import hosp.pharm.back.constant.ProductType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
@EqualsAndHashCode(callSuper = true)
public class ProductEntity extends AbstractEntity {

    @Enumerated(value = EnumType.STRING)
    private ProductType type;

    private String name;

    private String description;

    private Boolean active = true;

    private String imageId;

    private Boolean isRequiredRecipe;

    private String manufacturer;

    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "country_of_origin_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private CountryEntity countryOfOrigin;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.LAZY)
    private Set<BatchEntity> batches = new LinkedHashSet<>();
}
