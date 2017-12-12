package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Accadoate;

import pe.gob.trabajo.repository.AccadoateRepository;
import pe.gob.trabajo.repository.search.AccadoateSearchRepository;
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
 * REST controller for managing Accadoate.
 */
@RestController
@RequestMapping("/api")
public class AccadoateResource {

    private final Logger log = LoggerFactory.getLogger(AccadoateResource.class);

    private static final String ENTITY_NAME = "accadoate";

    private final AccadoateRepository accadoateRepository;

    private final AccadoateSearchRepository accadoateSearchRepository;

    public AccadoateResource(AccadoateRepository accadoateRepository, AccadoateSearchRepository accadoateSearchRepository) {
        this.accadoateRepository = accadoateRepository;
        this.accadoateSearchRepository = accadoateSearchRepository;
    }

    /**
     * POST  /accadoates : Create a new accadoate.
     *
     * @param accadoate the accadoate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accadoate, or with status 400 (Bad Request) if the accadoate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/accadoates")
    @Timed
    public ResponseEntity<Accadoate> createAccadoate(@Valid @RequestBody Accadoate accadoate) throws URISyntaxException {
        log.debug("REST request to save Accadoate : {}", accadoate);
        if (accadoate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new accadoate cannot already have an ID")).body(null);
        }
        Accadoate result = accadoateRepository.save(accadoate);
        accadoateSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/accadoates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /accadoates : Updates an existing accadoate.
     *
     * @param accadoate the accadoate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accadoate,
     * or with status 400 (Bad Request) if the accadoate is not valid,
     * or with status 500 (Internal Server Error) if the accadoate couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/accadoates")
    @Timed
    public ResponseEntity<Accadoate> updateAccadoate(@Valid @RequestBody Accadoate accadoate) throws URISyntaxException {
        log.debug("REST request to update Accadoate : {}", accadoate);
        if (accadoate.getId() == null) {
            return createAccadoate(accadoate);
        }
        Accadoate result = accadoateRepository.save(accadoate);
        accadoateSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accadoate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /accadoates : get all the accadoates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accadoates in body
     */
    @GetMapping("/accadoates")
    @Timed
    public List<Accadoate> getAllAccadoates() {
        log.debug("REST request to get all Accadoates");
        return accadoateRepository.findAll();
        }

    /**
     * GET  /accadoates/:id : get the "id" accadoate.
     *
     * @param id the id of the accadoate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accadoate, or with status 404 (Not Found)
     */
    @GetMapping("/accadoates/{id}")
    @Timed
    public ResponseEntity<Accadoate> getAccadoate(@PathVariable Long id) {
        log.debug("REST request to get Accadoate : {}", id);
        Accadoate accadoate = accadoateRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accadoate));
    }

    /** JH
     * GET  /accadoates : get all the accadoates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accadoates in body
     */
    @GetMapping("/accadoates/activos")
    @Timed
    public List<Accadoate> getAll_Activos() {
        log.debug("REST request to get all Accadoates");
        return accadoateRepository.findAll_Activos();
        }

     /** JH
     * GET  /accadoate/atencion/id_aten/:id_aten
     * @param id_aten es el id de la atencion
     * @return the ResponseEntity with status 200 (OK) and with body the accadoate, or with status 404 (Not Found)
     */
	@GetMapping("/accadoates/atencion/id_aten/{id_aten}")
    @Timed
    public List<Accadoate> getListAccionesAdoptadasById_Atencion(@PathVariable Long id_aten) {
        log.debug("REST request to get Atenaccadop : id_aten {} ", id_aten);
        return accadoateRepository.findListAccionesAdoptadasById_Atencion(id_aten);
    }

     /** JH
     * GET  /accadoate/atencion/id_aten/:id_aten
     * @param id_aten es el id de la atencion
     * @return the ResponseEntity with status 200 (OK) and with body the accadoate, or with status 404 (Not Found)
     */
	@GetMapping("/accadoates/seleccion/atencion/id_aten/{id_aten}")
    @Timed
    public List<Accadoate> getListAccionadop_ySeleccionadosByIdAtencion(@PathVariable Long id_aten) {
        log.debug("REST request to get Atenaccadop : id_aten {} ", id_aten);
        return accadoateRepository.findListAccionadop_ySeleccionadosByIdAtencion(id_aten);
    }

    /**
     * DELETE  /accadoates/:id : delete the "id" accadoate.
     *
     * @param id the id of the accadoate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/accadoates/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccadoate(@PathVariable Long id) {
        log.debug("REST request to delete Accadoate : {}", id);
        accadoateRepository.delete(id);
        accadoateSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/accadoates?query=:query : search for the accadoate corresponding
     * to the query.
     *
     * @param query the query of the accadoate search
     * @return the result of the search
     */
    @GetMapping("/_search/accadoates")
    @Timed
    public List<Accadoate> searchAccadoates(@RequestParam String query) {
        log.debug("REST request to search Accadoates for query {}", query);
        return StreamSupport
            .stream(accadoateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
