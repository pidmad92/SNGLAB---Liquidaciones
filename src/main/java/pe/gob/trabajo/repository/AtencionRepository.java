package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Atencion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Atencion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AtencionRepository extends JpaRepository<Atencion, Long> {

}
