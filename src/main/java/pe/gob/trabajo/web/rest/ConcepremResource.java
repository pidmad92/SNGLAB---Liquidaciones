package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Conceprem;

import pe.gob.trabajo.repository.ConcepremRepository;
import pe.gob.trabajo.repository.search.ConcepremSearchRepository;
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
 * REST controller for managing Conceprem.
 */
@RestController
@RequestMapping("/api")
public class ConcepremResource {

    private final Logger log = LoggerFactory.getLogger(ConcepremResource.class);

    private static final String ENTITY_NAME = "conceprem";

    private final ConcepremRepository concepremRepository;

    private final ConcepremSearchRepository concepremSearchRepository;

    public ConcepremResource(ConcepremRepository concepremRepository, ConcepremSearchRepository concepremSearchRepository) {
        this.concepremRepository = concepremRepository;
        this.concepremSearchRepository = concepremSearchRepository;
    }

    /**
     * POST  /conceprems : Create a new conceprem.
     *
     * @param conceprem the conceprem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new conceprem, or with status 400 (Bad Request) if the conceprem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/conceprems")
    @Timed
    public ResponseEntity<Conceprem> createConceprem(@Valid @RequestBody Conceprem conceprem) throws URISyntaxException {
        log.debug("REST request to save Conceprem : {}", conceprem);
        if (conceprem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new conceprem cannot already have an ID")).body(null);
        }
        Conceprem result = concepremRepository.save(conceprem);
        concepremSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/conceprems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /conceprems : Updates an existing conceprem.
     *
     * @param conceprem the conceprem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated conceprem,
     * or with status 400 (Bad Request) if the conceprem is not valid,
     * or with status 500 (Internal Server Error) if the conceprem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/conceprems")
    @Timed
    public ResponseEntity<Conceprem> updateConceprem(@Valid @RequestBody Conceprem conceprem) throws URISyntaxException {
        log.debug("REST request to update Conceprem : {}", conceprem);
        if (conceprem.getId() == null) {
            return createConceprem(conceprem);
        }
        Conceprem result = concepremRepository.save(conceprem);
        concepremSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, conceprem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /conceprems : get all the conceprems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of conceprems in body
     */
    @GetMapping("/conceprems")
    @Timed
    public List<Conceprem> getAllConceprems() {
        log.debug("REST request to get all Conceprems");
        return concepremRepository.findAll();
        }

    /**
     * GET  /conceprems/:id : get the "id" conceprem.
     *
     * @param id the id of the conceprem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the conceprem, or with status 404 (Not Found)
     */
    @GetMapping("/conceprems/{id}")
    @Timed
    public ResponseEntity<Conceprem> getConceprem(@PathVariable Long id) {
        log.debug("REST request to get Conceprem : {}", id);
        Conceprem conceprem = concepremRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(conceprem));
    }

    /**
     * DELETE  /conceprems/:id : delete the "id" conceprem.
     *
     * @param id the id of the conceprem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/conceprems/{id}")
    @Timed
    public ResponseEntity<Void> deleteConceprem(@PathVariable Long id) {
        log.debug("REST request to delete Conceprem : {}", id);
        concepremRepository.delete(id);
        concepremSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/conceprems?query=:query : search for the conceprem corresponding
     * to the query.
     *
     * @param query the query of the conceprem search
     * @return the result of the search
     */
    @GetMapping("/_search/conceprems")
    @Timed
    public List<Conceprem> searchConceprems(@RequestParam String query) {
        log.debug("REST request to search Conceprems for query {}", query);
        return StreamSupport
            .stream(concepremSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
