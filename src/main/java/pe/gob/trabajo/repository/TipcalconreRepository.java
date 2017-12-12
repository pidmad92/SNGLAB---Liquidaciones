package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Tipcalconre;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Tipcalconre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipcalconreRepository extends JpaRepository<Tipcalconre, Long> {

    @Query("select tipcalconre from Tipcalconre tipcalconre where tipcalconre.nFlgactivo = true")
    List<Tipcalconre> findAll_Activos();

}
