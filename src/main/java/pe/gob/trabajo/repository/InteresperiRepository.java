package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Interesperi;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Interesperi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InteresperiRepository extends JpaRepository<Interesperi, Long> {

    @Query("select interesperi from Interesperi interesperi where interesperi.nFlgactivo = true")
    List<Interesperi> findAll_Activos();

    @Query("select interesperi " + 
    " from Interesperi interesperi " + 
    " where interesperi.calperiodo.id=?1 and interesperi.nFlgactivo = true")
    Interesperi findInteresperiByIdCalperiodo(Long id_calper);

}
