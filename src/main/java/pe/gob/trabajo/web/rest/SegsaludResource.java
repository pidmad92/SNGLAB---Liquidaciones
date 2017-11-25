package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Segsalud;

import pe.gob.trabajo.repository.SegsaludRepository;
import pe.gob.trabajo.repository.search.SegsaludSearchRepository;
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
 * REST controller for managing Segsalud.
 */
@RestController
@RequestMapping("/api")
public class SegsaludResource {

    private final Logger log = LoggerFactory.getLogger(SegsaludResource.class);

    private static final String ENTITY_NAME = "segsalud";

    private final SegsaludRepository segsaludRepository;

    private final SegsaludSearchRepository segsaludSearchRepository;

    public SegsaludResource(SegsaludRepository segsaludRepository, SegsaludSearchRepository segsaludSearchRepository) {
        this.segsaludRepository = segsaludRepository;
        this.segsaludSearchRepository = segsaludSearchRepository;
    }

    /**
     * POST  /segsaluds : Create a new segsalud.
     *
     * @param segsalud the segsalud to create
     * @return the ResponseEntity with status 201 (Created) and with body the new segsalud, or with status 400 (Bad Request) if the segsalud has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/segsaluds")
    @Timed
    public ResponseEntity<Segsalud> createSegsalud(@Valid @RequestBody Segsalud segsalud) throws URISyntaxException {
        log.debug("REST request to save Segsalud : {}", segsalud);
        if (segsalud.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new segsalud cannot already have an ID")).body(null);
        }
        Segsalud result = segsaludRepository.save(segsalud);
        segsaludSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/segsaluds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /segsaluds : Updates an existing segsalud.
     *
     * @param segsalud the segsalud to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated segsalud,
     * or with status 400 (Bad Request) if the segsalud is not valid,
     * or with status 500 (Internal Server Error) if the segsalud couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/segsaluds")
    @Timed
    public ResponseEntity<Segsalud> updateSegsalud(@Valid @RequestBody Segsalud segsalud) throws URISyntaxException {
        log.debug("REST request to update Segsalud : {}", segsalud);
        if (segsalud.getId() == null) {
            return createSegsalud(segsalud);
        }
        Segsalud result = segsaludRepository.save(segsalud);
        segsaludSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, segsalud.getId().toString()))
            .body(result);
    }

    /**
     * GET  /segsaluds : get all the segsaluds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of segsaluds in body
     */
    @GetMapping("/segsaluds")
    @Timed
    public List<Segsalud> getAllSegsaluds() {
        log.debug("REST request to get all Segsaluds");
        return segsaludRepository.findAll();
        }

    /**
     * GET  /segsaluds/:id : get the "id" segsalud.
     *
     * @param id the id of the segsalud to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the segsalud, or with status 404 (Not Found)
     */
    @GetMapping("/segsaluds/{id}")
    @Timed
    public ResponseEntity<Segsalud> getSegsalud(@PathVariable Long id) {
        log.debug("REST request to get Segsalud : {}", id);
        Segsalud segsalud = segsaludRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(segsalud));
    }

    /**
     * DELETE  /segsaluds/:id : delete the "id" segsalud.
     *
     * @param id the id of the segsalud to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/segsaluds/{id}")
    @Timed
    public ResponseEntity<Void> deleteSegsalud(@PathVariable Long id) {
        log.debug("REST request to delete Segsalud : {}", id);
        segsaludRepository.delete(id);
        segsaludSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/segsaluds?query=:query : search for the segsalud corresponding
     * to the query.
     *
     * @param query the query of the segsalud search
     * @return the result of the search
     */
    @GetMapping("/_search/segsaluds")
    @Timed
    public List<Segsalud> searchSegsaluds(@RequestParam String query) {
        log.debug("REST request to search Segsaluds for query {}", query);
        return StreamSupport
            .stream(segsaludSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
