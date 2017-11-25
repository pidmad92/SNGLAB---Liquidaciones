package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Tipcalperi;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tipcalperi entity.
 */
public interface TipcalperiSearchRepository extends ElasticsearchRepository<Tipcalperi, Long> {
}
