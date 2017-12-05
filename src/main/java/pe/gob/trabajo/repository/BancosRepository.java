package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Bancos;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Bancos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BancosRepository extends JpaRepository<Bancos, Long> {

}
