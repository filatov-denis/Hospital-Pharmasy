package hosp.pharm.back.model.entity;

import hosp.pharm.back.constant.StatusType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
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
@EqualsAndHashCode(callSuper = true)
public class RequestEntity extends AbstractEntity {

    private Long creatorId;

    @OneToOne
    private UserEntity creator;

    private Long handlerId;

    @OneToOne
    private UserEntity handler;

    @OneToOne
    private RequestBatchEntity requestBatch;

    @Enumerated(value = EnumType.STRING)
    private StatusType status;

    @CreationTimestamp
    private LocalDateTime creation_time;
}
