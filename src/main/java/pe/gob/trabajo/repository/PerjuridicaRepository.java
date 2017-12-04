package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Perjuridica;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Perjuridica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PerjuridicaRepository extends JpaRepository<Perjuridica, Long> {

    @Query("select perjuridica from Perjuridica perjuridica where perjuridica.nFlgactivo = true")
    List<Perjuridica> findAll_Activos();

    @Query("select perjuridica from Perjuridica perjuridica where perjuridica.tipdocident.id=?1 and  perjuridica.vNumdoc=?2 and perjuridica.nFlgactivo = true")
    Perjuridica findPersonajuridByIdentDoc(Long id_tdoc,String ndoc);

}
