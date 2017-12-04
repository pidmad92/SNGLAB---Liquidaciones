package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Discapate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Discapate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiscapateRepository extends JpaRepository<Discapate, Long> {

    @Query("select discapate from Discapate discapate where discapate.nFlgactivo = true")
    List<Discapate> findAll_Activos();

    @Query("select discapate from Discapate discapate where discapate.atencion.id=?1 and discapate.nFlgactivo = true")
    List<Discapate> findListDiscapateById_Atencion(Long id);

}
