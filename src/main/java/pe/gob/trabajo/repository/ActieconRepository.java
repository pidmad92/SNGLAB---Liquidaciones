package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Actiecon;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Actiecon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActieconRepository extends JpaRepository<Actiecon, Long> {

    @Query("select actiecon from Actiecon actiecon where actiecon.nFlgactivo = true")
    List<Actiecon> findAll_Activos();

}
