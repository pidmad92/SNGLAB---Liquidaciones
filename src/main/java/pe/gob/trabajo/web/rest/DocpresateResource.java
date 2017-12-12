package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Docpresate;

import pe.gob.trabajo.repository.DocpresateRepository;
import pe.gob.trabajo.repository.search.DocpresateSearchRepository;
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
 * REST controller for managing Docpresate.
 */
@RestController
@RequestMapping("/api")
public class DocpresateResource {

    private final Logger log = LoggerFactory.getLogger(DocpresateResource.class);

    private static final String ENTITY_NAME = "docpresate";

    private final DocpresateRepository docpresateRepository;

    private final DocpresateSearchRepository docpresateSearchRepository;

    public DocpresateResource(DocpresateRepository docpresateRepository, DocpresateSearchRepository docpresateSearchRepository) {
        this.docpresateRepository = docpresateRepository;
        this.docpresateSearchRepository = docpresateSearchRepository;
    }

    /**
     * POST  /docpresates : Create a new docpresate.
     *
     * @param docpresate the docpresate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new docpresate, or with status 400 (Bad Request) if the docpresate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/docpresates")
    @Timed
    public ResponseEntity<Docpresate> createDocpresate(@Valid @RequestBody Docpresate docpresate) throws URISyntaxException {
        log.debug("REST request to save Docpresate : {}", docpresate);
        if (docpresate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new docpresate cannot already have an ID")).body(null);
        }
        Docpresate result = docpresateRepository.save(docpresate);
        docpresateSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/docpresates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /docpresates : Updates an existing docpresate.
     *
     * @param docpresate the docpresate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated docpresate,
     * or with status 400 (Bad Request) if the docpresate is not valid,
     * or with status 500 (Internal Server Error) if the docpresate couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/docpresates")
    @Timed
    public ResponseEntity<Docpresate> updateDocpresate(@Valid @RequestBody Docpresate docpresate) throws URISyntaxException {
        log.debug("REST request to update Docpresate : {}", docpresate);
        if (docpresate.getId() == null) {
            return createDocpresate(docpresate);
        }
        Docpresate result = docpresateRepository.save(docpresate);
        docpresateSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, docpresate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /docpresates : get all the docpresates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of docpresates in body
     */
    @GetMapping("/docpresates")
    @Timed
    public List<Docpresate> getAllDocpresates() {
        log.debug("REST request to get all Docpresates");
        return docpresateRepository.findAll();
        }

    /**
     * GET  /docpresates/:id : get the "id" docpresate.
     *
     * @param id the id of the docpresate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the docpresate, or with status 404 (Not Found)
     */
    @GetMapping("/docpresates/{id}")
    @Timed
    public ResponseEntity<Docpresate> getDocpresate(@PathVariable Long id) {
        log.debug("REST request to get Docpresate : {}", id);
        Docpresate docpresate = docpresateRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(docpresate));
    }

    /** JH
     * GET  /docpresates : get all the docpresates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of docpresates in body
     */
    @GetMapping("/docpresates/activos")
    @Timed
    public List<Docpresate> getAll_Activos() {
        log.debug("REST request to get all docpresates");
        return docpresateRepository.findAll_Activos();
    }

     /** JH
     * GET  /docpresates/atencion/id/:id_aten :
     * @param id_aten es el id de la atencion
     * @return the ResponseEntity with status 200 (OK) and with body the Docpresate, or with status 404 (Not Found)
     */
	@GetMapping("/docpresates/atencion/id/{id_aten}")
    @Timed
    public List<Docpresate> getListDocpresentaByIdAtencion(@PathVariable Long id_aten) {
        log.debug("REST request to get Docpresate : id_aten {}", id_aten);
        return docpresateRepository.findListDocPresentaById_Atencion(id_aten);
    }

     /** JH
     * GET  /docpresates/atencion/id_aten/:id_aten
     * @param id_aten es el id de la atencion
     * @return the ResponseEntity with status 200 (OK) and with body the docpresates, or with status 404 (Not Found)
     */
	@GetMapping("/docpresates/seleccion/atencion/id_aten/{id_aten}")
    @Timed
    public List<Docpresate> getListDocumento_ySeleccionadosByIdAtencion(@PathVariable Long id_aten) {
        log.debug("REST request to get Atenaccadop : id_aten {} ", id_aten);
        return docpresateRepository.findListDocumento_ySeleccionadosByIdAtencion(id_aten);
    }

    /**
     * DELETE  /docpresates/:id : delete the "id" docpresate.
     *
     * @param id the id of the docpresate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/docpresates/{id}")
    @Timed
    public ResponseEntity<Void> deleteDocpresate(@PathVariable Long id) {
        log.debug("REST request to delete Docpresate : {}", id);
        docpresateRepository.delete(id);
        docpresateSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/docpresates?query=:query : search for the docpresate corresponding
     * to the query.
     *
     * @param query the query of the docpresate search
     * @return the result of the search
     */
    @GetMapping("/_search/docpresates")
    @Timed
    public List<Docpresate> searchDocpresates(@RequestParam String query) {
        log.debug("REST request to search Docpresates for query {}", query);
        return StreamSupport
            .stream(docpresateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
