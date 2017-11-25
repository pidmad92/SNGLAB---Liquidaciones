package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Motatenofic;

import pe.gob.trabajo.repository.MotatenoficRepository;
import pe.gob.trabajo.repository.search.MotatenoficSearchRepository;
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
 * REST controller for managing Motatenofic.
 */
@RestController
@RequestMapping("/api")
public class MotatenoficResource {

    private final Logger log = LoggerFactory.getLogger(MotatenoficResource.class);

    private static final String ENTITY_NAME = "motatenofic";

    private final MotatenoficRepository motatenoficRepository;

    private final MotatenoficSearchRepository motatenoficSearchRepository;

    public MotatenoficResource(MotatenoficRepository motatenoficRepository, MotatenoficSearchRepository motatenoficSearchRepository) {
        this.motatenoficRepository = motatenoficRepository;
        this.motatenoficSearchRepository = motatenoficSearchRepository;
    }

    /**
     * POST  /motatenofics : Create a new motatenofic.
     *
     * @param motatenofic the motatenofic to create
     * @return the ResponseEntity with status 201 (Created) and with body the new motatenofic, or with status 400 (Bad Request) if the motatenofic has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/motatenofics")
    @Timed
    public ResponseEntity<Motatenofic> createMotatenofic(@Valid @RequestBody Motatenofic motatenofic) throws URISyntaxException {
        log.debug("REST request to save Motatenofic : {}", motatenofic);
        if (motatenofic.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new motatenofic cannot already have an ID")).body(null);
        }
        Motatenofic result = motatenoficRepository.save(motatenofic);
        motatenoficSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/motatenofics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /motatenofics : Updates an existing motatenofic.
     *
     * @param motatenofic the motatenofic to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated motatenofic,
     * or with status 400 (Bad Request) if the motatenofic is not valid,
     * or with status 500 (Internal Server Error) if the motatenofic couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/motatenofics")
    @Timed
    public ResponseEntity<Motatenofic> updateMotatenofic(@Valid @RequestBody Motatenofic motatenofic) throws URISyntaxException {
        log.debug("REST request to update Motatenofic : {}", motatenofic);
        if (motatenofic.getId() == null) {
            return createMotatenofic(motatenofic);
        }
        Motatenofic result = motatenoficRepository.save(motatenofic);
        motatenoficSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, motatenofic.getId().toString()))
            .body(result);
    }

    /**
     * GET  /motatenofics : get all the motatenofics.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of motatenofics in body
     */
    @GetMapping("/motatenofics")
    @Timed
    public List<Motatenofic> getAllMotatenofics() {
        log.debug("REST request to get all Motatenofics");
        return motatenoficRepository.findAll();
        }

    /**
     * GET  /motatenofics/:id : get the "id" motatenofic.
     *
     * @param id the id of the motatenofic to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the motatenofic, or with status 404 (Not Found)
     */
    @GetMapping("/motatenofics/{id}")
    @Timed
    public ResponseEntity<Motatenofic> getMotatenofic(@PathVariable Long id) {
        log.debug("REST request to get Motatenofic : {}", id);
        Motatenofic motatenofic = motatenoficRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(motatenofic));
    }

    /**
     * DELETE  /motatenofics/:id : delete the "id" motatenofic.
     *
     * @param id the id of the motatenofic to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/motatenofics/{id}")
    @Timed
    public ResponseEntity<Void> deleteMotatenofic(@PathVariable Long id) {
        log.debug("REST request to delete Motatenofic : {}", id);
        motatenoficRepository.delete(id);
        motatenoficSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/motatenofics?query=:query : search for the motatenofic corresponding
     * to the query.
     *
     * @param query the query of the motatenofic search
     * @return the result of the search
     */
    @GetMapping("/_search/motatenofics")
    @Timed
    public List<Motatenofic> searchMotatenofics(@RequestParam String query) {
        log.debug("REST request to search Motatenofics for query {}", query);
        return StreamSupport
            .stream(motatenoficSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
