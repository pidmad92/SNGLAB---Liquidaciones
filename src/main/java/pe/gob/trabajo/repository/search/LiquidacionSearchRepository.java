package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Liquidacion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Liquidacion entity.
 */
public interface LiquidacionSearchRepository extends ElasticsearchRepository<Liquidacion, Long> {
}
