package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Bensocial;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Bensocial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BensocialRepository extends JpaRepository<Bensocial, Long> {

    @Query("select bensocial from Bensocial bensocial where bensocial.nFlgactivo = true")
    List<Bensocial> findAll_Activos();

}
