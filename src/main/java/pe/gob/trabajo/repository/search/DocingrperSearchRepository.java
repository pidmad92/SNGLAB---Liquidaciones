package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Docingrper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Docingrper entity.
 */
public interface DocingrperSearchRepository extends ElasticsearchRepository<Docingrper, Long> {
}
