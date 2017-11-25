package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Conceprem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Conceprem entity.
 */
public interface ConcepremSearchRepository extends ElasticsearchRepository<Conceprem, Long> {
}
