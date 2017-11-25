package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Tipvinculo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tipvinculo entity.
 */
public interface TipvinculoSearchRepository extends ElasticsearchRepository<Tipvinculo, Long> {
}
