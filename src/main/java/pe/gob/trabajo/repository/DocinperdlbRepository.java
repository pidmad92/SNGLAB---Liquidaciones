package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Docinperdlb;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Docinperdlb entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocinperdlbRepository extends JpaRepository<Docinperdlb, Long> {

}
