package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Conceprem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Conceprem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConcepremRepository extends JpaRepository<Conceprem, Long> {

    // @Query("select conceprem from Conceprem conceprem where conceprem.nCodcrsup is null and conceprem.nFlgactivo = true")
    @Query("select conceprem from Conceprem conceprem where conceprem.nFlgactivo = true")
    List<Conceprem> findAll_Activos();

    // Lista de Conceprem Padres de un periodo
    // @Query("select conceprem " + 
    @Query("select new map( " +
                        " conceprem.id as Conceprem_id " +
                        " , conceprem.vNomconrem as Conceprem_vNomconrem " +
                        " , conceprem.nValconrem as Conceprem_nValconrem " +
                        " , conceprem.calrcmperi.id as ConcepremCalrcmperi_id " +
                        " , conceprem.calrcmperi.nCalrcmper as ConcepremCalrcmperi_nCalrcmper " +
                        " , conceprem.calrcmperi.calperiodo.id as ConcepremCalrcmperiCalperiodo_id " +
                        " , conceprem.calrcmperi.calperiodo.nCalper as ConcepremCalrcmperiCalperiodo_nCalper " +
                        " , conceprem.calrcmperi.calperiodo.nCalper2 as ConcepremCalrcmperiCalperiodo_nCalper2 " +
                        " , conceprem.calrcmperi.calperiodo.nNumper as ConcepremCalrcmperiCalperiodo_nNumper " +
                        " , conceprem.calrcmperi.calperiodo.tFecini as ConcepremCalrcmperiCalperiodo_tFecini " +
                        " , conceprem.calrcmperi.calperiodo.tFecfin as ConcepremCalrcmperiCalperiodo_tFecfin " +
                        " , conceprem.calrcmperi.calperiodo.nTcomput as ConcepremCalrcmperiCalperiodo_nTcomput " +
                        " , conceprem.calrcmperi.calperiodo.nTnocomput as ConcepremCalrcmperiCalperiodo_nTnocomput " +
                        " , conceprem.calrcmperi.calperiodo.nDgozados as ConcepremCalrcmperiCalperiodo_nDgozados " +
                        " , conceprem.calrcmperi.calperiodo.nDadeudos as ConcepremCalrcmperiCalperiodo_nDadeudos " +
                        " , conceprem.calrcmperi.calperiodo.nAnobase as ConcepremCalrcmperiCalperiodo_nAnobase " +
                        " , conceprem.calrcmperi.calperiodo.calbensoc.id as ConcepremCalrcmperiCalperiodoCalbensoc_id " +
                        " , conceprem.calrcmperi.calperiodo.calbensoc.nCalbens as ConcepremCalrcmperiCalperiodoCalbensoc_nCalbens " +
                        " , conceprem.calrcmperi.calperiodo.calbensoc.nCalbens2 as ConcepremCalrcmperiCalperiodoCalbensoc_nCalbens2 " +
                        " , conceprem.calrcmperi.calperiodo.calbensoc.bensocial.id as ConcepremCalrcmperiCalperiodoCalbensocBensocial_id " +
                        " , conceprem.calrcmperi.calperiodo.calbensoc.bensocial.vBensocial as ConcepremCalrcmperiCalperiodoCalbensocBensocial_vBensocial " +
                        " , conceprem.calrcmperi.calperiodo.calbensoc.liquidacion.id as ConcepremCalrcmperiCalperiodoCalbensocLiquidacion_id " +
                        " , conceprem.calrcmperi.calperiodo.calbensoc.liquidacion.nLiquid as ConcepremCalrcmperiCalperiodoCalbensocLiquidacion_nLiquid " +
                        " , conceprem.calrcmperi.calperiodo.segsalud.id as ConcepremCalrcmperiCalperiodoSegsalud_id " +
                        " , conceprem.calrcmperi.calperiodo.segsalud.vSegsal as ConcepremCalrcmperiCalperiodoSegsalud_vSegsal " +
                        " , conceprem.calrcmperi.calperiodo.estperical.id as ConcepremCalrcmperiCalperiodoEstperical_id " +
                        " , conceprem.calrcmperi.calperiodo.estperical.vNomestper as ConcepremCalrcmperiCalperiodoEstperical_vNomestper " +
                        " , conceprem.calrcmperi.calperiodo.tipcalperi.id as ConcepremCalrcmperiCalperiodoTipcalperi_id " +
                        " , conceprem.calrcmperi.calperiodo.tipcalperi.vNomtipcal as ConcepremCalrcmperiCalperiodoTipcalperi_vNomtipcal " +
                        " , conceprem.tipcalconre.id as ConcepremTipcalconre_id " +
                        " , conceprem.tipcalconre.vNomtcal as ConcepremTipcalconre_vNomtcal " +
                        " , conceprem.tipconrem.id as ConcepremTipconrem_id " +
                        " , conceprem.tipconrem.vNomtipcr as ConcepremTipconrem_vNomtipcr " +
                        " ) " +  
        " from Conceprem conceprem " + 
        " where conceprem.nCodcrsup is null and conceprem.calrcmperi.calperiodo.id=?1 and conceprem.nFlgactivo = true " + 
                " and conceprem.calrcmperi.calperiodo.nFlgactivo = true" +
                " and conceprem.calrcmperi.calperiodo.calbensoc.nFlgactivo = true " +
                " and conceprem.calrcmperi.nFlgactivo = true " + 
                " order by conceprem.tipcalconre.id")

    List<Conceprem> findListConceprem_ByIdCalperiodo(Long id_calper);

    // Lista de Conceprem Padres de todos los periodos de un Calbensoc y una Atencion
    // @Query("select conceprem " + 
    @Query("select new map( " +
                    " conceprem.id as Conceprem_id " +
                    " , conceprem.vNomconrem as Conceprem_vNomconrem " +
                    " , conceprem.nValconrem as Conceprem_nValconrem " +
                    " , conceprem.calrcmperi.id as ConcepremCalrcmperi_id " +
                    " , conceprem.calrcmperi.nCalrcmper as ConcepremCalrcmperi_nCalrcmper " +
                    " , conceprem.calrcmperi.calperiodo.id as ConcepremCalrcmperiCalperiodo_id " +
                    " , conceprem.calrcmperi.calperiodo.nCalper as ConcepremCalrcmperiCalperiodo_nCalper " +
                    " , conceprem.calrcmperi.calperiodo.nCalper2 as ConcepremCalrcmperiCalperiodo_nCalper2 " +
                    " , conceprem.calrcmperi.calperiodo.nNumper as ConcepremCalrcmperiCalperiodo_nNumper " +
                    " , conceprem.calrcmperi.calperiodo.tFecini as ConcepremCalrcmperiCalperiodo_tFecini " +
                    " , conceprem.calrcmperi.calperiodo.tFecfin as ConcepremCalrcmperiCalperiodo_tFecfin " +
                    " , conceprem.calrcmperi.calperiodo.nTcomput as ConcepremCalrcmperiCalperiodo_nTcomput " +
                    " , conceprem.calrcmperi.calperiodo.nTnocomput as ConcepremCalrcmperiCalperiodo_nTnocomput " +
                    " , conceprem.calrcmperi.calperiodo.nDgozados as ConcepremCalrcmperiCalperiodo_nDgozados " +
                    " , conceprem.calrcmperi.calperiodo.nDadeudos as ConcepremCalrcmperiCalperiodo_nDadeudos " +
                    " , conceprem.calrcmperi.calperiodo.nAnobase as ConcepremCalrcmperiCalperiodo_nAnobase " +
                    " , conceprem.calrcmperi.calperiodo.calbensoc.id as ConcepremCalrcmperiCalperiodoCalbensoc_id " +
                    " , conceprem.calrcmperi.calperiodo.calbensoc.nCalbens as ConcepremCalrcmperiCalperiodoCalbensoc_nCalbens " +
                    " , conceprem.calrcmperi.calperiodo.calbensoc.nCalbens2 as ConcepremCalrcmperiCalperiodoCalbensoc_nCalbens2 " +
                    " , conceprem.calrcmperi.calperiodo.calbensoc.bensocial.id as ConcepremCalrcmperiCalperiodoCalbensocBensocial_id " +
                    " , conceprem.calrcmperi.calperiodo.calbensoc.bensocial.vBensocial as ConcepremCalrcmperiCalperiodoCalbensocBensocial_vBensocial " +
                    " , conceprem.calrcmperi.calperiodo.calbensoc.liquidacion.id as ConcepremCalrcmperiCalperiodoCalbensocLiquidacion_id " +
                    " , conceprem.calrcmperi.calperiodo.calbensoc.liquidacion.nLiquid as ConcepremCalrcmperiCalperiodoCalbensocLiquidacion_nLiquid " +
                    " , conceprem.calrcmperi.calperiodo.segsalud.id as ConcepremCalrcmperiCalperiodoSegsalud_id " +
                    " , conceprem.calrcmperi.calperiodo.segsalud.vSegsal as ConcepremCalrcmperiCalperiodoSegsalud_vSegsal " +
                    " , conceprem.calrcmperi.calperiodo.estperical.id as ConcepremCalrcmperiCalperiodoEstperical_id " +
                    " , conceprem.calrcmperi.calperiodo.estperical.vNomestper as ConcepremCalrcmperiCalperiodoEstperical_vNomestper " +
                    " , conceprem.calrcmperi.calperiodo.tipcalperi.id as ConcepremCalrcmperiCalperiodoTipcalperi_id " +
                    " , conceprem.calrcmperi.calperiodo.tipcalperi.vNomtipcal as ConcepremCalrcmperiCalperiodoTipcalperi_vNomtipcal " +
                    " , conceprem.tipcalconre.id as ConcepremTipcalconre_id " +
                    " , conceprem.tipcalconre.vNomtcal as ConcepremTipcalconre_vNomtcal " +
                    " , conceprem.tipconrem.id as ConcepremTipconrem_id " +
                    " , conceprem.tipconrem.vNomtipcr as ConcepremTipconrem_vNomtipcr " +
                    " ) " +  
        " from Conceprem conceprem inner join Atencion atencion on atencion.liquidacion.id=conceprem.calrcmperi.calperiodo.calbensoc.liquidacion.id" + 
        " where conceprem.nCodcrsup is null and atencion.id=?1 and conceprem.calrcmperi.calperiodo.calbensoc.bensocial.id=?2 " + 
                "and atencion.nFlgactivo = true and conceprem.nFlgactivo = true " + 
                "order by conceprem.calrcmperi.calperiodo.calbensoc.bensocial.id, conceprem.tipcalconre.id, conceprem.calrcmperi.calperiodo.tFecini, conceprem.calrcmperi.calperiodo.tFecfin")
    List<Conceprem> findListConceprem_ByIdAtencionIdBensocial(Long id_aten,Long id_bensoc);

    // Lista de Conceprem hijos de un Conceprem Padre filtrado por un Tipcalconre
    // @Query("select conceprem " + 
    @Query("select new map( " +
                    " conceprem.id as Conceprem_id " +
                    " , conceprem.nCodcrsup as Conceprem_nCodcrsup " +
                    " , conceprem.vNomconrem as Conceprem_vNomconrem " +
                    " , conceprem.nValconrem as Conceprem_nValconrem " +
                    " , conceprem.tipcalconre.id as ConcepremTipcalconre_id " +
                    " , conceprem.tipcalconre.vNomtcal as ConcepremTipcalconre_vNomtcal " +
                    " ) " +  
            " from Conceprem conceprem " + 
            " where conceprem.tipcalconre.id=?2 and conceprem.nCodcrsup=?1 and conceprem.nFlgactivo = true ")
    List<Conceprem> findListConcepremHijo_ByIdPadreIdTipo(Long id_concep,Long id_tipccr);

    // Lista de Conceprem hijos de un Conceprem Padre
    // @Query("select conceprem " + 
    @Query("select new map( " +
                    " conceprem.id as Conceprem_id " +
                    " , conceprem.nCodcrsup as Conceprem_nCodcrsup " +
                    " , conceprem.vNomconrem as Conceprem_vNomconrem " +
                    " , conceprem.nValconrem as Conceprem_nValconrem " +
                    " , conceprem.tipcalconre.id as ConcepremTipcalconre_id " +
                    " , conceprem.tipcalconre.vNomtcal as ConcepremTipcalconre_vNomtcal " +
                    " ) " +  
            " from Conceprem conceprem " + 
            " where conceprem.nCodcrsup=?1 and conceprem.nFlgactivo = true " + 
            " order by conceprem.tipcalconre.id")
    List<Conceprem> findListConcepremHijo_ByIdPadre(Long id_concep);

}
