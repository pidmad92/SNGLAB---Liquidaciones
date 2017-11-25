package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Pernatural;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Pernatural entity.
 */
public interface PernaturalSearchRepository extends ElasticsearchRepository<Pernatural, Long> {
}
