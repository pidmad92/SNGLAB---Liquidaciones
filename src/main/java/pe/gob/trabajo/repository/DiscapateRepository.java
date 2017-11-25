package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Discapate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Discapate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiscapateRepository extends JpaRepository<Discapate, Long> {

}
