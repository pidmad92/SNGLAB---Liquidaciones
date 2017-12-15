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

    @Query("select docpresate from Docpresate docpresate where docpresate.atencion.id=?1 and docpresate.documento.oficina.id=?2 and docpresate.nFlgactivo = true")
    List<Docpresate> findListDocPresentaById_Atencion(Long id_aten,Long id_ofic);

    @Query("select new map(documento.id as nCoddoc, docpresate.atencion.id as nCodaten, " + 
                        "documento.vDesdoc as vDesdoc, docpresate.id as nCoddcpres, " + 
                        "docpresate.nFlgactivo as docpresate_nFlgactivo)" +
    // @Query("select new map(documento as documento, docpresate.id as nCoddcpres) " +
    " from Documento documento left join Docpresate docpresate " + 
            "on (documento.id=docpresate.documento.id and docpresate.atencion.id=?1 and docpresate.nFlgactivo=true) " +
    "where documento.oficina.id=?2")
    List<Docpresate> findListDocumento_ySeleccionadosByIdAtencion(Long id_aten,Long id_ofic);

}
