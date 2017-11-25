package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Actiecon;

import pe.gob.trabajo.repository.ActieconRepository;
import pe.gob.trabajo.repository.search.ActieconSearchRepository;
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
 * REST controller for managing Actiecon.
 */
@RestController
@RequestMapping("/api")
public class ActieconResource {

    private final Logger log = LoggerFactory.getLogger(ActieconResource.class);

    private static final String ENTITY_NAME = "actiecon";

    private final ActieconRepository actieconRepository;

    private final ActieconSearchRepository actieconSearchRepository;

    public ActieconResource(ActieconRepository actieconRepository, ActieconSearchRepository actieconSearchRepository) {
        this.actieconRepository = actieconRepository;
        this.actieconSearchRepository = actieconSearchRepository;
    }

    /**
     * POST  /actiecons : Create a new actiecon.
     *
     * @param actiecon the actiecon to create
     * @return the ResponseEntity with status 201 (Created) and with body the new actiecon, or with status 400 (Bad Request) if the actiecon has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/actiecons")
    @Timed
    public ResponseEntity<Actiecon> createActiecon(@Valid @RequestBody Actiecon actiecon) throws URISyntaxException {
        log.debug("REST request to save Actiecon : {}", actiecon);
        if (actiecon.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new actiecon cannot already have an ID")).body(null);
        }
        Actiecon result = actieconRepository.save(actiecon);
        actieconSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/actiecons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /actiecons : Updates an existing actiecon.
     *
     * @param actiecon the actiecon to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated actiecon,
     * or with status 400 (Bad Request) if the actiecon is not valid,
     * or with status 500 (Internal Server Error) if the actiecon couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/actiecons")
    @Timed
    public ResponseEntity<Actiecon> updateActiecon(@Valid @RequestBody Actiecon actiecon) throws URISyntaxException {
        log.debug("REST request to update Actiecon : {}", actiecon);
        if (actiecon.getId() == null) {
            return createActiecon(actiecon);
        }
        Actiecon result = actieconRepository.save(actiecon);
        actieconSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, actiecon.getId().toString()))
            .body(result);
    }

    /**
     * GET  /actiecons : get all the actiecons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of actiecons in body
     */
    @GetMapping("/actiecons")
    @Timed
    public List<Actiecon> getAllActiecons() {
        log.debug("REST request to get all Actiecons");
        return actieconRepository.findAll();
        }

    /**
     * GET  /actiecons/:id : get the "id" actiecon.
     *
     * @param id the id of the actiecon to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the actiecon, or with status 404 (Not Found)
     */
    @GetMapping("/actiecons/{id}")
    @Timed
    public ResponseEntity<Actiecon> getActiecon(@PathVariable Long id) {
        log.debug("REST request to get Actiecon : {}", id);
        Actiecon actiecon = actieconRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(actiecon));
    }

    /**
     * DELETE  /actiecons/:id : delete the "id" actiecon.
     *
     * @param id the id of the actiecon to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/actiecons/{id}")
    @Timed
    public ResponseEntity<Void> deleteActiecon(@PathVariable Long id) {
        log.debug("REST request to delete Actiecon : {}", id);
        actieconRepository.delete(id);
        actieconSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/actiecons?query=:query : search for the actiecon corresponding
     * to the query.
     *
     * @param query the query of the actiecon search
     * @return the result of the search
     */
    @GetMapping("/_search/actiecons")
    @Timed
    public List<Actiecon> searchActiecons(@RequestParam String query) {
        log.debug("REST request to search Actiecons for query {}", query);
        return StreamSupport
            .stream(actieconSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
