package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Direcalter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Direcalter entity.
 */
public interface DirecalterSearchRepository extends ElasticsearchRepository<Direcalter, Long> {
}
