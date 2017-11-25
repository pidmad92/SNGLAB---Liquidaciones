package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Tipdocident;

import pe.gob.trabajo.repository.TipdocidentRepository;
import pe.gob.trabajo.repository.search.TipdocidentSearchRepository;
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
 * REST controller for managing Tipdocident.
 */
@RestController
@RequestMapping("/api")
public class TipdocidentResource {

    private final Logger log = LoggerFactory.getLogger(TipdocidentResource.class);

    private static final String ENTITY_NAME = "tipdocident";

    private final TipdocidentRepository tipdocidentRepository;

    private final TipdocidentSearchRepository tipdocidentSearchRepository;

    public TipdocidentResource(TipdocidentRepository tipdocidentRepository, TipdocidentSearchRepository tipdocidentSearchRepository) {
        this.tipdocidentRepository = tipdocidentRepository;
        this.tipdocidentSearchRepository = tipdocidentSearchRepository;
    }

    /**
     * POST  /tipdocidents : Create a new tipdocident.
     *
     * @param tipdocident the tipdocident to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipdocident, or with status 400 (Bad Request) if the tipdocident has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipdocidents")
    @Timed
    public ResponseEntity<Tipdocident> createTipdocident(@Valid @RequestBody Tipdocident tipdocident) throws URISyntaxException {
        log.debug("REST request to save Tipdocident : {}", tipdocident);
        if (tipdocident.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tipdocident cannot already have an ID")).body(null);
        }
        Tipdocident result = tipdocidentRepository.save(tipdocident);
        tipdocidentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tipdocidents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipdocidents : Updates an existing tipdocident.
     *
     * @param tipdocident the tipdocident to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipdocident,
     * or with status 400 (Bad Request) if the tipdocident is not valid,
     * or with status 500 (Internal Server Error) if the tipdocident couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tipdocidents")
    @Timed
    public ResponseEntity<Tipdocident> updateTipdocident(@Valid @RequestBody Tipdocident tipdocident) throws URISyntaxException {
        log.debug("REST request to update Tipdocident : {}", tipdocident);
        if (tipdocident.getId() == null) {
            return createTipdocident(tipdocident);
        }
        Tipdocident result = tipdocidentRepository.save(tipdocident);
        tipdocidentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tipdocident.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipdocidents : get all the tipdocidents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tipdocidents in body
     */
    @GetMapping("/tipdocidents")
    @Timed
    public List<Tipdocident> getAllTipdocidents() {
        log.debug("REST request to get all Tipdocidents");
        return tipdocidentRepository.findAll();
        }

    /**
     * GET  /tipdocidents/:id : get the "id" tipdocident.
     *
     * @param id the id of the tipdocident to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipdocident, or with status 404 (Not Found)
     */
    @GetMapping("/tipdocidents/{id}")
    @Timed
    public ResponseEntity<Tipdocident> getTipdocident(@PathVariable Long id) {
        log.debug("REST request to get Tipdocident : {}", id);
        Tipdocident tipdocident = tipdocidentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tipdocident));
    }

    /**
     * DELETE  /tipdocidents/:id : delete the "id" tipdocident.
     *
     * @param id the id of the tipdocident to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tipdocidents/{id}")
    @Timed
    public ResponseEntity<Void> deleteTipdocident(@PathVariable Long id) {
        log.debug("REST request to delete Tipdocident : {}", id);
        tipdocidentRepository.delete(id);
        tipdocidentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tipdocidents?query=:query : search for the tipdocident corresponding
     * to the query.
     *
     * @param query the query of the tipdocident search
     * @return the result of the search
     */
    @GetMapping("/_search/tipdocidents")
    @Timed
    public List<Tipdocident> searchTipdocidents(@RequestParam String query) {
        log.debug("REST request to search Tipdocidents for query {}", query);
        return StreamSupport
            .stream(tipdocidentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
