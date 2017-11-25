package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Oficina;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Oficina entity.
 */
public interface OficinaSearchRepository extends ElasticsearchRepository<Oficina, Long> {
}
