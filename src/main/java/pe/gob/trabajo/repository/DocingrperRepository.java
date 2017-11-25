package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Docingrper;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Docingrper entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocingrperRepository extends JpaRepository<Docingrper, Long> {

}
