package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Tipatencion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tipatencion entity.
 */
public interface TipatencionSearchRepository extends ElasticsearchRepository<Tipatencion, Long> {
}
