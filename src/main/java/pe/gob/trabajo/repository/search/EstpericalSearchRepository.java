package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Estperical;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Estperical entity.
 */
public interface EstpericalSearchRepository extends ElasticsearchRepository<Estperical, Long> {
}
