package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Departamento;
import pe.gob.trabajo.domain.Provincia;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Atencion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {

    @Query("select provincia from Provincia provincia where provincia.vFlgact='1'")
    List<Provincia> getAllProvincias();

    @Query("select provincia from Provincia provincia where provincia.vCoddep=?1 and provincia.vCodpro=?2 and provincia.vFlgact='1'")
    List<Provincia> getAllByCodeDepProv(String coddep, String codprov);

    @Query("select provincia from Provincia provincia where provincia.vCoddep=?1 and provincia.vFlgact='1'")
    List<Provincia> getAllByCodeDep(String coddep);
}