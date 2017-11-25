package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Accadoate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Accadoate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccadoateRepository extends JpaRepository<Accadoate, Long> {

}