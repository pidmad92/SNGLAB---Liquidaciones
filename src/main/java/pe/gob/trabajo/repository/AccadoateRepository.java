package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Accadoate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Accadoate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccadoateRepository extends JpaRepository<Accadoate, Long> {

    @Query("select accadoate from Accadoate accadoate where accadoate.atencion.id=?1 and accadoate.nFlgactivo = true")
    List<Accadoate> findListAccionesAdoptadasById_Atencion(Long id_aten);

    @Query("select accadoate from Accadoate accadoate where accadoate.nFlgactivo = true")
    List<Accadoate> findAll_Activos();

}
