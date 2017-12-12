package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Segsalud;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Segsalud entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SegsaludRepository extends JpaRepository<Segsalud, Long> {

    @Query("select segsalud from Segsalud segsalud where segsalud.nFlgactivo = true")
    List<Segsalud> findAll_Activos();

}
