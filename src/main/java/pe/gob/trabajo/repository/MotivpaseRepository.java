package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Motivpase;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Motivpase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotivpaseRepository extends JpaRepository<Motivpase, Long> {

}
