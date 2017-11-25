package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Tipmotaten;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tipmotaten entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipmotatenRepository extends JpaRepository<Tipmotaten, Long> {

}
