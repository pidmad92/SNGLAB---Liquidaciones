package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Tipdoc;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Tipdoc entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipdocRepository extends JpaRepository<Tipdoc, Long> {

    @Query("select tipdoc from Tipdoc tipdoc where tipdoc.nFlgactivo = true")
    List<Tipdoc> findAll_Activos();

}
