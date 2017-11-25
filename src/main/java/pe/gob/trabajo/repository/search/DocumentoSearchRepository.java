package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Documento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Documento entity.
 */
public interface DocumentoSearchRepository extends ElasticsearchRepository<Documento, Long> {
}
