package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Motate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Motate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotateRepository extends JpaRepository<Motate, Long> {

    @Query("select motate from Motate motate where motate.nFlgactivo = true")
    List<Motate> findAll_Activos();

}
