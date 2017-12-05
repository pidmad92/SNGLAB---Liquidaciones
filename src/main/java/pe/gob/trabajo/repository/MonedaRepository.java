package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Moneda;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Moneda entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonedaRepository extends JpaRepository<Moneda, Long> {

}
