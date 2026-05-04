package hosp.pharm.back.repository;

import hosp.pharm.back.model.entity.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<StorageEntity, Long> {
}
