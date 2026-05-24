package hosp.pharm.back.dao.selector;
import hosp.pharm.back.filter.StorageFilter;
import hosp.pharm.back.model.entity.StorageEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class StorageQuerySelector extends AbstractQuerySelector<StorageEntity, StorageFilter> {

    public StorageQuerySelector(final EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected List<Predicate> createPredicates(final Root<StorageEntity> root, final StorageFilter filter) {
        final List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("active"), true));

        if(Objects.nonNull(filter.getName())) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), formLikeSentence(filter.getName()))
            );
        }

        return predicates;
    }

    @Override
    protected Class<StorageEntity> getEntityClass() {
        return StorageEntity.class;
    }
}
