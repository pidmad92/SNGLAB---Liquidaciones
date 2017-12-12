package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Tipinteres;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Tipinteres entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipinteresRepository extends JpaRepository<Tipinteres, Long> {

    @Query("select tipinteres from Tipinteres tipinteres where tipinteres.nFlgactivo = true")
    List<Tipinteres> findAll_Activos();

}
