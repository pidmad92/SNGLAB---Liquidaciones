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

    @Query("select new map(datlab as datolab " + 
    // ", (select TOP 1 atencion from Atencion aten where aten.datlab.id=datlab.id and atencion.nFlgactivo = true order by atencion.tFecreg desc) as ultaten " + 
    // ", (select TOP 1 aten from Atencion aten where aten.datlab.id=datlab.id and aten.nFlgactivo = true order by aten.tFecreg desc) as ultaten " + 
    //", (select motateselec from Motateselec motateselec where motateselec.atencion.id=atencion.id) as motivoaten " + 
    //", (select pasegl from Pasegl pasegl where pasegl.atencion.id=atencion.id) as pase " +
    //", (select motivpase from Motivpase motivpase where motivpase.pasegl.atencion.id=atencion.id) as motivopase " +
    " ) "+
    " from Datlab datlab "+
    " where datlab.trabajador.id=?1 and datlab.nFlgactivo = true order by datlab.dFecvincul desc")
    List<Datlab> findListDatLab_UltAtencion_ByIdTrabajador(Long id_trab);

    @Query("select atencion.datlab from Atencion atencion where atencion.id=?1 and atencion.nFlgactivo = true")
    Datlab findDatlaboralByIdAtencion(Long id);

}
