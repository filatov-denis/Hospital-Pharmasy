package hosp.pharm.back.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "request_batch")
public class RequestBatchEntity {

    @Id
    private Long id;

    @JoinColumn(name = "source_batch_id", referencedColumnName = "id")
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.EAGER)
    private BatchEntity sourceBatch;

    @JoinColumn(name = "target_batch_id", referencedColumnName = "id")
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.EAGER)
    private BatchEntity targetBatch;

    @MapsId
    @JoinColumn(name = "id")
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.LAZY)
    private RequestEntity request;

    private Integer count;

//    private Boolean isDelivering;

}
