package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Cartrab;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Cartrab entity.
 */
public interface CartrabSearchRepository extends ElasticsearchRepository<Cartrab, Long> {
}
