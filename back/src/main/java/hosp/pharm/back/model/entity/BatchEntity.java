package hosp.pharm.back.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "batch")
@EqualsAndHashCode(callSuper = true)
public class BatchEntity extends AbstractEntity {

    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private ProductEntity product;

    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "storage_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private StorageEntity storage;

    private Integer count;

    private Integer totalReservedCount;

    private boolean active;

    private LocalDate manufactureDate;

    private LocalDate expirationDate;

}
