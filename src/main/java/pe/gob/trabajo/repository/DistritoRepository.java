package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Departamento;
import pe.gob.trabajo.domain.Distrito;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Atencion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DistritoRepository extends JpaRepository<Distrito, Long> {

    @Query("select distrito from Distrito distrito where distrito.vFlgact='1'")
    List<Distrito> getAllDistritos();

    @Query("select distrito from Distrito distrito where distrito.vCoddep=?1 and distrito.vCodpro=?2 and distrito.vCoddis=?3 and distrito.vFlgact='1'")
    List<Distrito> getAllByCodeDepProvDist(String coddep, String codprov, String coddis);

    @Query("select distrito from Distrito distrito where distrito.vCoddep=?1 and distrito.vCodpro=?2 and distrito.vFlgact='1'")
    List<Distrito> getAllByCodeDepProv(String coddep, String codprov);
}