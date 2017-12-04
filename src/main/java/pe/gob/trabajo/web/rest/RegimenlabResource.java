package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Regimenlab;

import pe.gob.trabajo.repository.RegimenlabRepository;
import pe.gob.trabajo.repository.search.RegimenlabSearchRepository;
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
 * REST controller for managing Regimenlab.
 */
@RestController
@RequestMapping("/api")
public class RegimenlabResource {

    private final Logger log = LoggerFactory.getLogger(RegimenlabResource.class);

    private static final String ENTITY_NAME = "regimenlab";

    private final RegimenlabRepository regimenlabRepository;

    private final RegimenlabSearchRepository regimenlabSearchRepository;

    public RegimenlabResource(RegimenlabRepository regimenlabRepository, RegimenlabSearchRepository regimenlabSearchRepository) {
        this.regimenlabRepository = regimenlabRepository;
        this.regimenlabSearchRepository = regimenlabSearchRepository;
    }

    /**
     * POST  /regimenlabs : Create a new regimenlab.
     *
     * @param regimenlab the regimenlab to create
     * @return the ResponseEntity with status 201 (Created) and with body the new regimenlab, or with status 400 (Bad Request) if the regimenlab has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/regimenlabs")
    @Timed
    public ResponseEntity<Regimenlab> createRegimenlab(@Valid @RequestBody Regimenlab regimenlab) throws URISyntaxException {
        log.debug("REST request to save Regimenlab : {}", regimenlab);
        if (regimenlab.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new regimenlab cannot already have an ID")).body(null);
        }
        Regimenlab result = regimenlabRepository.save(regimenlab);
        regimenlabSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/regimenlabs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /regimenlabs : Updates an existing regimenlab.
     *
     * @param regimenlab the regimenlab to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated regimenlab,
     * or with status 400 (Bad Request) if the regimenlab is not valid,
     * or with status 500 (Internal Server Error) if the regimenlab couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/regimenlabs")
    @Timed
    public ResponseEntity<Regimenlab> updateRegimenlab(@Valid @RequestBody Regimenlab regimenlab) throws URISyntaxException {
        log.debug("REST request to update Regimenlab : {}", regimenlab);
        if (regimenlab.getId() == null) {
            return createRegimenlab(regimenlab);
        }
        Regimenlab result = regimenlabRepository.save(regimenlab);
        regimenlabSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, regimenlab.getId().toString()))
            .body(result);
    }

    /**
     * GET  /regimenlabs : get all the regimenlabs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of regimenlabs in body
     */
    @GetMapping("/regimenlabs")
    @Timed
    public List<Regimenlab> getAllRegimenlabs() {
        log.debug("REST request to get all Regimenlabs");
        return regimenlabRepository.findAll();
        }

    /**
     * GET  /regimenlabs/:id : get the "id" regimenlab.
     *
     * @param id the id of the regimenlab to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the regimenlab, or with status 404 (Not Found)
     */
    @GetMapping("/regimenlabs/{id}")
    @Timed
    public ResponseEntity<Regimenlab> getRegimenlab(@PathVariable Long id) {
        log.debug("REST request to get Regimenlab : {}", id);
        Regimenlab regimenlab = regimenlabRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(regimenlab));
    }

    /** JH
     * GET  /regimenlabs : get all the regimenlabs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of regimenlabs in body
     */
    @GetMapping("/regimenlabs/activos")
    @Timed
    public List<Regimenlab> getAll_Activos() {
        log.debug("REST request to get all regimenlabs");
        return regimenlabRepository.findAll_Activos();
    }

    /**
     * DELETE  /regimenlabs/:id : delete the "id" regimenlab.
     *
     * @param id the id of the regimenlab to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/regimenlabs/{id}")
    @Timed
    public ResponseEntity<Void> deleteRegimenlab(@PathVariable Long id) {
        log.debug("REST request to delete Regimenlab : {}", id);
        regimenlabRepository.delete(id);
        regimenlabSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/regimenlabs?query=:query : search for the regimenlab corresponding
     * to the query.
     *
     * @param query the query of the regimenlab search
     * @return the result of the search
     */
    @GetMapping("/_search/regimenlabs")
    @Timed
    public List<Regimenlab> searchRegimenlabs(@RequestParam String query) {
        log.debug("REST request to search Regimenlabs for query {}", query);
        return StreamSupport
            .stream(regimenlabSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
