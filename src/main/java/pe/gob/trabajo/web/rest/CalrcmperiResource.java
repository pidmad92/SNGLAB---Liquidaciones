package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Calrcmperi;

import pe.gob.trabajo.repository.CalrcmperiRepository;
import pe.gob.trabajo.repository.search.CalrcmperiSearchRepository;
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
 * REST controller for managing Calrcmperi.
 */
@RestController
@RequestMapping("/api")
public class CalrcmperiResource {

    private final Logger log = LoggerFactory.getLogger(CalrcmperiResource.class);

    private static final String ENTITY_NAME = "calrcmperi";

    private final CalrcmperiRepository calrcmperiRepository;

    private final CalrcmperiSearchRepository calrcmperiSearchRepository;

    public CalrcmperiResource(CalrcmperiRepository calrcmperiRepository, CalrcmperiSearchRepository calrcmperiSearchRepository) {
        this.calrcmperiRepository = calrcmperiRepository;
        this.calrcmperiSearchRepository = calrcmperiSearchRepository;
    }

    /**
     * POST  /calrcmperis : Create a new calrcmperi.
     *
     * @param calrcmperi the calrcmperi to create
     * @return the ResponseEntity with status 201 (Created) and with body the new calrcmperi, or with status 400 (Bad Request) if the calrcmperi has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/calrcmperis")
    @Timed
    public ResponseEntity<Calrcmperi> createCalrcmperi(@Valid @RequestBody Calrcmperi calrcmperi) throws URISyntaxException {
        log.debug("REST request to save Calrcmperi : {}", calrcmperi);
        if (calrcmperi.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new calrcmperi cannot already have an ID")).body(null);
        }
        Calrcmperi result = calrcmperiRepository.save(calrcmperi);
        calrcmperiSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/calrcmperis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /calrcmperis : Updates an existing calrcmperi.
     *
     * @param calrcmperi the calrcmperi to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated calrcmperi,
     * or with status 400 (Bad Request) if the calrcmperi is not valid,
     * or with status 500 (Internal Server Error) if the calrcmperi couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/calrcmperis")
    @Timed
    public ResponseEntity<Calrcmperi> updateCalrcmperi(@Valid @RequestBody Calrcmperi calrcmperi) throws URISyntaxException {
        log.debug("REST request to update Calrcmperi : {}", calrcmperi);
        if (calrcmperi.getId() == null) {
            return createCalrcmperi(calrcmperi);
        }
        Calrcmperi result = calrcmperiRepository.save(calrcmperi);
        calrcmperiSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, calrcmperi.getId().toString()))
            .body(result);
    }

    /**
     * GET  /calrcmperis : get all the calrcmperis.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of calrcmperis in body
     */
    @GetMapping("/calrcmperis")
    @Timed
    public List<Calrcmperi> getAllCalrcmperis() {
        log.debug("REST request to get all Calrcmperis");
        return calrcmperiRepository.findAll();
        }

    /**
     * GET  /calrcmperis/:id : get the "id" calrcmperi.
     *
     * @param id the id of the calrcmperi to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the calrcmperi, or with status 404 (Not Found)
     */
    @GetMapping("/calrcmperis/{id}")
    @Timed
    public ResponseEntity<Calrcmperi> getCalrcmperi(@PathVariable Long id) {
        log.debug("REST request to get Calrcmperi : {}", id);
        Calrcmperi calrcmperi = calrcmperiRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(calrcmperi));
    }

    /** JH
     * GET  /calrcmperis/activos : get all the calrcmperis.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of calrcmperis in body
     */
    @GetMapping("/calrcmperis/activos")
    @Timed
    public List<Calrcmperi> getAll_Activos() {
        log.debug("REST request to get all calrcmperis");
        return calrcmperiRepository.findAll_Activos();
        }

    /**
     * DELETE  /calrcmperis/:id : delete the "id" calrcmperi.
     *
     * @param id the id of the calrcmperi to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/calrcmperis/{id}")
    @Timed
    public ResponseEntity<Void> deleteCalrcmperi(@PathVariable Long id) {
        log.debug("REST request to delete Calrcmperi : {}", id);
        calrcmperiRepository.delete(id);
        calrcmperiSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/calrcmperis?query=:query : search for the calrcmperi corresponding
     * to the query.
     *
     * @param query the query of the calrcmperi search
     * @return the result of the search
     */
    @GetMapping("/_search/calrcmperis")
    @Timed
    public List<Calrcmperi> searchCalrcmperis(@RequestParam String query) {
        log.debug("REST request to search Calrcmperis for query {}", query);
        return StreamSupport
            .stream(calrcmperiSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
