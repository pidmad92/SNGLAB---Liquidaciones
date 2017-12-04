package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Motcese;

import pe.gob.trabajo.repository.MotceseRepository;
import pe.gob.trabajo.repository.search.MotceseSearchRepository;
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
 * REST controller for managing Motcese.
 */
@RestController
@RequestMapping("/api")
public class MotceseResource {

    private final Logger log = LoggerFactory.getLogger(MotceseResource.class);

    private static final String ENTITY_NAME = "motcese";

    private final MotceseRepository motceseRepository;

    private final MotceseSearchRepository motceseSearchRepository;

    public MotceseResource(MotceseRepository motceseRepository, MotceseSearchRepository motceseSearchRepository) {
        this.motceseRepository = motceseRepository;
        this.motceseSearchRepository = motceseSearchRepository;
    }

    /**
     * POST  /motcese : Create a new motcese.
     *
     * @param motcese the motcese to create
     * @return the ResponseEntity with status 201 (Created) and with body the new motcese, or with status 400 (Bad Request) if the motcese has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/motcese")
    @Timed
    public ResponseEntity<Motcese> createMotcese(@Valid @RequestBody Motcese motcese) throws URISyntaxException {
        log.debug("REST request to save Motcese : {}", motcese);
        if (motcese.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new motcese cannot already have an ID")).body(null);
        }
        Motcese result = motceseRepository.save(motcese);
        motceseSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/motcese/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /motcese : Updates an existing motcese.
     *
     * @param motcese the motcese to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated motcese,
     * or with status 400 (Bad Request) if the motcese is not valid,
     * or with status 500 (Internal Server Error) if the motcese couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/motcese")
    @Timed
    public ResponseEntity<Motcese> updateMotcese(@Valid @RequestBody Motcese motcese) throws URISyntaxException {
        log.debug("REST request to update Motcese : {}", motcese);
        if (motcese.getId() == null) {
            return createMotcese(motcese);
        }
        Motcese result = motceseRepository.save(motcese);
        motceseSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, motcese.getId().toString()))
            .body(result);
    }

    /**
     * GET  /motcese : get all the motcese.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of motcese in body
     */
    @GetMapping("/motcese")
    @Timed
    public List<Motcese> getAllMotcese() {
        log.debug("REST request to get all Motcese");
        return motceseRepository.findAll();
        }

    /**
     * GET  /motcese/:id : get the "id" motcese.
     *
     * @param id the id of the motcese to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the motcese, or with status 404 (Not Found)
     */
    @GetMapping("/motcese/{id}")
    @Timed
    public ResponseEntity<Motcese> getMotcese(@PathVariable Long id) {
        log.debug("REST request to get Motcese : {}", id);
        Motcese motcese = motceseRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(motcese));
    }

    /** JH
     * GET  /motcese : get all the motcese.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of motcese in body
     */
    @GetMapping("/motcese/activos")
    @Timed
    public List<Motcese> getAll_Activos() {
        log.debug("REST request to get all motcese");
        return motceseRepository.findAll_Activos();
    }

    /**
     * DELETE  /motcese/:id : delete the "id" motcese.
     *
     * @param id the id of the motcese to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/motcese/{id}")
    @Timed
    public ResponseEntity<Void> deleteMotcese(@PathVariable Long id) {
        log.debug("REST request to delete Motcese : {}", id);
        motceseRepository.delete(id);
        motceseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/motcese?query=:query : search for the motcese corresponding
     * to the query.
     *
     * @param query the query of the motcese search
     * @return the result of the search
     */
    @GetMapping("/_search/motcese")
    @Timed
    public List<Motcese> searchMotcese(@RequestParam String query) {
        log.debug("REST request to search Motcese for query {}", query);
        return StreamSupport
            .stream(motceseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
