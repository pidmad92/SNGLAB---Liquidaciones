package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Tipconrem;

import pe.gob.trabajo.repository.TipconremRepository;
import pe.gob.trabajo.repository.search.TipconremSearchRepository;
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
 * REST controller for managing Tipconrem.
 */
@RestController
@RequestMapping("/api")
public class TipconremResource {

    private final Logger log = LoggerFactory.getLogger(TipconremResource.class);

    private static final String ENTITY_NAME = "tipconrem";

    private final TipconremRepository tipconremRepository;

    private final TipconremSearchRepository tipconremSearchRepository;

    public TipconremResource(TipconremRepository tipconremRepository, TipconremSearchRepository tipconremSearchRepository) {
        this.tipconremRepository = tipconremRepository;
        this.tipconremSearchRepository = tipconremSearchRepository;
    }

    /**
     * POST  /tipconrems : Create a new tipconrem.
     *
     * @param tipconrem the tipconrem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipconrem, or with status 400 (Bad Request) if the tipconrem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipconrems")
    @Timed
    public ResponseEntity<Tipconrem> createTipconrem(@Valid @RequestBody Tipconrem tipconrem) throws URISyntaxException {
        log.debug("REST request to save Tipconrem : {}", tipconrem);
        if (tipconrem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tipconrem cannot already have an ID")).body(null);
        }
        Tipconrem result = tipconremRepository.save(tipconrem);
        tipconremSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tipconrems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipconrems : Updates an existing tipconrem.
     *
     * @param tipconrem the tipconrem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipconrem,
     * or with status 400 (Bad Request) if the tipconrem is not valid,
     * or with status 500 (Internal Server Error) if the tipconrem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tipconrems")
    @Timed
    public ResponseEntity<Tipconrem> updateTipconrem(@Valid @RequestBody Tipconrem tipconrem) throws URISyntaxException {
        log.debug("REST request to update Tipconrem : {}", tipconrem);
        if (tipconrem.getId() == null) {
            return createTipconrem(tipconrem);
        }
        Tipconrem result = tipconremRepository.save(tipconrem);
        tipconremSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tipconrem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipconrems : get all the tipconrems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tipconrems in body
     */
    @GetMapping("/tipconrems")
    @Timed
    public List<Tipconrem> getAllTipconrems() {
        log.debug("REST request to get all Tipconrems");
        return tipconremRepository.findAll();
        }

    /**
     * GET  /tipconrems/:id : get the "id" tipconrem.
     *
     * @param id the id of the tipconrem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipconrem, or with status 404 (Not Found)
     */
    @GetMapping("/tipconrems/{id}")
    @Timed
    public ResponseEntity<Tipconrem> getTipconrem(@PathVariable Long id) {
        log.debug("REST request to get Tipconrem : {}", id);
        Tipconrem tipconrem = tipconremRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tipconrem));
    }

    /**
     * DELETE  /tipconrems/:id : delete the "id" tipconrem.
     *
     * @param id the id of the tipconrem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tipconrems/{id}")
    @Timed
    public ResponseEntity<Void> deleteTipconrem(@PathVariable Long id) {
        log.debug("REST request to delete Tipconrem : {}", id);
        tipconremRepository.delete(id);
        tipconremSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tipconrems?query=:query : search for the tipconrem corresponding
     * to the query.
     *
     * @param query the query of the tipconrem search
     * @return the result of the search
     */
    @GetMapping("/_search/tipconrems")
    @Timed
    public List<Tipconrem> searchTipconrems(@RequestParam String query) {
        log.debug("REST request to search Tipconrems for query {}", query);
        return StreamSupport
            .stream(tipconremSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
