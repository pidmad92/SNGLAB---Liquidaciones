package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Tipinteres;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tipinteres entity.
 */
public interface TipinteresSearchRepository extends ElasticsearchRepository<Tipinteres, Long> {
}
