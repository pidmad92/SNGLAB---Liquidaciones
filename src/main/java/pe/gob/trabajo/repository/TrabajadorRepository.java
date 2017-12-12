package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Trabajador;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Trabajador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, Long> {

    @Query("select trabajador from Trabajador trabajador where trabajador.nFlgactivo = true")
    List<Trabajador> findAll_Activos();

    @Query("select trabajador " +
            " from Trabajador trabajador " +
            " where trabajador.pernatural.tipdocident.id=?1 and  trabajador.pernatural.vNumdoc=?2 and trabajador.nFlgactivo = true")
    Trabajador findTrabajadorByIdentDoc(Long id_tdoc,String ndoc);

    // @Query("select trabajador " + 
    @Query("select new map(trabajador as Trabajador, sucesor.vCodpartid as vCodpartid) " +
            // "from Trabajador trabajador " + 
            " from Trabajador trabajador left join Sucesor sucesor on sucesor.trabajador.id=trabajador.id and sucesor.nFlgactivo = true " + 
            "where trabajador.pernatural.tipdocident.id=?1 and  trabajador.pernatural.vNumdoc like ?2% and trabajador.nFlgactivo = true")
    List<Trabajador> findListTrabajadorByIdentDoc(Long id_tdoc,String ndoc);

    @Query("select trabajador from Trabajador trabajador where trabajador.pernatural.vNombres like ?1% and  trabajador.pernatural.vApepat like ?2% and trabajador.pernatural.vApemat like ?3% and trabajador.nFlgactivo = true")
    List<Trabajador> findListTrabajadorByName(String nombres, String apepat, String apemat);

}
