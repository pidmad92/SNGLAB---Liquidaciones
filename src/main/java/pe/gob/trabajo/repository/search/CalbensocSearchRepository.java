package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Calbensoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Calbensoc entity.
 */
public interface CalbensocSearchRepository extends ElasticsearchRepository<Calbensoc, Long> {
}
