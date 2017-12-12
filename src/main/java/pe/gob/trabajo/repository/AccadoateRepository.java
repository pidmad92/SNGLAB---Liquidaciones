package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Accadoate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Accadoate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccadoateRepository extends JpaRepository<Accadoate, Long> {

    @Query("select accadoate from Accadoate accadoate where accadoate.atencion.id=?1 and accadoate.nFlgactivo = true")
    List<Accadoate> findListAccionesAdoptadasById_Atencion(Long id_aten);

    @Query("select accadoate from Accadoate accadoate where accadoate.nFlgactivo = true")
    List<Accadoate> findAll_Activos();

    @Query("select new map(accionadop.id as nCodacadop, accadoate.atencion.id as nCodaten, " + 
                        "accionadop.vDesaccdop as vDesaccdop, accadoate.id as nCodatacad, " + 
                        "accadoate.nFlgactivo as accadoate_nFlgactivo)" +
    // @Query("select new map(accionadop as Accionadop, accadoate.id as nCodatacad) " +
    " from Accionadop accionadop left join Accadoate accadoate " + 
            "on (accionadop.id=accadoate.accionadop.id and accadoate.atencion.id=?1 and accadoate.nFlgactivo=true) ")
    List<Accadoate> findListAccionadop_ySeleccionadosByIdAtencion(Long id_aten);

}
