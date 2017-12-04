package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Tipmotaten;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Tipmotaten entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipmotatenRepository extends JpaRepository<Tipmotaten, Long> {

    @Query("select tipmotaten from Tipmotaten tipmotaten where tipmotaten.nFlgactivo = true")
    List<Tipmotaten> findAll_Activos();
    
}
