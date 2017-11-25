package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Calrcmperi;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Calrcmperi entity.
 */
public interface CalrcmperiSearchRepository extends ElasticsearchRepository<Calrcmperi, Long> {
}
