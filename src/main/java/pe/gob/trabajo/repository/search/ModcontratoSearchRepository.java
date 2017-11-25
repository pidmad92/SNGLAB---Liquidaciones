package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Modcontrato;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Modcontrato entity.
 */
public interface ModcontratoSearchRepository extends ElasticsearchRepository<Modcontrato, Long> {
}
