package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Docpresate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Docpresate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocpresateRepository extends JpaRepository<Docpresate, Long> {

    @Query("select docpresate from Docpresate docpresate where docpresate.nFlgactivo = true")
    List<Docpresate> findAll_Activos();

    @Query("select docpresate from Docpresate docpresate where docpresate.atencion.id=?1 and docpresate.nFlgactivo = true")
    List<Docpresate> findListDocPresentaById_Atencion(Long id_aten);

}
