package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Tipdoc;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tipdoc entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipdocRepository extends JpaRepository<Tipdoc, Long> {

}
