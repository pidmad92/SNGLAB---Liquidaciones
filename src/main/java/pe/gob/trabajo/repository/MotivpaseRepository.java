package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Motivpase;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Motivpase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotivpaseRepository extends JpaRepository<Motivpase, Long> {

    @Query("select motivpase from Motivpase motivpase where motivpase.nFlgactivo = true")
    List<Motivpase> findAll_Activos();

    // @Query("select motivPase " + 
    @Query("select motivpase.id, motivpase.vObsmotpas, motivpase.motatenofic.motate.vDesmotate " + 
    "from Motivpase motivpase where motivpase.pasegl.id=?1 and motivpase.nFlgactivo = true")
    List<Motivpase> findListMotivPaseById_Pasegl(Long id);

    @Query("select new map(motatenofic.id as nCodmtatof, motivpase.pasegl.id as nCodpase, motatenofic.motate.vDesmotate as vDesmotate, motivpase.id as nCodmotpas, motivpase.vObsmotpas as vObsmotpas, motivpase.nFlgactivo as motivpase_nFlgactivo)" +
    " from Motatenofic motatenofic left join Motivpase motivpase on (motatenofic.id=motivpase.motatenofic.id and motivpase.pasegl.id=?2 and motivpase.nFlgactivo=true) " +
    " where motatenofic.oficina.id=?1 and motatenofic.nFlgactivo = true")
    List<Motivpase> findListMotivPaseByIdOfic_IdPase(Long id_ofi, Long id_pase);

}
