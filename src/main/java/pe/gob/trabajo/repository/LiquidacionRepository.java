package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Liquidacion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Liquidacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LiquidacionRepository extends JpaRepository<Liquidacion, Long> {

}
