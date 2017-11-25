package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Calperiodo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Calperiodo entity.
 */
public interface CalperiodoSearchRepository extends ElasticsearchRepository<Calperiodo, Long> {
}
