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

        if(Objects.nonNull(filter.getName())) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), formLikeSentence(filter.getName()))
            );
        }

        if(Objects.nonNull(filter.getProductType())) {
            predicates.add(criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("productType")), filter.getProductType())
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

//        if(Objects.nonNull(filter.getCountryManufactureId())) {
//            root.join(ADDRESS, JoinType.LEFT);
//            predicates.add(criteriaBuilder.like(
//                    criteriaBuilder.lower(root.get(ADDRESS).get(COUNTRY)), formLikeSentence(filter.getCountry()))
//            );
//        }
//
//        if(Objects.nonNull(filter.getAmenities()) && !filter.getAmenities().isEmpty()) {
//            List<String> likeNames = new ArrayList<>();
//            for(String amenity : filter.getAmenities()) likeNames.add(amenity.toLowerCase());
//
//            Subquery<Long> subquery = query.subquery(Long.class);
//            Root<Hotel> subHotelRoot = subquery.from(Hotel.class);
//            Join<Hotel, Amenity> amenityJoin = subHotelRoot.join(AMENITIES);
//
//            subquery.select(criteriaBuilder.countDistinct(amenityJoin.get(NAME)))
//                    .where(
//                            criteriaBuilder.and(
//                                    criteriaBuilder.equal(subHotelRoot.get(ID), root.get(ID)),
//                                    criteriaBuilder.lower(amenityJoin.get(NAME)).in(likeNames)
//                            )
//                    )
//                    .groupBy(subHotelRoot.get(ID));
//
//            predicates.add(criteriaBuilder.equal(subquery, (long) filter.getAmenities().size()));
//        }

        return predicates;
    }

    @Override
    protected Class<ProductEntity> getEntityClass() {
        return ProductEntity.class;
    }
}
