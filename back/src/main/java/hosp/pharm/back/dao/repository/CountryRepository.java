package hosp.pharm.back.dao.repository;

import hosp.pharm.back.model.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {

    List<CountryEntity> findByNameContains(final String name);

}
