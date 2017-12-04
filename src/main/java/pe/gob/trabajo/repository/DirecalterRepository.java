package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Direcalter;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Direcalter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirecalterRepository extends JpaRepository<Direcalter, Long> {

    @Query("select direcalter from Direcalter direcalter where direcalter.nFlgactivo = true")
    List<Direcalter> findAll_Activos();

    @Query("select direcalter.vDireccion from Direcalter direcalter where direcalter.vRazsocial like ?1 and direcalter.nFlgactivo = true")
    String findDireccionAlternativaByRazonSocial(String raz_social);

}
