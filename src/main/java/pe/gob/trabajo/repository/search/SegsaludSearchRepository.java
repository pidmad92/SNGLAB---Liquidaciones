package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Segsalud;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Segsalud entity.
 */
public interface SegsaludSearchRepository extends ElasticsearchRepository<Segsalud, Long> {
}
