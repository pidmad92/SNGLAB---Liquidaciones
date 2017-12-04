package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Pasegl;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Pasegl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaseglRepository extends JpaRepository<Pasegl, Long> {

    @Query("select pasegl from Pasegl pasegl where pasegl.nFlgactivo = true")
    List<Pasegl> findAll_Activos();

    @Query("select pasegl from Pasegl pasegl where pasegl.vEstado=?3 and (pasegl.atencion.trabajador.id=?1 or pasegl.atencion.datlab.trabajador.id=?1) and pasegl.oficina.id=?2 and pasegl.nFlgactivo=true order by pasegl.tFecreg desc")
    List<Pasegl> findPasegl_Pendientes_By_IdTrabajador_IdOficina(Long id_trab,Long  id_ofic,String vEstado);

    @Query("select pasegl from Pasegl pasegl where pasegl.id=?1 and pasegl.nFlgactivo = true")
    List<Pasegl> findPaseglById(Long id);

}
