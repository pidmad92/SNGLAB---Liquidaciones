package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Motateselec;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Motateselec entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotateselecRepository extends JpaRepository<Motateselec, Long> {

}
