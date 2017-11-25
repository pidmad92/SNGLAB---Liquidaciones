package pe.gob.trabajo.repository.search;

import pe.gob.trabajo.domain.Motateselec;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Motateselec entity.
 */
public interface MotateselecSearchRepository extends ElasticsearchRepository<Motateselec, Long> {
}
