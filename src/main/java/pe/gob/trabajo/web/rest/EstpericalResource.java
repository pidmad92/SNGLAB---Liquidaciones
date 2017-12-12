package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Estperical;

import pe.gob.trabajo.repository.EstpericalRepository;
import pe.gob.trabajo.repository.search.EstpericalSearchRepository;
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
 * REST controller for managing Estperical.
 */
@RestController
@RequestMapping("/api")
public class EstpericalResource {

    private final Logger log = LoggerFactory.getLogger(EstpericalResource.class);

    private static final String ENTITY_NAME = "estperical";

    private final EstpericalRepository estpericalRepository;

    private final EstpericalSearchRepository estpericalSearchRepository;

    public EstpericalResource(EstpericalRepository estpericalRepository, EstpericalSearchRepository estpericalSearchRepository) {
        this.estpericalRepository = estpericalRepository;
        this.estpericalSearchRepository = estpericalSearchRepository;
    }

    /**
     * POST  /estpericals : Create a new estperical.
     *
     * @param estperical the estperical to create
     * @return the ResponseEntity with status 201 (Created) and with body the new estperical, or with status 400 (Bad Request) if the estperical has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/estpericals")
    @Timed
    public ResponseEntity<Estperical> createEstperical(@Valid @RequestBody Estperical estperical) throws URISyntaxException {
        log.debug("REST request to save Estperical : {}", estperical);
        if (estperical.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new estperical cannot already have an ID")).body(null);
        }
        Estperical result = estpericalRepository.save(estperical);
        estpericalSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/estpericals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /estpericals : Updates an existing estperical.
     *
     * @param estperical the estperical to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated estperical,
     * or with status 400 (Bad Request) if the estperical is not valid,
     * or with status 500 (Internal Server Error) if the estperical couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/estpericals")
    @Timed
    public ResponseEntity<Estperical> updateEstperical(@Valid @RequestBody Estperical estperical) throws URISyntaxException {
        log.debug("REST request to update Estperical : {}", estperical);
        if (estperical.getId() == null) {
            return createEstperical(estperical);
        }
        Estperical result = estpericalRepository.save(estperical);
        estpericalSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, estperical.getId().toString()))
            .body(result);
    }

    /**
     * GET  /estpericals : get all the estpericals.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of estpericals in body
     */
    @GetMapping("/estpericals")
    @Timed
    public List<Estperical> getAllEstpericals() {
        log.debug("REST request to get all Estpericals");
        return estpericalRepository.findAll();
        }

    /**
     * GET  /estpericals/:id : get the "id" estperical.
     *
     * @param id the id of the estperical to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the estperical, or with status 404 (Not Found)
     */
    @GetMapping("/estpericals/{id}")
    @Timed
    public ResponseEntity<Estperical> getEstperical(@PathVariable Long id) {
        log.debug("REST request to get Estperical : {}", id);
        Estperical estperical = estpericalRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(estperical));
    }

    /** JH
     * GET  /estpericals/activos : get all the estpericals.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of estpericals in body
     */
    @GetMapping("/estpericals/activos")
    @Timed
    public List<Estperical> getAll_Activos() {
        log.debug("REST request to get all estpericals");
        return estpericalRepository.findAll_Activos();
        }

    /**
     * DELETE  /estpericals/:id : delete the "id" estperical.
     *
     * @param id the id of the estperical to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/estpericals/{id}")
    @Timed
    public ResponseEntity<Void> deleteEstperical(@PathVariable Long id) {
        log.debug("REST request to delete Estperical : {}", id);
        estpericalRepository.delete(id);
        estpericalSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/estpericals?query=:query : search for the estperical corresponding
     * to the query.
     *
     * @param query the query of the estperical search
     * @return the result of the search
     */
    @GetMapping("/_search/estpericals")
    @Timed
    public List<Estperical> searchEstpericals(@RequestParam String query) {
        log.debug("REST request to search Estpericals for query {}", query);
        return StreamSupport
            .stream(estpericalSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
