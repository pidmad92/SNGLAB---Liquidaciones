package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Bensocial;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Bensocial entity.
 */
public interface BensocialSearchRepository extends ElasticsearchRepository<Bensocial, Long> {
}
