package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Tipmotaten;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tipmotaten entity.
 */
public interface TipmotatenSearchRepository extends ElasticsearchRepository<Tipmotaten, Long> {
}
