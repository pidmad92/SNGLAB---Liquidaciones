package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Discapate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Discapate entity.
 */
public interface DiscapateSearchRepository extends ElasticsearchRepository<Discapate, Long> {
}
