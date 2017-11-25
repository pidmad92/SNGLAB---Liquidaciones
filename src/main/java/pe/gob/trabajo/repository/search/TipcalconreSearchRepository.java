package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Tipcalconre;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tipcalconre entity.
 */
public interface TipcalconreSearchRepository extends ElasticsearchRepository<Tipcalconre, Long> {
}
