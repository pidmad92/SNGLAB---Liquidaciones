package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Interesperi;

import pe.gob.trabajo.repository.InteresperiRepository;
import pe.gob.trabajo.repository.search.InteresperiSearchRepository;
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
 * REST controller for managing Interesperi.
 */
@RestController
@RequestMapping("/api")
public class InteresperiResource {

    private final Logger log = LoggerFactory.getLogger(InteresperiResource.class);

    private static final String ENTITY_NAME = "interesperi";

    private final InteresperiRepository interesperiRepository;

    private final InteresperiSearchRepository interesperiSearchRepository;

    public InteresperiResource(InteresperiRepository interesperiRepository, InteresperiSearchRepository interesperiSearchRepository) {
        this.interesperiRepository = interesperiRepository;
        this.interesperiSearchRepository = interesperiSearchRepository;
    }

    /**
     * POST  /interesperis : Create a new interesperi.
     *
     * @param interesperi the interesperi to create
     * @return the ResponseEntity with status 201 (Created) and with body the new interesperi, or with status 400 (Bad Request) if the interesperi has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/interesperis")
    @Timed
    public ResponseEntity<Interesperi> createInteresperi(@Valid @RequestBody Interesperi interesperi) throws URISyntaxException {
        log.debug("REST request to save Interesperi : {}", interesperi);
        if (interesperi.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new interesperi cannot already have an ID")).body(null);
        }
        Interesperi result = interesperiRepository.save(interesperi);
        interesperiSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/interesperis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /interesperis : Updates an existing interesperi.
     *
     * @param interesperi the interesperi to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated interesperi,
     * or with status 400 (Bad Request) if the interesperi is not valid,
     * or with status 500 (Internal Server Error) if the interesperi couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/interesperis")
    @Timed
    public ResponseEntity<Interesperi> updateInteresperi(@Valid @RequestBody Interesperi interesperi) throws URISyntaxException {
        log.debug("REST request to update Interesperi : {}", interesperi);
        if (interesperi.getId() == null) {
            return createInteresperi(interesperi);
        }
        Interesperi result = interesperiRepository.save(interesperi);
        interesperiSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, interesperi.getId().toString()))
            .body(result);
    }

    /**
     * GET  /interesperis : get all the interesperis.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of interesperis in body
     */
    @GetMapping("/interesperis")
    @Timed
    public List<Interesperi> getAllInteresperis() {
        log.debug("REST request to get all Interesperis");
        return interesperiRepository.findAll();
        }

    /**
     * GET  /interesperis/:id : get the "id" interesperi.
     *
     * @param id the id of the interesperi to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the interesperi, or with status 404 (Not Found)
     */
    @GetMapping("/interesperis/{id}")
    @Timed
    public ResponseEntity<Interesperi> getInteresperi(@PathVariable Long id) {
        log.debug("REST request to get Interesperi : {}", id);
        Interesperi interesperi = interesperiRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(interesperi));
    }

    /**
     * DELETE  /interesperis/:id : delete the "id" interesperi.
     *
     * @param id the id of the interesperi to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/interesperis/{id}")
    @Timed
    public ResponseEntity<Void> deleteInteresperi(@PathVariable Long id) {
        log.debug("REST request to delete Interesperi : {}", id);
        interesperiRepository.delete(id);
        interesperiSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/interesperis?query=:query : search for the interesperi corresponding
     * to the query.
     *
     * @param query the query of the interesperi search
     * @return the result of the search
     */
    @GetMapping("/_search/interesperis")
    @Timed
    public List<Interesperi> searchInteresperis(@RequestParam String query) {
        log.debug("REST request to search Interesperis for query {}", query);
        return StreamSupport
            .stream(interesperiSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
