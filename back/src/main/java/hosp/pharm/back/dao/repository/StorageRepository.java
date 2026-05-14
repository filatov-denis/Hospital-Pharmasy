package hosp.pharm.back.dao.repository;

import hosp.pharm.back.model.entity.StorageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<StorageEntity, Long> {

    Page<StorageEntity> findByNameContains(final String name, final Pageable pageable);

    Optional<StorageEntity> findByIdAndActiveTrue(final Long id);

}
