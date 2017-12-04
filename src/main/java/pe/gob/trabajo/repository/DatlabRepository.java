package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Datlab;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Datlab entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DatlabRepository extends JpaRepository<Datlab, Long> {

    @Query("select datlab from Datlab datlab where datlab.nFlgactivo = true")
    List<Datlab> findAll_Activos();

    @Query("select datlab from Datlab datlab where datlab.trabajador.id=?1 and datlab.nFlgactivo = true order by datlab.tFecreg desc")
    List<Datlab> findListDatlaboralByIdTrabajador(Long id);

    @Query("select datlab from Datlab datlab where datlab.trabajador.id=?1 and datlab.empleador.id=?2 and datlab.nFlgactivo = true order by datlab.tFecreg desc")
    List<Datlab> findListDatlaboralBy_IdTrabajador_IdEmpleador(Long id_trab,Long id_empl);

}
