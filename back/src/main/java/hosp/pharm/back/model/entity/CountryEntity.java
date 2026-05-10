package hosp.pharm.back.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "country")
@EqualsAndHashCode(callSuper = true)
public class CountryEntity extends AbstractEntity {

    private String name;

}
