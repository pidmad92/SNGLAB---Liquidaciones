package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Motcese;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Motcese entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotceseRepository extends JpaRepository<Motcese, Long> {

}
