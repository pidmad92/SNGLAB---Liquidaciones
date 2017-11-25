package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Accionadop;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Accionadop entity.
 */
public interface AccionadopSearchRepository extends ElasticsearchRepository<Accionadop, Long> {
}
