package hosp.pharm.back.dao.repository;

import hosp.pharm.back.model.entity.RequestBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestBatchRepository extends JpaRepository<RequestBatchEntity, Long> {
}
