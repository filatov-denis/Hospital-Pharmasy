package hosp.pharm.back.dao.repository;

import hosp.pharm.back.model.entity.BatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BatchRepository extends JpaRepository<BatchEntity, Long> {

    Optional<BatchEntity> findByIdAndActiveTrue(final Long id);

}
