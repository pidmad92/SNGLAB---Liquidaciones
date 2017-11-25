package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Perjuridica;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Perjuridica entity.
 */
public interface PerjuridicaSearchRepository extends ElasticsearchRepository<Perjuridica, Long> {
}
