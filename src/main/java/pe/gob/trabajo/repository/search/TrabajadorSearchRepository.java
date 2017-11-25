package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Trabajador;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Trabajador entity.
 */
public interface TrabajadorSearchRepository extends ElasticsearchRepository<Trabajador, Long> {
}
