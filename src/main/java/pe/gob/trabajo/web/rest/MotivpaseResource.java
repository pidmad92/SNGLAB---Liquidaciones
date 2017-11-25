package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Motivpase;

import pe.gob.trabajo.repository.MotivpaseRepository;
import pe.gob.trabajo.repository.search.MotivpaseSearchRepository;
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
 * REST controller for managing Motivpase.
 */
@RestController
@RequestMapping("/api")
public class MotivpaseResource {

    private final Logger log = LoggerFactory.getLogger(MotivpaseResource.class);

    private static final String ENTITY_NAME = "motivpase";

    private final MotivpaseRepository motivpaseRepository;

    private final MotivpaseSearchRepository motivpaseSearchRepository;

    public MotivpaseResource(MotivpaseRepository motivpaseRepository, MotivpaseSearchRepository motivpaseSearchRepository) {
        this.motivpaseRepository = motivpaseRepository;
        this.motivpaseSearchRepository = motivpaseSearchRepository;
    }

    /**
     * POST  /motivpases : Create a new motivpase.
     *
     * @param motivpase the motivpase to create
     * @return the ResponseEntity with status 201 (Created) and with body the new motivpase, or with status 400 (Bad Request) if the motivpase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/motivpases")
    @Timed
    public ResponseEntity<Motivpase> createMotivpase(@Valid @RequestBody Motivpase motivpase) throws URISyntaxException {
        log.debug("REST request to save Motivpase : {}", motivpase);
        if (motivpase.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new motivpase cannot already have an ID")).body(null);
        }
        Motivpase result = motivpaseRepository.save(motivpase);
        motivpaseSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/motivpases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /motivpases : Updates an existing motivpase.
     *
     * @param motivpase the motivpase to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated motivpase,
     * or with status 400 (Bad Request) if the motivpase is not valid,
     * or with status 500 (Internal Server Error) if the motivpase couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/motivpases")
    @Timed
    public ResponseEntity<Motivpase> updateMotivpase(@Valid @RequestBody Motivpase motivpase) throws URISyntaxException {
        log.debug("REST request to update Motivpase : {}", motivpase);
        if (motivpase.getId() == null) {
            return createMotivpase(motivpase);
        }
        Motivpase result = motivpaseRepository.save(motivpase);
        motivpaseSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, motivpase.getId().toString()))
            .body(result);
    }

    /**
     * GET  /motivpases : get all the motivpases.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of motivpases in body
     */
    @GetMapping("/motivpases")
    @Timed
    public List<Motivpase> getAllMotivpases() {
        log.debug("REST request to get all Motivpases");
        return motivpaseRepository.findAll();
        }

    /**
     * GET  /motivpases/:id : get the "id" motivpase.
     *
     * @param id the id of the motivpase to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the motivpase, or with status 404 (Not Found)
     */
    @GetMapping("/motivpases/{id}")
    @Timed
    public ResponseEntity<Motivpase> getMotivpase(@PathVariable Long id) {
        log.debug("REST request to get Motivpase : {}", id);
        Motivpase motivpase = motivpaseRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(motivpase));
    }

    /**
     * DELETE  /motivpases/:id : delete the "id" motivpase.
     *
     * @param id the id of the motivpase to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/motivpases/{id}")
    @Timed
    public ResponseEntity<Void> deleteMotivpase(@PathVariable Long id) {
        log.debug("REST request to delete Motivpase : {}", id);
        motivpaseRepository.delete(id);
        motivpaseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/motivpases?query=:query : search for the motivpase corresponding
     * to the query.
     *
     * @param query the query of the motivpase search
     * @return the result of the search
     */
    @GetMapping("/_search/motivpases")
    @Timed
    public List<Motivpase> searchMotivpases(@RequestParam String query) {
        log.debug("REST request to search Motivpases for query {}", query);
        return StreamSupport
            .stream(motivpaseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
