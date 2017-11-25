package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Regimenlab;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Regimenlab entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegimenlabRepository extends JpaRepository<Regimenlab, Long> {

}
