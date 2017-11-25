package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Motatenofic;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Motatenofic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotatenoficRepository extends JpaRepository<Motatenofic, Long> {

}
