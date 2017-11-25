package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Motatenofic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Motatenofic entity.
 */
public interface MotatenoficSearchRepository extends ElasticsearchRepository<Motatenofic, Long> {
}
