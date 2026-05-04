package hosp.pharm.back.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RequestBatchEntity extends AbstractEntity {

    @OneToOne
    private BatchEntity sourceBatch;

    @OneToOne()
    private BatchEntity targetBatch;

    private Integer count;

    private Boolean isDelivering;

}
