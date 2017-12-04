package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Cartrab;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Cartrab entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartrabRepository extends JpaRepository<Cartrab, Long> {

    @Query("select cartrab from Cartrab cartrab where cartrab.nFlgactivo = true")
    List<Cartrab> findAll_Activos();

}
