package hosp.pharm.back.dao.selector;

import hosp.pharm.back.filter.AbstractFilter;
import hosp.pharm.back.model.entity.AbstractEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractQuerySelector<T extends AbstractEntity, F extends AbstractFilter> {

    protected final EntityManager entityManager;

    protected final CriteriaBuilder criteriaBuilder;

    public AbstractQuerySelector(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<T> getByDynamicFilter(final F filter, final Pageable pageable) {
        CriteriaQuery<T> cq = criteriaBuilder.createQuery(getEntityClass());
        Root<T> root = cq.from(getEntityClass());

        List<Predicate> predicates = createPredicates(root, filter);

        cq.select(root);
        cq.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        //cq.orderBy(criteriaBuilder.asc(root.get(ID.getValue())));
        TypedQuery<T> query = entityManager.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        if(StringUtils.isNotEmpty(getFetchGraph())) {
            query.setHint("jakarta.persistence.fetchgraph", entityManager.getEntityGraph(getFetchGraph()));
        }

        List<T> results = query.getResultList();

        return new PageImpl<>(results, pageable, getCountOfElements(filter));
    }

    private long getCountOfElements(final F filter) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(getEntityClass());
        List<Predicate> predicates = createPredicates(countRoot, filter);

        countQuery.select(criteriaBuilder.count(countRoot)).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    protected abstract List<Predicate> createPredicates(final Root<T> root, final F filter);

    protected String formLikeSentence(String data) {return ("%" + data + "%").toLowerCase();}

    protected String getFetchGraph() {
        return "";
    }

    abstract protected Class<T> getEntityClass();

}
