package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Dirpernat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Dirpernat entity.
 */
public interface DirpernatSearchRepository extends ElasticsearchRepository<Dirpernat, Long> {
}
