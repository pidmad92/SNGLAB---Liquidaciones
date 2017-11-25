package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Datlab;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Datlab entity.
 */
public interface DatlabSearchRepository extends ElasticsearchRepository<Datlab, Long> {
}
