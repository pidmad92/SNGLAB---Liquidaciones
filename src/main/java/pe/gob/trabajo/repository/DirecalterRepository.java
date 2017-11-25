package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Direcalter;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Direcalter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirecalterRepository extends JpaRepository<Direcalter, Long> {

}