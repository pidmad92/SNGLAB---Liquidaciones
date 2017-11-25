package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Bensocial;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Bensocial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BensocialRepository extends JpaRepository<Bensocial, Long> {

}
