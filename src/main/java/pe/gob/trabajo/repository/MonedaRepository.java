package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Moneda;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Moneda entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonedaRepository extends JpaRepository<Moneda, Long> {

    @Query("select moneda from Moneda moneda where moneda.nFlgactivo = true")
    List<Moneda> findAll_Activos();

}
