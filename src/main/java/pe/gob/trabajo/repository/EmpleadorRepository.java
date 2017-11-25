package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Empleador;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Empleador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmpleadorRepository extends JpaRepository<Empleador, Long> {

}
