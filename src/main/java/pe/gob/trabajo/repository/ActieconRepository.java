package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Actiecon;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Actiecon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActieconRepository extends JpaRepository<Actiecon, Long> {

}
