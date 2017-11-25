package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Docinperdlb;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Docinperdlb entity.
 */
public interface DocinperdlbSearchRepository extends ElasticsearchRepository<Docinperdlb, Long> {
}
