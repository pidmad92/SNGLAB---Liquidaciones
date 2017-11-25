package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Pasegl;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Pasegl entity.
 */
public interface PaseglSearchRepository extends ElasticsearchRepository<Pasegl, Long> {
}
