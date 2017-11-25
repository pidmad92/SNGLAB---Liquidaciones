package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Tipdoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tipdoc entity.
 */
public interface TipdocSearchRepository extends ElasticsearchRepository<Tipdoc, Long> {
}
