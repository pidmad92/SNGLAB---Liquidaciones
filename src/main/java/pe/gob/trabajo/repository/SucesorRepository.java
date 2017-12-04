package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Sucesor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Sucesor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SucesorRepository extends JpaRepository<Sucesor, Long> {

    @Query("select sucesor from Sucesor sucesor where sucesor.nFlgactivo = true")
    List<Sucesor> findAll_Activos();

    @Query("select sucesor from Sucesor sucesor where sucesor.trabajador.id=?1 and sucesor.nFlgactivo = true order by sucesor.tFecreg desc")
    Sucesor findSucesorBy_IdTrabajador(Long id_trab);

}
