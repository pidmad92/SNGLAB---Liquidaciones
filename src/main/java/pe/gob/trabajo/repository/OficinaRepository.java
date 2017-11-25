package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Oficina;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Oficina entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OficinaRepository extends JpaRepository<Oficina, Long> {

}
