package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Regimenlab;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Regimenlab entity.
 */
public interface RegimenlabSearchRepository extends ElasticsearchRepository<Regimenlab, Long> {
}
