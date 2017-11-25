package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Calperiodo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Calperiodo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalperiodoRepository extends JpaRepository<Calperiodo, Long> {

}
