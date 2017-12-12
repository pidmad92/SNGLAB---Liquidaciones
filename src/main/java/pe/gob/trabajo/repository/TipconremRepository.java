package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Tipconrem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Tipconrem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipconremRepository extends JpaRepository<Tipconrem, Long> {

    @Query("select tipconrem from Tipconrem tipconrem where tipconrem.nFlgactivo = true")
    List<Tipconrem> findAll_Activos();

}
