package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Discap;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Discap entity.
 */
public interface DiscapSearchRepository extends ElasticsearchRepository<Discap, Long> {
}
