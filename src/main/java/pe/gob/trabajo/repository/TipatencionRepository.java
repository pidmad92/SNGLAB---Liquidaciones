package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Tipatencion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Tipatencion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipatencionRepository extends JpaRepository<Tipatencion, Long> {

    @Query("select tipatencion from Tipatencion tipatencion where tipatencion.nFlgactivo = true")
    List<Tipatencion> findAll_Activos();

}
