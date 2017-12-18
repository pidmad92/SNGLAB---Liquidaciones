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
    
        // @Query("select calperiodo " + 
        @Query("select new map( " + 
                        " calperiodo.id as Calperiodo_id " +
                        " , calperiodo.nCalper as Calperiodo_nCalper " +
                        " , calperiodo.nCalper2 as Calperiodo_nCalper2 " +
                        " , calperiodo.nNumper as Calperiodo_nNumper " +
                        " , calperiodo.tFecini as Calperiodo_tFecini " +
                        " , calperiodo.tFecfin as Calperiodo_tFecfin " +
                        " , calperiodo.nTcomput as Calperiodo_nTcomput " +
                        " , calperiodo.nTnocomput as Calperiodo_nTnocomput " +
                        " , calperiodo.nDgozados as Calperiodo_nDgozados " +
                        " , calperiodo.nDadeudos as Calperiodo_nDadeudos " +
                        " , calperiodo.nAnobase as Calperiodo_nAnobase " +
                        " , calperiodo.calbensoc.id as CalperiodoCalbensoc_id " +
                        " , calperiodo.calbensoc.nCalbens as CalperiodoCalbensoc_nCalbens " +
                        " , calperiodo.calbensoc.nCalbens2 as CalperiodoCalbensoc_nCalbens2 " +
                        " , calperiodo.calbensoc.bensocial.id as CalperiodoCalbensocBensocial_id " +
                        " , calperiodo.calbensoc.bensocial.vBensocial as CalperiodoCalbensocBensocial_vBensocial " +
                        " , calperiodo.calbensoc.liquidacion.id as CalperiodoCalbensocLiquidacion_id " +
                        " , calperiodo.calbensoc.liquidacion.nLiquid as CalperiodoCalbensocLiquidacion_nLiquid " +
                        " , calperiodo.segsalud.id as CalperiodoSegsalud_id " +
                        " , calperiodo.segsalud.vSegsal as CalperiodoSegsalud_vSegsal " +
                        " , calperiodo.estperical.id as CalperiodoEstperical_id " +
                        " , calperiodo.estperical.vNomestper as CalperiodoEstperical_vNomestper " +
                        " , calperiodo.tipcalperi.id as CalperiodoTipcalperi_id " +
                        " , calperiodo.tipcalperi.vNomtipcal as CalperiodoTipcalperi_vNomtipcal " +
                        " ) " + 
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

        @Query("select new map(calrcmperi.nCalrcmper as nCalrcmper, interesperi.nValintper as nValintper " + 
                        " , calperiodo.nCalper as nCalper, calperiodo.nCalper2 as nCalper2 " +
                        " , calperiodo.tFecini as tFecini, calperiodo.tFecfin as tFecfin " +
                        " , calperiodo.nNumper as nNumper, calperiodo.nDgozados as nDgozados " +
                        " , calperiodo.nTcomput as nTcomput, calperiodo.nTnocomput as nTnocomput " +
                        " , calperiodo.nDadeudos as nDadeudos, calperiodo.nAnobase as nAnobase " +
                        " , calperiodo.tipcalperi.vNomtipcal as tipcalperi_vNomtipcal " + 
                        " , calperiodo.segsalud.vSegsal as segsalud_vSegsal " +
                        " , calperiodo.estperical.vNomestper as estperical_vNomestper " + 
                        " , calperiodo.calbensoc.id as calbensoc_id " +
                        ")" +
                " from Calperiodo calperiodo, Interesperi interesperi, Calrcmperi calrcmperi " + 
                " where calperiodo.nFlgactivo = true and " + 
                        " interesperi.nFlgactivo = true and " +
                        " calrcmperi.nFlgactivo = true and " +
                        " interesperi.calperiodo.id=?1 and " +
                        " calrcmperi.calperiodo.id=?1 and " +
                        " calperiodo.calbensoc.bensocial.id=?2 and " +
                        " calperiodo.id=?1 "
                )
        List<Calperiodo> find_RCM_Deposito_Interes_ByIdCalperiodo(Long id_calper, Long id_bsoc);

        @Query("select new map(sum(calperiodo.nCalper) as gratiperi, sum(calperiodo.nCalper2) as boniperi ) " +
                " from Calperiodo calperiodo " + 
                " where calperiodo.nFlgactivo = true and " + 
                        " (calperiodo.id,calperiodo.id) in (select calperi.nCodhijo1, calperi.nCodhijo2 from Calperiodo calperi where calperi.id=?1 and calperi.calbensoc.bensocial.id=?2 and calperi.nFlgactivo = true) ")
        List<Calperiodo> find_Grati_Bonifi_ByIdCalperiodo(Long id_calper, Long id_bsoc);
        
}
