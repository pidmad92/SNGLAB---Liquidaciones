package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Tipcalperi;

import pe.gob.trabajo.repository.TipcalperiRepository;
import pe.gob.trabajo.repository.search.TipcalperiSearchRepository;
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
 * REST controller for managing Tipcalperi.
 */
@RestController
@RequestMapping("/api")
public class TipcalperiResource {

    private final Logger log = LoggerFactory.getLogger(TipcalperiResource.class);

    private static final String ENTITY_NAME = "tipcalperi";

    private final TipcalperiRepository tipcalperiRepository;

    private final TipcalperiSearchRepository tipcalperiSearchRepository;

    public TipcalperiResource(TipcalperiRepository tipcalperiRepository, TipcalperiSearchRepository tipcalperiSearchRepository) {
        this.tipcalperiRepository = tipcalperiRepository;
        this.tipcalperiSearchRepository = tipcalperiSearchRepository;
    }

    /**
     * POST  /tipcalperis : Create a new tipcalperi.
     *
     * @param tipcalperi the tipcalperi to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipcalperi, or with status 400 (Bad Request) if the tipcalperi has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipcalperis")
    @Timed
    public ResponseEntity<Tipcalperi> createTipcalperi(@Valid @RequestBody Tipcalperi tipcalperi) throws URISyntaxException {
        log.debug("REST request to save Tipcalperi : {}", tipcalperi);
        if (tipcalperi.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tipcalperi cannot already have an ID")).body(null);
        }
        Tipcalperi result = tipcalperiRepository.save(tipcalperi);
        tipcalperiSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tipcalperis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipcalperis : Updates an existing tipcalperi.
     *
     * @param tipcalperi the tipcalperi to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipcalperi,
     * or with status 400 (Bad Request) if the tipcalperi is not valid,
     * or with status 500 (Internal Server Error) if the tipcalperi couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tipcalperis")
    @Timed
    public ResponseEntity<Tipcalperi> updateTipcalperi(@Valid @RequestBody Tipcalperi tipcalperi) throws URISyntaxException {
        log.debug("REST request to update Tipcalperi : {}", tipcalperi);
        if (tipcalperi.getId() == null) {
            return createTipcalperi(tipcalperi);
        }
        Tipcalperi result = tipcalperiRepository.save(tipcalperi);
        tipcalperiSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tipcalperi.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipcalperis : get all the tipcalperis.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tipcalperis in body
     */
    @GetMapping("/tipcalperis")
    @Timed
    public List<Tipcalperi> getAllTipcalperis() {
        log.debug("REST request to get all Tipcalperis");
        return tipcalperiRepository.findAll();
        }

    /**
     * GET  /tipcalperis/:id : get the "id" tipcalperi.
     *
     * @param id the id of the tipcalperi to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipcalperi, or with status 404 (Not Found)
     */
    @GetMapping("/tipcalperis/{id}")
    @Timed
    public ResponseEntity<Tipcalperi> getTipcalperi(@PathVariable Long id) {
        log.debug("REST request to get Tipcalperi : {}", id);
        Tipcalperi tipcalperi = tipcalperiRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tipcalperi));
    }

    /**
     * DELETE  /tipcalperis/:id : delete the "id" tipcalperi.
     *
     * @param id the id of the tipcalperi to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tipcalperis/{id}")
    @Timed
    public ResponseEntity<Void> deleteTipcalperi(@PathVariable Long id) {
        log.debug("REST request to delete Tipcalperi : {}", id);
        tipcalperiRepository.delete(id);
        tipcalperiSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tipcalperis?query=:query : search for the tipcalperi corresponding
     * to the query.
     *
     * @param query the query of the tipcalperi search
     * @return the result of the search
     */
    @GetMapping("/_search/tipcalperis")
    @Timed
    public List<Tipcalperi> searchTipcalperis(@RequestParam String query) {
        log.debug("REST request to search Tipcalperis for query {}", query);
        return StreamSupport
            .stream(tipcalperiSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
