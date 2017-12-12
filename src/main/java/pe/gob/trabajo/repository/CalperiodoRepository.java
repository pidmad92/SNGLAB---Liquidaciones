package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Calperiodo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Calperiodo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalperiodoRepository extends JpaRepository<Calperiodo, Long> {

    @Query("select calperiodo from Calperiodo calperiodo where calperiodo.nFlgactivo = true")
    List<Calperiodo> findAll_Activos();

    @Query("select calperiodo " + 
            " from Calbensoc calbensoc inner join Atencion atencion on atencion.liquidacion.id=calbensoc.liquidacion.id " + 
                    " inner join Calperiodo calperiodo on calperiodo.calbensoc.id=calbensoc.id" + 
            " where atencion.id=?1 and calbensoc.bensocial.id=?2 " + 
                    " and calbensoc.bensocial.nFlgactivo = true " + 
                    " and atencion.nFlgactivo = true " + 
                    " and calbensoc.nFlgactivo = true " +
                    " and calperiodo.nFlgactivo = true ")
    List<Calperiodo> findListCalperiodo_By_IdAtencion_IdBiensocial(Long id_aten,Long id_bsoc);

    @Query("select new map(sum(calperiodo.nCalper) as depositos, " + 
                        " sum(interesperi.nValintper) as intereses )" + 
            " from Calbensoc calbensoc inner join Atencion atencion on atencion.liquidacion.id=calbensoc.liquidacion.id " + 
                    " inner join Calperiodo calperiodo on calperiodo.calbensoc.id=calbensoc.id" + 
                    " inner join Interesperi interesperi on interesperi.calperiodo.id=calperiodo.id " +
            " where atencion.id=?1 and calbensoc.bensocial.id=?2 " + 
                    " and calbensoc.bensocial.nFlgactivo = true " + 
                    " and atencion.nFlgactivo = true " + 
                    " and calbensoc.nFlgactivo = true " +
                    " and interesperi.nFlgactivo = true " +
                    " and calperiodo.nFlgactivo = true ")
    List<Calperiodo> findTotalDeposito_Interes_By_IdAtencion_IdBiensocial(Long id_aten,Long id_bsoc);

}
