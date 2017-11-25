package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Motate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Motate entity.
 */
public interface MotateSearchRepository extends ElasticsearchRepository<Motate, Long> {
}
