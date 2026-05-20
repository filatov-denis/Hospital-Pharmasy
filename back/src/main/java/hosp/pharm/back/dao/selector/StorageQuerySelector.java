package hosp.pharm.back.dao.selector;

import hosp.pharm.back.constant.RoleName;
import hosp.pharm.back.filter.StorageFilter;
import hosp.pharm.back.model.entity.StorageEntity;
import hosp.pharm.back.model.entity.UserEntity;
import hosp.pharm.back.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class StorageQuerySelector extends AbstractQuerySelector<StorageEntity, StorageFilter> {

    private final UserService userService;

    public StorageQuerySelector(final EntityManager entityManager, final UserService userService) {
        super(entityManager);
        this.userService = userService;
    }

    @Override
    protected List<Predicate> createPredicates(final Root<StorageEntity> root, final StorageFilter filter) {
        final List<Predicate> predicates = new ArrayList<>();

        final UserEntity current = userService.getCurrentUser();
        if(current.getRole().equals(RoleName.ROLE_NURSE)) {
            final List<Long> ids = new ArrayList<>();
            ids.add(1L);

            if(current.getStorage() != null) {
                ids.add(current.getId());
            }

            predicates.add(root.get("id").in(ids));
        }

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
