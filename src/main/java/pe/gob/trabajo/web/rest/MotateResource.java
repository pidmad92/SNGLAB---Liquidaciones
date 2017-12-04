package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Motate;

import pe.gob.trabajo.repository.MotateRepository;
import pe.gob.trabajo.repository.search.MotateSearchRepository;
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
 * REST controller for managing Motate.
 */
@RestController
@RequestMapping("/api")
public class MotateResource {

    private final Logger log = LoggerFactory.getLogger(MotateResource.class);

    private static final String ENTITY_NAME = "motate";

    private final MotateRepository motateRepository;

    private final MotateSearchRepository motateSearchRepository;

    public MotateResource(MotateRepository motateRepository, MotateSearchRepository motateSearchRepository) {
        this.motateRepository = motateRepository;
        this.motateSearchRepository = motateSearchRepository;
    }

    /**
     * POST  /motates : Create a new motate.
     *
     * @param motate the motate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new motate, or with status 400 (Bad Request) if the motate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/motates")
    @Timed
    public ResponseEntity<Motate> createMotate(@Valid @RequestBody Motate motate) throws URISyntaxException {
        log.debug("REST request to save Motate : {}", motate);
        if (motate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new motate cannot already have an ID")).body(null);
        }
        Motate result = motateRepository.save(motate);
        motateSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/motates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /motates : Updates an existing motate.
     *
     * @param motate the motate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated motate,
     * or with status 400 (Bad Request) if the motate is not valid,
     * or with status 500 (Internal Server Error) if the motate couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/motates")
    @Timed
    public ResponseEntity<Motate> updateMotate(@Valid @RequestBody Motate motate) throws URISyntaxException {
        log.debug("REST request to update Motate : {}", motate);
        if (motate.getId() == null) {
            return createMotate(motate);
        }
        Motate result = motateRepository.save(motate);
        motateSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, motate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /motates : get all the motates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of motates in body
     */
    @GetMapping("/motates")
    @Timed
    public List<Motate> getAllMotates() {
        log.debug("REST request to get all Motates");
        return motateRepository.findAll();
        }

    /**
     * GET  /motates/:id : get the "id" motate.
     *
     * @param id the id of the motate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the motate, or with status 404 (Not Found)
     */
    @GetMapping("/motates/{id}")
    @Timed
    public ResponseEntity<Motate> getMotate(@PathVariable Long id) {
        log.debug("REST request to get Motate : {}", id);
        Motate motate = motateRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(motate));
    }

    /** JH
     * GET  /motates : get all the motates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of motates in body
     */
    @GetMapping("/motates/activos")
    @Timed
    public List<Motate> getAll_Activos() {
        log.debug("REST request to get all motates");
        return motateRepository.findAll_Activos();
    }

    /**
     * DELETE  /motates/:id : delete the "id" motate.
     *
     * @param id the id of the motate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/motates/{id}")
    @Timed
    public ResponseEntity<Void> deleteMotate(@PathVariable Long id) {
        log.debug("REST request to delete Motate : {}", id);
        motateRepository.delete(id);
        motateSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/motates?query=:query : search for the motate corresponding
     * to the query.
     *
     * @param query the query of the motate search
     * @return the result of the search
     */
    @GetMapping("/_search/motates")
    @Timed
    public List<Motate> searchMotates(@RequestParam String query) {
        log.debug("REST request to search Motates for query {}", query);
        return StreamSupport
            .stream(motateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
