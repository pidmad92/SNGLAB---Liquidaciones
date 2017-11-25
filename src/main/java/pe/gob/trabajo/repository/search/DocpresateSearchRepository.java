package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Docpresate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Docpresate entity.
 */
public interface DocpresateSearchRepository extends ElasticsearchRepository<Docpresate, Long> {
}
