package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Direcalter;

import pe.gob.trabajo.repository.DirecalterRepository;
import pe.gob.trabajo.repository.search.DirecalterSearchRepository;
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
 * REST controller for managing Direcalter.
 */
@RestController
@RequestMapping("/api")
public class DirecalterResource {

    private final Logger log = LoggerFactory.getLogger(DirecalterResource.class);

    private static final String ENTITY_NAME = "direcalter";

    private final DirecalterRepository direcalterRepository;

    private final DirecalterSearchRepository direcalterSearchRepository;

    public DirecalterResource(DirecalterRepository direcalterRepository, DirecalterSearchRepository direcalterSearchRepository) {
        this.direcalterRepository = direcalterRepository;
        this.direcalterSearchRepository = direcalterSearchRepository;
    }

    /**
     * POST  /direcalters : Create a new direcalter.
     *
     * @param direcalter the direcalter to create
     * @return the ResponseEntity with status 201 (Created) and with body the new direcalter, or with status 400 (Bad Request) if the direcalter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/direcalters")
    @Timed
    public ResponseEntity<Direcalter> createDirecalter(@Valid @RequestBody Direcalter direcalter) throws URISyntaxException {
        log.debug("REST request to save Direcalter : {}", direcalter);
        if (direcalter.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new direcalter cannot already have an ID")).body(null);
        }
        Direcalter result = direcalterRepository.save(direcalter);
        direcalterSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/direcalters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /direcalters : Updates an existing direcalter.
     *
     * @param direcalter the direcalter to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated direcalter,
     * or with status 400 (Bad Request) if the direcalter is not valid,
     * or with status 500 (Internal Server Error) if the direcalter couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/direcalters")
    @Timed
    public ResponseEntity<Direcalter> updateDirecalter(@Valid @RequestBody Direcalter direcalter) throws URISyntaxException {
        log.debug("REST request to update Direcalter : {}", direcalter);
        if (direcalter.getId() == null) {
            return createDirecalter(direcalter);
        }
        Direcalter result = direcalterRepository.save(direcalter);
        direcalterSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, direcalter.getId().toString()))
            .body(result);
    }

    /**
     * GET  /direcalters : get all the direcalters.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of direcalters in body
     */
    @GetMapping("/direcalters")
    @Timed
    public List<Direcalter> getAllDirecalters() {
        log.debug("REST request to get all Direcalters");
        return direcalterRepository.findAll();
        }

    /**
     * GET  /direcalters/:id : get the "id" direcalter.
     *
     * @param id the id of the direcalter to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the direcalter, or with status 404 (Not Found)
     */
    @GetMapping("/direcalters/{id}")
    @Timed
    public ResponseEntity<Direcalter> getDirecalter(@PathVariable Long id) {
        log.debug("REST request to get Direcalter : {}", id);
        Direcalter direcalter = direcalterRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(direcalter));
    }

    /** JH
     * GET  /direcalters : get all the direcalters.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of direcalters in body
     */
    @GetMapping("/direcalters/activos")
    @Timed
    public List<Direcalter> getAll_Activos() {
        log.debug("REST request to get all direcalters");
        return direcalterRepository.findAll_Activos();
    }

     /** JH
     * GET  /direcalters/raz_social/:raz_social/
     * @param raz_social es una Razón Social
     * @return the ResponseEntity with status 200 (OK) and with body the Direcalter, or with status 404 (Not Found)
     */
	@GetMapping("/direcalters/raz_social/{raz_social}")
    @Timed
    public String getDireccionAlternativaByRazonSocial(@PathVariable String raz_social) {
        log.debug("REST request to get Direcalter : id raz_social {}", raz_social);
        String direcalter = direcalterRepository.findDireccionAlternativaByRazonSocial(raz_social);
        return direcalter;
    }

    /**
     * DELETE  /direcalters/:id : delete the "id" direcalter.
     *
     * @param id the id of the direcalter to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/direcalters/{id}")
    @Timed
    public ResponseEntity<Void> deleteDirecalter(@PathVariable Long id) {
        log.debug("REST request to delete Direcalter : {}", id);
        direcalterRepository.delete(id);
        direcalterSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/direcalters?query=:query : search for the direcalter corresponding
     * to the query.
     *
     * @param query the query of the direcalter search
     * @return the result of the search
     */
    @GetMapping("/_search/direcalters")
    @Timed
    public List<Direcalter> searchDirecalters(@RequestParam String query) {
        log.debug("REST request to search Direcalters for query {}", query);
        return StreamSupport
            .stream(direcalterSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
