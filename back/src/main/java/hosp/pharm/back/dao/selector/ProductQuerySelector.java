package hosp.pharm.back.dao.selector;

import hosp.pharm.back.filter.ProductFilter;
import hosp.pharm.back.model.entity.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ProductQuerySelector extends AbstractQuerySelector<ProductEntity, ProductFilter> {

    public ProductQuerySelector(final EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected List<Predicate> createPredicates(final Root<ProductEntity> root, final ProductFilter filter) {
        final List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("active"), true));

        if(Objects.nonNull(filter.getName())) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), formLikeSentence(filter.getName()))
            );
        }

        if(Objects.nonNull(filter.getProductType())) {
            predicates.add(criteriaBuilder.equal(root.get("productType"), filter.getProductType())
            );
        }

        if(Objects.nonNull(filter.getManufacturer())) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("manufacturer")), formLikeSentence(filter.getManufacturer()))
            );
        }

        if(Objects.nonNull(filter.getCountryOfOriginId())) {
            root.join("countryOfOrigin", JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(root.get("countryOfOrigin").get("id"), filter.getCountryOfOriginId()));
        }

        return predicates;
    }

    @Override
    protected Class<ProductEntity> getEntityClass() {
        return ProductEntity.class;
    }
}
