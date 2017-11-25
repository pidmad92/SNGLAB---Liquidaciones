package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Tipconrem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tipconrem entity.
 */
public interface TipconremSearchRepository extends ElasticsearchRepository<Tipconrem, Long> {
}
