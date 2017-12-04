package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Tipvinculo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Tipvinculo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipvinculoRepository extends JpaRepository<Tipvinculo, Long> {

    @Query("select tipvinculo from Tipvinculo tipvinculo where tipvinculo.nFlgactivo = true")
    List<Tipvinculo> findAll_Activos();

}
