package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Documento;

import pe.gob.trabajo.repository.DocumentoRepository;
import pe.gob.trabajo.repository.search.DocumentoSearchRepository;
import pe.gob.trabajo.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Documento.
 */
@RestController
@RequestMapping("/api")
public class DocumentoResource {

    private final Logger log = LoggerFactory.getLogger(DocumentoResource.class);

    private static final String ENTITY_NAME = "documento";

    private final DocumentoRepository documentoRepository;

    private final DocumentoSearchRepository documentoSearchRepository;

    public DocumentoResource(DocumentoRepository documentoRepository, DocumentoSearchRepository documentoSearchRepository) {
        this.documentoRepository = documentoRepository;
        this.documentoSearchRepository = documentoSearchRepository;
    }

    /**
     * POST  /documentos : Create a new documento.
     *
     * @param documento the documento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new documento, or with status 400 (Bad Request) if the documento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/documentos")
    @Timed
    public ResponseEntity<Documento> createDocumento(@Valid @RequestBody Documento documento) throws URISyntaxException {
        log.debug("REST request to save Documento : {}", documento);
        if (documento.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new documento cannot already have an ID")).body(null);
        }
        Documento result = documentoRepository.save(documento);
        documentoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/documentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /documentos : Updates an existing documento.
     *
     * @param documento the documento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated documento,
     * or with status 400 (Bad Request) if the documento is not valid,
     * or with status 500 (Internal Server Error) if the documento couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/documentos")
    @Timed
    public ResponseEntity<Documento> updateDocumento(@Valid @RequestBody Documento documento) throws URISyntaxException {
        log.debug("REST request to update Documento : {}", documento);
        if (documento.getId() == null) {
            return createDocumento(documento);
        }
        Documento result = documentoRepository.save(documento);
        documentoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, documento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /documentos : get all the documentos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of documentos in body
     */
    @GetMapping("/documentos")
    @Timed
    public List<Documento> getAllDocumentos() {
        log.debug("REST request to get all Documentos");
        return documentoRepository.findAll();
        }

    /**
     * GET  /documentos/:id : get the "id" documento.
     *
     * @param id the id of the documento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the documento, or with status 404 (Not Found)
     */
    @GetMapping("/documentos/{id}")
    @Timed
    public ResponseEntity<Documento> getDocumento(@PathVariable Long id) {
        log.debug("REST request to get Documento : {}", id);
        Documento documento = documentoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(documento));
    }

    /** JH
     * GET  /documentos : get all the documentos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of documentos in body
     */
    @GetMapping("/documentos/activos")
    @Timed
    public List<Documento> getAll_Activos() {
        log.debug("REST request to get all documentos");
        return documentoRepository.findAll_Activos();
    }

    /** JH
     * GET  /documentos/id_oficina/{id_ofic} : get all the documentos de una Oficina.
     * @param id_ofic the id de la Oficina
     * @return the ResponseEntity with status 200 (OK) and the list of documentos in body
     */
    @GetMapping("/documentos/id_oficina/{id_ofic}")
    @Timed
    public List<Documento> getDocumentos_ByIdOficina(@PathVariable Long id_ofic) {
        log.debug("REST request to get all documentos : id_ofic {}",id_ofic);
        return documentoRepository.findDocumentos_ByIdOficina(id_ofic);
    }

    /**
     * DELETE  /documentos/:id : delete the "id" documento.
     *
     * @param id the id of the documento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/documentos/{id}")
    @Timed
    public ResponseEntity<Void> deleteDocumento(@PathVariable Long id) {
        log.debug("REST request to delete Documento : {}", id);
        documentoRepository.delete(id);
        documentoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/documentos?query=:query : search for the documento corresponding
     * to the query.
     *
     * @param query the query of the documento search
     * @return the result of the search
     */
    @GetMapping("/_search/documentos")
    @Timed
    public List<Documento> searchDocumentos(@RequestParam String query) {
        log.debug("REST request to search Documentos for query {}", query);
        return StreamSupport
            .stream(documentoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
