package hosp.pharm.back.dao.repository;

import hosp.pharm.back.model.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestRepository extends JpaRepository<RequestEntity, Long> {

    List<RequestEntity> getAllByCreationDateAfterOrderByCreationDateDesc(final LocalDateTime creationDateAfter);

}
