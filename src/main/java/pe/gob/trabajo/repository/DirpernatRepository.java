package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Dirpernat;
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
 * Spring Data JPA repository for the Dirpernat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirpernatRepository extends JpaRepository<Dirpernat, Long> {

    @Query("select dirpernat from Dirpernat dirpernat where dirpernat.nFlgactivo = true")
    List<Dirpernat> findAll_Activos();

    @Query("select new map( " +
    "(select dep.vDesdep from Departamento dep where dep.vCoddep=dirpernat.nCoddepto) as dpto "+
    ", (select provin.vDespro from Provincia provin where provin.vCodpro=dirpernat.nCodprov and provin.vCoddep=dirpernat.nCoddepto) as prov " +
    ", (select distri.vDesdis from Distrito distri where distri.vCoddis=dirpernat.nCoddist and distri.vCodpro=dirpernat.nCodprov and distri.vCoddep=dirpernat.nCoddepto) as dist" +
    ", dirpernat as direc "+
    ")"+
    "from Dirpernat dirpernat inner join Trabajador trabajador on dirpernat.pernatural.id=trabajador.pernatural.id  " + 
    "where trabajador.id=?1 and dirpernat.nFlgactivo = true")
    List<Dirpernat> findListDireccionesTrabajadorById(Long id);

}
