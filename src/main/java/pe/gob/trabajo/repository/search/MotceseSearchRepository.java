package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Motcese;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Motcese entity.
 */
public interface MotceseSearchRepository extends ElasticsearchRepository<Motcese, Long> {
}
