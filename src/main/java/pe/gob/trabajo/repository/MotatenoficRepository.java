package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Motatenofic;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Motatenofic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotatenoficRepository extends JpaRepository<Motatenofic, Long> {
    @Query("select motatenofic from Motatenofic motatenofic where motatenofic.nFlgactivo = true")
    List<Motatenofic> findAll_Activos();

    @Query("select motatenofic from Motatenofic motatenofic where motatenofic.oficina.id=?1 and motatenofic.nFlgactivo = true")
    List<Motatenofic> findListMotivosAtencionById_Oficina(Long id);
}
