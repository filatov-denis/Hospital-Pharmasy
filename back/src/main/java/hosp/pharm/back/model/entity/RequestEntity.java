package hosp.pharm.back.model.entity;

import hosp.pharm.back.constant.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "request")
@EqualsAndHashCode(callSuper = true)
public class RequestEntity extends AbstractEntity {

    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.EAGER)
    private UserEntity creator;

    @JoinColumn(name = "handler_id", referencedColumnName = "id")
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.EAGER)
    private UserEntity handler;

    @MapsId
    @JoinColumn(name = "id")
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.LAZY)
    private RequestBatchEntity requestBatch;

    @Enumerated(value = EnumType.STRING)
    private StatusType status;

    @CreationTimestamp
    private LocalDateTime creationTime;
}
