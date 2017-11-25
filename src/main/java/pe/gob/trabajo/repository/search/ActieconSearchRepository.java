package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Actiecon;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Actiecon entity.
 */
public interface ActieconSearchRepository extends ElasticsearchRepository<Actiecon, Long> {
}
