package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Calbensoc;

import pe.gob.trabajo.repository.CalbensocRepository;
import pe.gob.trabajo.repository.search.CalbensocSearchRepository;
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
 * REST controller for managing Calbensoc.
 */
@RestController
@RequestMapping("/api")
public class CalbensocResource {

    private final Logger log = LoggerFactory.getLogger(CalbensocResource.class);

    private static final String ENTITY_NAME = "calbensoc";

    private final CalbensocRepository calbensocRepository;

    private final CalbensocSearchRepository calbensocSearchRepository;

    public CalbensocResource(CalbensocRepository calbensocRepository, CalbensocSearchRepository calbensocSearchRepository) {
        this.calbensocRepository = calbensocRepository;
        this.calbensocSearchRepository = calbensocSearchRepository;
    }

    /**
     * POST  /calbensocs : Create a new calbensoc.
     *
     * @param calbensoc the calbensoc to create
     * @return the ResponseEntity with status 201 (Created) and with body the new calbensoc, or with status 400 (Bad Request) if the calbensoc has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/calbensocs")
    @Timed
    public ResponseEntity<Calbensoc> createCalbensoc(@Valid @RequestBody Calbensoc calbensoc) throws URISyntaxException {
        log.debug("REST request to save Calbensoc : {}", calbensoc);
        if (calbensoc.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new calbensoc cannot already have an ID")).body(null);
        }
        Calbensoc result = calbensocRepository.save(calbensoc);
        calbensocSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/calbensocs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /calbensocs : Updates an existing calbensoc.
     *
     * @param calbensoc the calbensoc to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated calbensoc,
     * or with status 400 (Bad Request) if the calbensoc is not valid,
     * or with status 500 (Internal Server Error) if the calbensoc couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/calbensocs")
    @Timed
    public ResponseEntity<Calbensoc> updateCalbensoc(@Valid @RequestBody Calbensoc calbensoc) throws URISyntaxException {
        log.debug("REST request to update Calbensoc : {}", calbensoc);
        if (calbensoc.getId() == null) {
            return createCalbensoc(calbensoc);
        }
        Calbensoc result = calbensocRepository.save(calbensoc);
        calbensocSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, calbensoc.getId().toString()))
            .body(result);
    }

    /**
     * GET  /calbensocs : get all the calbensocs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of calbensocs in body
     */
    @GetMapping("/calbensocs")
    @Timed
    public List<Calbensoc> getAllCalbensocs() {
        log.debug("REST request to get all Calbensocs");
        return calbensocRepository.findAll();
        }

    /**
     * GET  /calbensocs/:id : get the "id" calbensoc.
     *
     * @param id the id of the calbensoc to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the calbensoc, or with status 404 (Not Found)
     */
    @GetMapping("/calbensocs/{id}")
    @Timed
    public ResponseEntity<Calbensoc> getCalbensoc(@PathVariable Long id) {
        log.debug("REST request to get Calbensoc : {}", id);
        Calbensoc calbensoc = calbensocRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(calbensoc));
    }

    /**
     * DELETE  /calbensocs/:id : delete the "id" calbensoc.
     *
     * @param id the id of the calbensoc to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/calbensocs/{id}")
    @Timed
    public ResponseEntity<Void> deleteCalbensoc(@PathVariable Long id) {
        log.debug("REST request to delete Calbensoc : {}", id);
        calbensocRepository.delete(id);
        calbensocSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/calbensocs?query=:query : search for the calbensoc corresponding
     * to the query.
     *
     * @param query the query of the calbensoc search
     * @return the result of the search
     */
    @GetMapping("/_search/calbensocs")
    @Timed
    public List<Calbensoc> searchCalbensocs(@RequestParam String query) {
        log.debug("REST request to search Calbensocs for query {}", query);
        return StreamSupport
            .stream(calbensocSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
