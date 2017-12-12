package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Calrcmperi;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Calrcmperi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalrcmperiRepository extends JpaRepository<Calrcmperi, Long> {

    @Query("select calrcmperi from Calrcmperi calrcmperi where calrcmperi.nFlgactivo = true")
    List<Calrcmperi> findAll_Activos();

}
