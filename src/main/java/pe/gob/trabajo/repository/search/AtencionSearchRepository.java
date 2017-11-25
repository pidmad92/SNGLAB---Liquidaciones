package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Atencion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Atencion entity.
 */
public interface AtencionSearchRepository extends ElasticsearchRepository<Atencion, Long> {
}
