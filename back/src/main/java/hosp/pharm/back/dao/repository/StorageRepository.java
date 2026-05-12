package hosp.pharm.back.dao.repository;

import hosp.pharm.back.model.entity.StorageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<StorageEntity, Long> {

    Page<StorageEntity> findByNameContains(final String name, final Pageable pageable);

    //Page<StorageEntity> findAll(final Pageable pageable);

}
