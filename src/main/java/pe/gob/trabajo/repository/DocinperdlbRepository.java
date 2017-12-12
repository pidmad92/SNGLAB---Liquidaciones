package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Docinperdlb;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Docinperdlb entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocinperdlbRepository extends JpaRepository<Docinperdlb, Long> {

    @Query("select docinperdlb from Docinperdlb docinperdlb where docinperdlb.nFlgactivo = true")
    List<Docinperdlb> findAll_Activos();

    @Query("select docinperdlb from Docinperdlb docinperdlb left join Atencion atencion on atencion.datlab.id=docinperdlb.datlab.id where atencion.id=?1 and docinperdlb.nFlgactivo = true")
    List<Docinperdlb> findListDocinperdlbById_Atencion(Long id);

    @Query("select new map(docingrper.id as nCoddinper, docinperdlb.datlab.id as nCodaten, " + 
                        "docingrper.vDesdocing as vDesdocing, docinperdlb.id as nCoddipedl, " + 
                        "docinperdlb.nFlgactivo as docinperdlb_nFlgactivo)" +
    // @Query("select new map(Docingrper as docingrper, docinperdlb.id as nCoddipedl) " +
    " from Docingrper docingrper left join Docinperdlb docinperdlb " + 
            "on (docingrper.id=docinperdlb.docingrper.id and docinperdlb.datlab.id=?1 and docinperdlb.nFlgactivo=true) ")
    List<Docinperdlb> findListDocingrper_ySeleccionadosByIdDatlab(Long id_datlab);

}
