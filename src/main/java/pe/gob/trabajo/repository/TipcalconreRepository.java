package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Tipcalconre;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tipcalconre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipcalconreRepository extends JpaRepository<Tipcalconre, Long> {

}
