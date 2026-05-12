package hosp.pharm.back.dao.repository;

import hosp.pharm.back.model.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
}
