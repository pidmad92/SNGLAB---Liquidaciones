package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Pernatural;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Pernatural entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PernaturalRepository extends JpaRepository<Pernatural, Long> {

}
