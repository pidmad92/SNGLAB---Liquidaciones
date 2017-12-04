package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Modcontrato;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Modcontrato entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModcontratoRepository extends JpaRepository<Modcontrato, Long> {

    @Query("select modcontrato from Modcontrato modcontrato where modcontrato.nFlgactivo = true")
    List<Modcontrato> findAll_Activos();

}
