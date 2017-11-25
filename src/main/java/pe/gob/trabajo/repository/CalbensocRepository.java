package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Calbensoc;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Calbensoc entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalbensocRepository extends JpaRepository<Calbensoc, Long> {

}
