package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Estperical;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Estperical entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstpericalRepository extends JpaRepository<Estperical, Long> {

}
