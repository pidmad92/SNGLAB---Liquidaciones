package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Motateselec;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Motateselec entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotateselecRepository extends JpaRepository<Motateselec, Long> {

    @Query("select motateselec from Motateselec motateselec where motateselec.nFlgactivo = true")
    List<Motateselec> findAll_Activos();

    @Query("select motateselec from Motateselec motateselec where motateselec.atencion.id=?1 and motateselec.nFlgactivo = true")
    List<Motateselec> findListMotateselecById_Atencion(Long id);

}
