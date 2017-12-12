package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Liquidacion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Liquidacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LiquidacionRepository extends JpaRepository<Liquidacion, Long> {

    @Query("select liquidacion from Liquidacion liquidacion where liquidacion.nFlgactivo = true")
    List<Liquidacion> findAll_Activos();

}
