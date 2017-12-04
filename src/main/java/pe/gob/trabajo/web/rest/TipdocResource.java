package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Tipdoc;

import pe.gob.trabajo.repository.TipdocRepository;
import pe.gob.trabajo.repository.search.TipdocSearchRepository;
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
 * REST controller for managing Tipdoc.
 */
@RestController
@RequestMapping("/api")
public class TipdocResource {

    private final Logger log = LoggerFactory.getLogger(TipdocResource.class);

    private static final String ENTITY_NAME = "tipdoc";

    private final TipdocRepository tipdocRepository;

    private final TipdocSearchRepository tipdocSearchRepository;

    public TipdocResource(TipdocRepository tipdocRepository, TipdocSearchRepository tipdocSearchRepository) {
        this.tipdocRepository = tipdocRepository;
        this.tipdocSearchRepository = tipdocSearchRepository;
    }

    /**
     * POST  /tipdocs : Create a new tipdoc.
     *
     * @param tipdoc the tipdoc to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipdoc, or with status 400 (Bad Request) if the tipdoc has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipdocs")
    @Timed
    public ResponseEntity<Tipdoc> createTipdoc(@Valid @RequestBody Tipdoc tipdoc) throws URISyntaxException {
        log.debug("REST request to save Tipdoc : {}", tipdoc);
        if (tipdoc.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tipdoc cannot already have an ID")).body(null);
        }
        Tipdoc result = tipdocRepository.save(tipdoc);
        tipdocSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tipdocs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipdocs : Updates an existing tipdoc.
     *
     * @param tipdoc the tipdoc to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipdoc,
     * or with status 400 (Bad Request) if the tipdoc is not valid,
     * or with status 500 (Internal Server Error) if the tipdoc couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tipdocs")
    @Timed
    public ResponseEntity<Tipdoc> updateTipdoc(@Valid @RequestBody Tipdoc tipdoc) throws URISyntaxException {
        log.debug("REST request to update Tipdoc : {}", tipdoc);
        if (tipdoc.getId() == null) {
            return createTipdoc(tipdoc);
        }
        Tipdoc result = tipdocRepository.save(tipdoc);
        tipdocSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tipdoc.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipdocs : get all the tipdocs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tipdocs in body
     */
    @GetMapping("/tipdocs")
    @Timed
    public List<Tipdoc> getAllTipdocs() {
        log.debug("REST request to get all Tipdocs");
        return tipdocRepository.findAll();
        }

    /**
     * GET  /tipdocs/:id : get the "id" tipdoc.
     *
     * @param id the id of the tipdoc to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipdoc, or with status 404 (Not Found)
     */
    @GetMapping("/tipdocs/{id}")
    @Timed
    public ResponseEntity<Tipdoc> getTipdoc(@PathVariable Long id) {
        log.debug("REST request to get Tipdoc : {}", id);
        Tipdoc tipdoc = tipdocRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tipdoc));
    }

    /** JH
     * GET  /tipdocs : get all the tipdocs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tipdocs in body
     */
    @GetMapping("/tipdocs/activos")
    @Timed
    public List<Tipdoc> getAll_Activos() {
        log.debug("REST request to get all tipdocs");
        return tipdocRepository.findAll_Activos();
    }

    /**
     * DELETE  /tipdocs/:id : delete the "id" tipdoc.
     *
     * @param id the id of the tipdoc to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tipdocs/{id}")
    @Timed
    public ResponseEntity<Void> deleteTipdoc(@PathVariable Long id) {
        log.debug("REST request to delete Tipdoc : {}", id);
        tipdocRepository.delete(id);
        tipdocSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tipdocs?query=:query : search for the tipdoc corresponding
     * to the query.
     *
     * @param query the query of the tipdoc search
     * @return the result of the search
     */
    @GetMapping("/_search/tipdocs")
    @Timed
    public List<Tipdoc> searchTipdocs(@RequestParam String query) {
        log.debug("REST request to search Tipdocs for query {}", query);
        return StreamSupport
            .stream(tipdocSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
