package hosp.pharm.back.dao.selector;

import hosp.pharm.back.constant.RoleName;
import hosp.pharm.back.filter.RequestFilter;
import hosp.pharm.back.model.entity.*;
import hosp.pharm.back.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class RequestQuerySelector extends AbstractQuerySelector<RequestEntity, RequestFilter> {

    private final UserService userService;

    public RequestQuerySelector(final EntityManager entityManager, final UserService userService) {
        super(entityManager);
        this.userService = userService;
    }

    @Override
    protected List<Predicate> createPredicates(final Root<RequestEntity> root, final RequestFilter filter) {
        final List<Predicate> predicates = new ArrayList<>();

        final UserEntity current = userService.getCurrentUser();
        if(current.getRole().equals(RoleName.ROLE_NURSE)) {
            filter.setCreatorId(current.getId());
        }

        if(Objects.nonNull(filter.getCreatorId())) {
            final Join<RequestEntity, UserEntity> creator = root.join("creator", JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(creator.get("id"), filter.getCreatorId()));
        }

        if(Objects.nonNull(filter.getStatus())) {
            predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus().name()));
        }

        if(Objects.nonNull(filter.getProductName())) {
            final Join<RequestEntity, RequestBatchEntity> requestBatch = root.join("requestBatch", JoinType.LEFT);
            final Join<RequestBatchEntity, BatchEntity> targetBatch = requestBatch.join("targetBatch", JoinType.LEFT);
            final Join<RequestBatchEntity, BatchEntity> sourceBatch = requestBatch.join("sourceBatch", JoinType.LEFT);
            final Join<BatchEntity, ProductEntity> targetProduct = targetBatch.join("product", JoinType.LEFT);
            final Join<BatchEntity, ProductEntity> sourceProduct = sourceBatch.join("product", JoinType.LEFT);

            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(targetProduct.get("name")), formLikeSentence(filter.getProductName())),
                    criteriaBuilder.like(criteriaBuilder.lower(sourceProduct.get("name")), formLikeSentence(filter.getProductName())))
            );
        }

        if(Objects.nonNull(filter.getCreationDateFrom())) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("creationDate"), filter.getCreationDateFrom()));
        }

        if(Objects.nonNull(filter.getCreationDateTo())) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("creationTime"), filter.getCreationDateTo()));
        }

        return predicates;
    }

    @Override
    protected Class<RequestEntity> getEntityClass() {
        return RequestEntity.class;
    }
}
