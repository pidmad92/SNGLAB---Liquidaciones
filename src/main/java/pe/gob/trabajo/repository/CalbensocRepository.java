package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Calbensoc;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Calbensoc entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalbensocRepository extends JpaRepository<Calbensoc, Long> {

    @Query("select calbensoc from Calbensoc calbensoc where calbensoc.nFlgactivo = true")
    List<Calbensoc> findAll_Activos();

    @Query("select calbensoc " + 
    " from Calbensoc calbensoc inner join Atencion atencion on atencion.liquidacion.id=calbensoc.liquidacion.id " + 
    " where atencion.id=?1 and calbensoc.bensocial.id=?2 and calbensoc.bensocial.nFlgactivo = true and atencion.nFlgactivo = true and calbensoc.nFlgactivo = true")
    List<Calbensoc> findCalbensoc_ByIdAtencionIdBensocial(Long id_aten, Long id_bsoc);

    @Query("select calbensoc " + 
    " from Calbensoc calbensoc inner join Atencion atencion on atencion.liquidacion.id=calbensoc.liquidacion.id " + 
    " where atencion.id=?1 and atencion.nFlgactivo = true and calbensoc.nFlgactivo = true ")
    List<Calbensoc> findAllCalbensoc_ByIdAtencion(Long id_aten);

}
