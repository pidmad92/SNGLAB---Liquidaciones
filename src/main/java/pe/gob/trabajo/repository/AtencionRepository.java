package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Atencion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Atencion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AtencionRepository extends JpaRepository<Atencion, Long> {

    @Query("select atencion from Atencion atencion where atencion.nFlgactivo = true")
    List<Atencion> findAll_Activos();

    @Query("select atencion from Atencion atencion where (atencion.trabajador.id=?1 or atencion.datlab.trabajador.id=?1) and atencion.nFlgactivo = true order by atencion.tFecreg desc")
    List<Atencion> findListAtencionByIdTrabajador(Long id);

    // @Query("select atencion from Atencion atencion where atencion.empleador.id=?1 and atencion.nCodtrepre > 0 and atencion.nFlgactivo = true order by atencion.tFecreg desc")
    @Query("select new  map(atencion as aten, " + 
        "(select repre.pernatural.vNombres || ' ' || repre.pernatural.vApepat || ' ' || repre.pernatural.vApemat " + 
        "from Trabajador repre where repre.id=atencion.nCodtrepre) as nomrepre) " + 
        "from Atencion atencion where (atencion.datlab.empleador.id=?1 or atencion.empleador.id=?1) and atencion.nCodtrepre > 0 and atencion.nFlgactivo = true order by atencion.tFecreg desc")
    List<Atencion> findListAtencionByIdEmpleador(Long id);

// listar la ultima atencion de cada vinculo laboral de un trabajador en cuaquier oficina
    // @Query("select atencion.datlab.dFecvincul, atencion.datlab.empleador.perjuridica.vRazsocial, atencion.id, atencion.tFecreg, atencion.oficina.vDesofic, motivpase.motatenofic.motate.vDesmotate from (Atencion atencion inner join Pasegl pasegl on atencion.id=pasegl.atencion.id) inner join MotivPase motivpase on pasegl.id=motivpase.pasegl.id where atencion.datlab is not null and (atencion.trabajador.id=?1 or atencion.datlab.trabajador.id=?1) and atencion.nFlgactivo=true and pasegl.nFlgactivo=true order by atencion.tFecreg desc")
    @Query("select new map(atencion as aten " + 
    //", (select motateselec from Motateselec motateselec where motateselec.atencion.id=atencion.id) as motivoaten " + 
    ", (select pasegl from Pasegl pasegl where pasegl.atencion.id=atencion.id) as pase " +
    //", (select motivpase from Motivpase motivpase where motivpase.pasegl.atencion.id=atencion.id) as motivopase " +
    " ) "+
    "from Atencion atencion "+
    // "where atencion.trabajador.id=?1 or atencion.datlab.trabajador.id=?1 and atencion.nFlgactivo=true order by atencion.tFecreg desc")
    "where atencion.datlab.trabajador.id=?1 and atencion.nFlgactivo=true order by atencion.tFecreg desc")
    List<Atencion> findListAtenPase_ByIdTrabajador(Long id_trab);

    @Query("select atencion " + 
            "from Atencion atencion " + 
            "where atencion.datlab.trabajador.id=?1 and atencion.liquidacion.id > 0 and atencion.nFlgactivo = true order by atencion.liquidacion.tFecreg desc")
    List<Atencion> findLiquidaciones_ByIdTrabajador(Long id);

}
