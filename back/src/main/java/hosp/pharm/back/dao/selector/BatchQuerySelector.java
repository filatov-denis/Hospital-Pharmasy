package hosp.pharm.back.dao.selector;

import hosp.pharm.back.filter.BatchFilter;
import hosp.pharm.back.model.entity.BatchEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BatchQuerySelector extends AbstractQuerySelector<BatchEntity, BatchFilter>{

    public BatchQuerySelector(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected List<Predicate> createPredicates(final Root<BatchEntity> root, final BatchFilter filter) {
        final List<Predicate> predicates = new ArrayList<>();
        root.join("storage", JoinType.LEFT);
        predicates.add(criteriaBuilder.equal(root.get("storage").get("id"), filter.getStorageId()));

        return predicates;
    }

    @Override
    protected Class<BatchEntity> getEntityClass() {
        return BatchEntity.class;
    }
}
