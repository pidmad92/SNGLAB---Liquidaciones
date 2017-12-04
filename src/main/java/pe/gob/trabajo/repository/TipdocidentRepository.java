package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Tipdocident;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Tipdocident entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipdocidentRepository extends JpaRepository<Tipdocident, Long> {

    @Query("select tipdocident from Tipdocident tipdocident where tipdocident.nFlgactivo = true")
    List<Tipdocident> findAll_Activos();

}
