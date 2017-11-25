package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Conceprem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Conceprem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConcepremRepository extends JpaRepository<Conceprem, Long> {

}
