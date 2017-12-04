package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Dirperjuri;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

// import pe.gob.trabajo.domain.Departamento;
// import pe.gob.trabajo.domain.Distrito;
// import pe.gob.trabajo.domain.Provincia;
// import pe.gob.trabajo.repository.DepartamentoRepository;
// import pe.gob.trabajo.repository.DistritoRepository;
// import pe.gob.trabajo.repository.ProvinciaRepository;


/**
 * Spring Data JPA repository for the Dirperjuri entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirperjuriRepository extends JpaRepository<Dirperjuri, Long> {

    @Query("select dirperjuri from Dirperjuri dirperjuri where dirperjuri.nFlgactivo = true")
    List<Dirperjuri> findAll_Activos();

    @Query("select dirperjuri from Dirperjuri dirperjuri right join Empleador empleador on dirperjuri.perjuridica.id=empleador.perjuridica.id  where empleador.id=?1 and dirperjuri.nFlgactivo = true")
    // @Query("select new map((select dep.vDesdep from Departamento dep where dep.vCoddep=dirperjuri.nCoddepto) as dpto, (select provin.vDespro from Provincia provin where provin.vCodpro=dirperjuri.nCodprov) as prov, (select distri.vDesdis from Distrito distri where provin.vCoddis=dirperjuri.nCoddist) as dist, dirperjuri as direc) from Dirperjuri dirperjuri right join Empleador empleador on dirperjuri.perjuridica.id=empleador.perjuridica.id  where empleador.id=?1 and dirperjuri.nFlgactivo = true")
    List<Dirperjuri> findListDireccionesEmpleadorById(Long id);

}
