package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Calperiodo;

import pe.gob.trabajo.repository.CalperiodoRepository;
import pe.gob.trabajo.repository.search.CalperiodoSearchRepository;
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
 * REST controller for managing Calperiodo.
 */
@RestController
@RequestMapping("/api")
public class CalperiodoResource {

    private final Logger log = LoggerFactory.getLogger(CalperiodoResource.class);

    private static final String ENTITY_NAME = "calperiodo";

    private final CalperiodoRepository calperiodoRepository;

    private final CalperiodoSearchRepository calperiodoSearchRepository;

    public CalperiodoResource(CalperiodoRepository calperiodoRepository, CalperiodoSearchRepository calperiodoSearchRepository) {
        this.calperiodoRepository = calperiodoRepository;
        this.calperiodoSearchRepository = calperiodoSearchRepository;
    }

    /**
     * POST  /calperiodos : Create a new calperiodo.
     *
     * @param calperiodo the calperiodo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new calperiodo, or with status 400 (Bad Request) if the calperiodo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/calperiodos")
    @Timed
    public ResponseEntity<Calperiodo> createCalperiodo(@Valid @RequestBody Calperiodo calperiodo) throws URISyntaxException {
        log.debug("REST request to save Calperiodo : {}", calperiodo);
        if (calperiodo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new calperiodo cannot already have an ID")).body(null);
        }
        Calperiodo result = calperiodoRepository.save(calperiodo);
        calperiodoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/calperiodos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /calperiodos : Updates an existing calperiodo.
     *
     * @param calperiodo the calperiodo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated calperiodo,
     * or with status 400 (Bad Request) if the calperiodo is not valid,
     * or with status 500 (Internal Server Error) if the calperiodo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/calperiodos")
    @Timed
    public ResponseEntity<Calperiodo> updateCalperiodo(@Valid @RequestBody Calperiodo calperiodo) throws URISyntaxException {
        log.debug("REST request to update Calperiodo : {}", calperiodo);
        if (calperiodo.getId() == null) {
            return createCalperiodo(calperiodo);
        }
        Calperiodo result = calperiodoRepository.save(calperiodo);
        calperiodoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, calperiodo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /calperiodos : get all the calperiodos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of calperiodos in body
     */
    @GetMapping("/calperiodos")
    @Timed
    public List<Calperiodo> getAllCalperiodos() {
        log.debug("REST request to get all Calperiodos");
        return calperiodoRepository.findAll();
        }

    /**
     * GET  /calperiodos/:id : get the "id" calperiodo.
     *
     * @param id the id of the calperiodo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the calperiodo, or with status 404 (Not Found)
     */
    @GetMapping("/calperiodos/{id}")
    @Timed
    public ResponseEntity<Calperiodo> getCalperiodo(@PathVariable Long id) {
        log.debug("REST request to get Calperiodo : {}", id);
        Calperiodo calperiodo = calperiodoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(calperiodo));
    }

    /**
     * DELETE  /calperiodos/:id : delete the "id" calperiodo.
     *
     * @param id the id of the calperiodo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/calperiodos/{id}")
    @Timed
    public ResponseEntity<Void> deleteCalperiodo(@PathVariable Long id) {
        log.debug("REST request to delete Calperiodo : {}", id);
        calperiodoRepository.delete(id);
        calperiodoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/calperiodos?query=:query : search for the calperiodo corresponding
     * to the query.
     *
     * @param query the query of the calperiodo search
     * @return the result of the search
     */
    @GetMapping("/_search/calperiodos")
    @Timed
    public List<Calperiodo> searchCalperiodos(@RequestParam String query) {
        log.debug("REST request to search Calperiodos for query {}", query);
        return StreamSupport
            .stream(calperiodoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
