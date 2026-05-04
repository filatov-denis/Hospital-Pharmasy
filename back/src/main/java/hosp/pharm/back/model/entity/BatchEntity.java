package hosp.pharm.back.model.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BatchEntity extends AbstractEntity {


    private ProductEntity product;


    private StorageEntity storage;

    private Integer count;

    private LocalDateTime manufactureDate;

    private LocalDateTime expirationDate;

}
