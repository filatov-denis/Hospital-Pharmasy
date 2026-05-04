package hosp.pharm.back.repository;

import hosp.pharm.back.model.entity.BatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<BatchEntity, Long> {
}
