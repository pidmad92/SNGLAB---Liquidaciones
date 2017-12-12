package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Bancos;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Bancos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BancosRepository extends JpaRepository<Bancos, Long> {

    @Query("select bancos from Bancos bancos where bancos.nFlgactivo = true")
    List<Bancos> findAll_Activos();

}
