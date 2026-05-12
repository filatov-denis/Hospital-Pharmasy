package hosp.pharm.back.dao.selector;

import hosp.pharm.back.filter.RequestFilter;
import hosp.pharm.back.model.entity.RequestEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequestQuerySelector extends AbstractQuerySelector<RequestEntity, RequestFilter> {

    public RequestQuerySelector(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected List<Predicate> createPredicates(Root<RequestEntity> root, RequestFilter filter) {
        return List.of();
    }

    @Override
    protected Class<RequestEntity> getEntityClass() {
        return RequestEntity.class;
    }
}
