package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Tippersona;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tippersona entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TippersonaRepository extends JpaRepository<Tippersona, Long> {

}
