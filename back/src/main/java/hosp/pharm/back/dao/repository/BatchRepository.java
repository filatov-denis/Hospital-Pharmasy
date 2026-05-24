package hosp.pharm.back.dao.repository;

import hosp.pharm.back.model.entity.BatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface BatchRepository extends JpaRepository<BatchEntity, Long> {

    Optional<BatchEntity> findByIdAndActiveTrue(final Long id);

    @Query(value = "select * from batch b " +
            "where active = true and storage_id = :storageId and product_id = :productId " +
            "and manufacture_date = :manufactureDate and expiration_date = :expirationDate limit 1", nativeQuery = true)
    Optional<BatchEntity> findBySourceBatchParameters(@Param("storageId") final Long storageId,
                                                      @Param("productId") final Long productId,
                                                      @Param("manufactureDate") final LocalDate manufactureDate,
                                                      @Param("expirationDate") final LocalDate expirationDate);

}
