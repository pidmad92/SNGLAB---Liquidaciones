package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Bancos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Bancos entity.
 */
public interface BancosSearchRepository extends ElasticsearchRepository<Bancos, Long> {
}
