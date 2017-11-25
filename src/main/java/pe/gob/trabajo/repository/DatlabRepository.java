package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Datlab;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Datlab entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DatlabRepository extends JpaRepository<Datlab, Long> {

}
