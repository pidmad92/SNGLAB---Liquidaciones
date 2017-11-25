package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Modcontrato;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Modcontrato entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModcontratoRepository extends JpaRepository<Modcontrato, Long> {

}
