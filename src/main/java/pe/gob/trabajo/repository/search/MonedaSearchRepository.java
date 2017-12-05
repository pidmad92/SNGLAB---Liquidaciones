package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Moneda;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Moneda entity.
 */
public interface MonedaSearchRepository extends ElasticsearchRepository<Moneda, Long> {
}
