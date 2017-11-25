package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Motateselec;

import pe.gob.trabajo.repository.MotateselecRepository;
import pe.gob.trabajo.repository.search.MotateselecSearchRepository;
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
 * REST controller for managing Motateselec.
 */
@RestController
@RequestMapping("/api")
public class MotateselecResource {

    private final Logger log = LoggerFactory.getLogger(MotateselecResource.class);

    private static final String ENTITY_NAME = "motateselec";

    private final MotateselecRepository motateselecRepository;

    private final MotateselecSearchRepository motateselecSearchRepository;

    public MotateselecResource(MotateselecRepository motateselecRepository, MotateselecSearchRepository motateselecSearchRepository) {
        this.motateselecRepository = motateselecRepository;
        this.motateselecSearchRepository = motateselecSearchRepository;
    }

    /**
     * POST  /motateselecs : Create a new motateselec.
     *
     * @param motateselec the motateselec to create
     * @return the ResponseEntity with status 201 (Created) and with body the new motateselec, or with status 400 (Bad Request) if the motateselec has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/motateselecs")
    @Timed
    public ResponseEntity<Motateselec> createMotateselec(@Valid @RequestBody Motateselec motateselec) throws URISyntaxException {
        log.debug("REST request to save Motateselec : {}", motateselec);
        if (motateselec.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new motateselec cannot already have an ID")).body(null);
        }
        Motateselec result = motateselecRepository.save(motateselec);
        motateselecSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/motateselecs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /motateselecs : Updates an existing motateselec.
     *
     * @param motateselec the motateselec to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated motateselec,
     * or with status 400 (Bad Request) if the motateselec is not valid,
     * or with status 500 (Internal Server Error) if the motateselec couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/motateselecs")
    @Timed
    public ResponseEntity<Motateselec> updateMotateselec(@Valid @RequestBody Motateselec motateselec) throws URISyntaxException {
        log.debug("REST request to update Motateselec : {}", motateselec);
        if (motateselec.getId() == null) {
            return createMotateselec(motateselec);
        }
        Motateselec result = motateselecRepository.save(motateselec);
        motateselecSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, motateselec.getId().toString()))
            .body(result);
    }

    /**
     * GET  /motateselecs : get all the motateselecs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of motateselecs in body
     */
    @GetMapping("/motateselecs")
    @Timed
    public List<Motateselec> getAllMotateselecs() {
        log.debug("REST request to get all Motateselecs");
        return motateselecRepository.findAll();
        }

    /**
     * GET  /motateselecs/:id : get the "id" motateselec.
     *
     * @param id the id of the motateselec to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the motateselec, or with status 404 (Not Found)
     */
    @GetMapping("/motateselecs/{id}")
    @Timed
    public ResponseEntity<Motateselec> getMotateselec(@PathVariable Long id) {
        log.debug("REST request to get Motateselec : {}", id);
        Motateselec motateselec = motateselecRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(motateselec));
    }

    /**
     * DELETE  /motateselecs/:id : delete the "id" motateselec.
     *
     * @param id the id of the motateselec to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/motateselecs/{id}")
    @Timed
    public ResponseEntity<Void> deleteMotateselec(@PathVariable Long id) {
        log.debug("REST request to delete Motateselec : {}", id);
        motateselecRepository.delete(id);
        motateselecSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/motateselecs?query=:query : search for the motateselec corresponding
     * to the query.
     *
     * @param query the query of the motateselec search
     * @return the result of the search
     */
    @GetMapping("/_search/motateselecs")
    @Timed
    public List<Motateselec> searchMotateselecs(@RequestParam String query) {
        log.debug("REST request to search Motateselecs for query {}", query);
        return StreamSupport
            .stream(motateselecSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
