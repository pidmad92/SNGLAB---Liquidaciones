package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Documento;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Documento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    @Query("select documento from Documento documento where documento.nFlgactivo = true")
    List<Documento> findAll_Activos();

    @Query("select documento from Documento documento where documento.oficina.id=?1 and documento.nFlgactivo = true")
    List<Documento> findDocumentos_ByIdOficina(Long id_ofic);

}
