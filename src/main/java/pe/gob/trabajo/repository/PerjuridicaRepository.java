package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Perjuridica;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Perjuridica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PerjuridicaRepository extends JpaRepository<Perjuridica, Long> {

}
