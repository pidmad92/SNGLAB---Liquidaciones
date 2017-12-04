package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Discap;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;



/**
 * Spring Data JPA repository for the Discap entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiscapRepository extends JpaRepository<Discap, Long> {

    @Query("select discap from Discap discap where discap.nFlgactivo = true")
    List<Discap> findAll_Activos();

}
