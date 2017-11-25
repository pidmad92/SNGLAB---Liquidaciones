package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Sucesor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Sucesor entity.
 */
public interface SucesorSearchRepository extends ElasticsearchRepository<Sucesor, Long> {
}
