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
        "from Atencion atencion where atencion.empleador.id=?1 and atencion.nCodtrepre > 0 and atencion.nFlgactivo = true order by atencion.tFecreg desc")
    List<Atencion> findListAtencionByIdEmpleador(Long id);

    // @Query("select atencion.datlab.dFecvincul, atencion.datlab.empleador.perjuridica.vRazsocial, atencion.id, atencion.tFecreg, atencion.oficina.vDesofic, motivpase.motatenofic.motate.vDesmotate from (Atencion atencion inner join Pasegl pasegl on atencion.id=pasegl.atencion.id) inner join MotivPase motivpase on pasegl.id=motivpase.pasegl.id where atencion.datlab is not null and (atencion.trabajador.id=?1 or atencion.datlab.trabajador.id=?1) and atencion.nFlgactivo=true and pasegl.nFlgactivo=true order by atencion.tFecreg desc")
    // @Query("select new map(atencion as aten, motateselec as motiaten, pasegl as pase, motivpase as motipase) from Atencion as atencion inner join Motateselec as motateselec on atencion.id=motateselect.atencion.id left join Pasegl as pasegl on atencion.id=pasegl.atencion.id inner join MotivPase as motivpase on pasegl.id=motivpase.pasegl.id where atencion.trabajador.id=?1 or atencion.datlab.trabajador.id=?1 order by atencion.tFecreg desc")
    // @Query("select new map(atencion as aten, motateselec as motiaten, pasegl as pase, motivpase as motipase) from ((Atencion as atencion inner join Motateselec as motateselec on atencion.id=motateselect.atencion.id) left join Pasegl as pasegl on atencion.id=pasegl.atencion.id) inner join MotivPase as motivpase on pasegl.id=motivpase.pasegl.id where atencion.trabajador.id=?1 or atencion.datlab.trabajador.id=?1 order by atencion.tFecreg desc")
    // @Query("select new map(atencion as aten, pasegl as pase, (select m from Motateselec as m inner join atencion as a on a.id=m.atencion.id where m.atencion.trabajador.id=?1 or m.atencion.datlab.trabajador.id=?1) as motiaten) from Atencion as atencion left join Pasegl as pasegl on atencion.id=pasegl.atencion.id where atencion.trabajador.id=?1 or atencion.datlab.trabajador.id=?1 order by atencion.tFecreg desc")
    // @Query("select new map(atencion as aten, pasegl as pase,(select m from atencion as a inner join Motateselec as m on a.id=m.atencion.id where m.n.Flgactivo=true) as motiaten) from Motateselec motateselec left join MotivPase motivpase on motateselec.atencion.id=motivpase.pasegl.atencion.id where motateselec.atencion.trabajador.id=?1 or motateselec.atencion.datlab.trabajador.id=?1 order by motateselec.atencion.tFecreg desc")
    // @Query("select new map(atencion as aten, motateselec as motiaten) from Atencion as atencion inner join Motateselec as motateselec on atencion.id=motateselec.atencion.id where atencion.trabajador.id=?1 or atencion.datlab.trabajador.id=?1 and atencion.nFlgactivo=true order by atencion.tFecreg desc")
    // @Query("select new map(atencion as aten, motateselec as motiaten) from Motateselec as motateselec inner join motateselec.atencion as atencion  on atencion.id=motateselec.atencion.id where atencion.trabajador.id=?1 or atencion.datlab.trabajador.id=?1 and atencion.nFlgactivo=true order by atencion.tFecreg desc")

    // @Query("select new map(atencion as aten, (select motivselec from Motateselec motivselec where motivselec.atencion.id=atencion.id) as motivoaten, pasegl as pase, " +
    // "(select motpasegl from MotivPase motpasegl where motpasegl.pase.id=pasegl.id) as motivopase) "+
    // "from Atencion as atencion inner join Pasegl as pasegl on atencion.id=pasegl.atencion.id "+
    // "where (atencion.trabajador.id=?1 or atencion.datlab.trabajador.id=?1)  and atencion.nFlgactivo=true order by atencion.tFecreg desc")
    @Query("select new map(atencion as aten " + 
    //", (select motateselec from Motateselec motateselec where motateselec.atencion.id=atencion.id) as motivoaten " + 
    ", (select pasegl from Pasegl pasegl where pasegl.atencion.id=atencion.id) as pase " +
    //", (select motivpase from Motivpase motivpase where motivpase.pasegl.atencion.id=atencion.id) as motivopase " +
    " ) "+
    "from Atencion atencion "+
    "where  atencion.trabajador.id=?1 or atencion.datlab.trabajador.id=?1 and atencion.nFlgactivo=true order by atencion.tFecreg desc")
    List<Atencion> findListAtenPase_ByIdTrabajador(Long id_trab);

}
