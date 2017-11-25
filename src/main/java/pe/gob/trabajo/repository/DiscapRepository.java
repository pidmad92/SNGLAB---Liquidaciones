package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Discap;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Discap entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiscapRepository extends JpaRepository<Discap, Long> {

}
