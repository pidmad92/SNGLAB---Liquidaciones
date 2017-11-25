package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Accionadop;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Accionadop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccionadopRepository extends JpaRepository<Accionadop, Long> {

}
