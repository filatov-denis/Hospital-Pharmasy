package hosp.pharm.back.dao.selector;

import hosp.pharm.back.constant.RoleName;
import hosp.pharm.back.filter.BatchFilter;
import hosp.pharm.back.model.entity.BatchEntity;
import hosp.pharm.back.model.entity.StorageEntity;
import hosp.pharm.back.model.entity.UserEntity;
import hosp.pharm.back.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class BatchQuerySelector extends AbstractQuerySelector<BatchEntity, BatchFilter>{

    private final UserService userService;

    public BatchQuerySelector(final EntityManager entityManager, final UserService userService) {
        super(entityManager);
        this.userService = userService;
    }
    @Override
    protected List<Predicate> createPredicates(final Root<BatchEntity> root, final BatchFilter filter) {
        final List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("active"), true));

        final UserEntity current = userService.getCurrentUser();
        if(current.getRole().equals(RoleName.ROLE_NURSE)) {
            final StorageEntity storage = current.getStorage();
            long storageId = storage != null ? storage.getId() : 0L;
            filter.setStorageId(storageId);
        }

        if(Objects.nonNull(filter.getStorageId())) {
            root.join("storage", JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(root.get("storage").get("id"), filter.getStorageId()));
        }

        return predicates;
    }

    @Override
    protected Class<BatchEntity> getEntityClass() {
        return BatchEntity.class;
    }
}
