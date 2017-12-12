package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Motateselec;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Motateselec entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotateselecRepository extends JpaRepository<Motateselec, Long> {

    @Query("select motateselec from Motateselec motateselec where motateselec.nFlgactivo = true")
    List<Motateselec> findAll_Activos();

    @Query("select motateselec from Motateselec motateselec where motateselec.atencion.id=?1 and motateselec.nFlgactivo = true")
    List<Motateselec> findListMotateselecById_Atencion(Long id);

    @Query("select new map(motatenofic.id as nCodmtatof, " + 
                        "motateselec.atencion.id as nCodaten, " + 
                        "motatenofic.motate.vDesmotate as vDesmotate, " + 
                        "motateselec.id as nCodmotasel, " + 
                        "motateselec.vObsmoseat as vObsmoseat, " + 
                        "motateselec.nFlgactivo as motateselec_nFlgactivo)" +
    // @Query("select new map(motatenofic as Motateno, motateselec.id as nCodmotasel, motateselec.vObsmotpas as observacion) " +
    " from Motatenofic motatenofic left join Motateselec motateselec on (motatenofic.id=motateselec.motatenofic.id and motateselec.atencion.id=?1 and motateselec.nFlgactivo=true) " +
    " where motatenofic.oficina.id=?2 and motatenofic.nFlgactivo = true")
    List<Motateselec> findListMotateselecByIdOfic_IdAtencion(Long id_aten, Long id_ofi);

}
