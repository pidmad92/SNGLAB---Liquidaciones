package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Oficina;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Oficina entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OficinaRepository extends JpaRepository<Oficina, Long> {

    @Query("select oficina from Oficina oficina where oficina.nFlgactivo = true")
    List<Oficina> findAll_Activos();

}
