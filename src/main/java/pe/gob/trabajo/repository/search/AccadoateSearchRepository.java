package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Accadoate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Accadoate entity.
 */
public interface AccadoateSearchRepository extends ElasticsearchRepository<Accadoate, Long> {
}
