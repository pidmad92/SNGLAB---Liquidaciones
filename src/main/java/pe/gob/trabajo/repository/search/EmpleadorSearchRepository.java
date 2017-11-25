package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Empleador;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Empleador entity.
 */
public interface EmpleadorSearchRepository extends ElasticsearchRepository<Empleador, Long> {
}
