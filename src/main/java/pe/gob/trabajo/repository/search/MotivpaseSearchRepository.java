package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Motivpase;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Motivpase entity.
 */
public interface MotivpaseSearchRepository extends ElasticsearchRepository<Motivpase, Long> {
}
