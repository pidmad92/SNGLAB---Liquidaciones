package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Tipdocident;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tipdocident entity.
 */
public interface TipdocidentSearchRepository extends ElasticsearchRepository<Tipdocident, Long> {
}
