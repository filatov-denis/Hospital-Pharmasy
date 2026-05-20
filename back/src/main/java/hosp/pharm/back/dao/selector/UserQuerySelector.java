package hosp.pharm.back.dao.selector;

import hosp.pharm.back.filter.UserFilter;
import hosp.pharm.back.model.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserQuerySelector extends AbstractQuerySelector<UserEntity, UserFilter>{

    public UserQuerySelector(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected List<Predicate> createPredicates(final Root<UserEntity> root, final UserFilter filter) {
        final List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("active"), true));

        if(Objects.nonNull(filter.getUsername())) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("username")), formLikeSentence(filter.getUsername()))
            );
        }

        if(Objects.nonNull(filter.getName())) {
            predicates.add(criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), formLikeSentence(filter.getName())),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("middlename")), formLikeSentence(filter.getName())),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("lastname")), formLikeSentence(filter.getName())))
            );
        }

        return predicates;
    }

    @Override
    protected Class<UserEntity> getEntityClass() {
        return UserEntity.class;
    }
}
