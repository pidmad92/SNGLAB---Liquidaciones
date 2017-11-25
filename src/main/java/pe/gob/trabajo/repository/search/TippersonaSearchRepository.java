package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Tippersona;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tippersona entity.
 */
public interface TippersonaSearchRepository extends ElasticsearchRepository<Tippersona, Long> {
}
