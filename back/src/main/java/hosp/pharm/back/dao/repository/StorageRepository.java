package hosp.pharm.back.dao.repository;

import hosp.pharm.back.model.entity.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<StorageEntity, Long> {

    Optional<StorageEntity> findByIdAndActiveTrue(final Long id);

}
