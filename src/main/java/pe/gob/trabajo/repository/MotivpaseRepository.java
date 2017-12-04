package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Motivpase;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Motivpase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotivpaseRepository extends JpaRepository<Motivpase, Long> {

    @Query("select motivpase from Motivpase motivpase where motivpase.nFlgactivo = true")
    List<Motivpase> findAll_Activos();

    @Query("select motivpase from Motivpase motivpase where motivpase.pasegl.id=?1 and motivpase.nFlgactivo = true")
    List<Motivpase> findListMotivPaseById_Pasegl(Long id);

}
