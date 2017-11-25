package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Sucesor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Sucesor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SucesorRepository extends JpaRepository<Sucesor, Long> {

}
