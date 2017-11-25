package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Dirperjuri;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Dirperjuri entity.
 */
public interface DirperjuriSearchRepository extends ElasticsearchRepository<Dirperjuri, Long> {
}
