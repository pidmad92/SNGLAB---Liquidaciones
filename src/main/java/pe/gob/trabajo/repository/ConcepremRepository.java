package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Conceprem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Conceprem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConcepremRepository extends JpaRepository<Conceprem, Long> {

    @Query("select conceprem from Conceprem conceprem where conceprem.nFlgactivo = true")
    List<Conceprem> findAll_Activos();

    @Query("select conceprem " + 
        //     "from Conceprem conceprem inner join Calrcmperi calrcmperi on calrcmperi.id=conceprem.calrcmperi.id " + 
        "from Conceprem conceprem " + 
        "where conceprem.calrcmperi.calperiodo.id=?1 and conceprem.nFlgactivo = true ")
            // "where calrcmperi.calperiodo.id=?1 and conceprem.nCodcrsup <= 0 and conceprem.nFlgactivo = true and calrcmperi.nFlgactivo = true")
        //     "where calrcmperi.calperiodo.id=?1 and conceprem.nFlgactivo = true and calrcmperi.nFlgactivo = true")
    List<Conceprem> findListConceprem_ByIdCalperiodo(Long id_calper);

    @Query("select conceprem " + 
            "from Conceprem conceprem " + 
            // "where conceprem.tipcalconre.id=?2 and conceprem.nCodcrsup=?1 and conceprem.nFlgactivo = true")
            "where conceprem.tipcalconre.id=?2 and conceprem.nFlgactivo = true")
    List<Conceprem> findListConcepremHijo_ByIdPadreIdTipo(Long id_concep,Long id_tipccr);

}
