package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Tipcalperi;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Tipcalperi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipcalperiRepository extends JpaRepository<Tipcalperi, Long> {

    @Query("select tipcalperi from Tipcalperi tipcalperi where tipcalperi.nFlgactivo = true")
    List<Tipcalperi> findAll_Activos();

}
