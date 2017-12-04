package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Regimenlab;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Regimenlab entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegimenlabRepository extends JpaRepository<Regimenlab, Long> {

    @Query("select regimenlab from Regimenlab regimenlab where regimenlab.nFlgactivo = true")
    List<Regimenlab> findAll_Activos();

}
