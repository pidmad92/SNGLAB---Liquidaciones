package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Pernatural;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Pernatural entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PernaturalRepository extends JpaRepository<Pernatural, Long> {

    @Query("select pernatural from Pernatural pernatural where pernatural.nFlgactivo = true")
    List<Pernatural> findAll_Activos();

    @Query("select pernatural from Pernatural pernatural where pernatural.tipdocident.id=?1 and  pernatural.vNumdoc=?2 and pernatural.nFlgactivo = true")
    Pernatural findpersonanaturByIdentDoc(Long id_tdoc,String ndoc);

    @Query("select pernatural.vNombres || ' ' || pernatural.vApepat || ' ' || pernatural.vApemat from Pernatural pernatural where pernatural.id=?1 and pernatural.nFlgactivo = true")
    String findNomCompleto_PernaturalById(Long id);

}
