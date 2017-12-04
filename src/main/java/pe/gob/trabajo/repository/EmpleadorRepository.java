package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Empleador;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Empleador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmpleadorRepository extends JpaRepository<Empleador, Long> {

    @Query("select empleador from Empleador empleador where empleador.nFlgactivo = true")
    List<Empleador> findAll_Activos();

    @Query("select empleador from Empleador empleador where empleador.pernatural.tipdocident.id=?1 and  empleador.pernatural.vNumdoc=?2 and empleador.nFlgactivo = true")
    Empleador findEmpleadorPerNaturalByIdentDoc(Long id_tdoc,String ndoc);

    @Query("select empleador from Empleador empleador where empleador.perjuridica.tipdocident.id=?1 and  empleador.perjuridica.vNumdoc=?2 and empleador.nFlgactivo = true")
    Empleador findEmpleadorPerJuridByIdentDoc(Long id_tdoc,String ndoc);

    @Query("select empleador from Empleador empleador where empleador.perjuridica.vRazsocial like %?1% and empleador.nFlgactivo = true")
    List<Empleador> findEmpleadordByRazsocial(String razsoc);

}
