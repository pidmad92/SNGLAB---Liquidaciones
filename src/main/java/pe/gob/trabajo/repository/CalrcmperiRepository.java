package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Calrcmperi;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.math.BigDecimal;


/**
 * Spring Data JPA repository for the Calrcmperi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalrcmperiRepository extends JpaRepository<Calrcmperi, Long> {

    @Query("select calrcmperi from Calrcmperi calrcmperi where calrcmperi.nFlgactivo = true")
    List<Calrcmperi> findAll_Activos();

    @Query("select new map(calrcmperi.nCalrcmper as nCalrcmper) " + 
            "from Calrcmperi calrcmperi " + 
            "where calrcmperi.calperiodo.id=?1 and calrcmperi.nFlgactivo = true")
    List<Calrcmperi> find_RCM_ByIdCalperiodo(Long id_calper);

}
