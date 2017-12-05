package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Departamento;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Atencion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    @Query("select departamento from Departamento departamento where departamento.vFlgact='1'")
    List<Departamento> getAllDepartaments();

    @Query("select departamento from Departamento departamento where departamento.vCoddep=?1 and departamento.vFlgact='1' ")
    List<Departamento> getAllByCode(String cod);
}