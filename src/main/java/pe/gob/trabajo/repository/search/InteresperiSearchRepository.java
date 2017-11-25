package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Interesperi;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Interesperi entity.
 */
public interface InteresperiSearchRepository extends ElasticsearchRepository<Interesperi, Long> {
}
