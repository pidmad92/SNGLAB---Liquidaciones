package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Segsalud;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Segsalud entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SegsaludRepository extends JpaRepository<Segsalud, Long> {

}
