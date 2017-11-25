package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Interesperi;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Interesperi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InteresperiRepository extends JpaRepository<Interesperi, Long> {

}
