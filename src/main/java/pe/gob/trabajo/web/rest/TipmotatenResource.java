package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Tipmotaten;

import pe.gob.trabajo.repository.TipmotatenRepository;
import pe.gob.trabajo.repository.search.TipmotatenSearchRepository;
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
 * REST controller for managing Tipmotaten.
 */
@RestController
@RequestMapping("/api")
public class TipmotatenResource {

    private final Logger log = LoggerFactory.getLogger(TipmotatenResource.class);

    private static final String ENTITY_NAME = "tipmotaten";

    private final TipmotatenRepository tipmotatenRepository;

    private final TipmotatenSearchRepository tipmotatenSearchRepository;

    public TipmotatenResource(TipmotatenRepository tipmotatenRepository, TipmotatenSearchRepository tipmotatenSearchRepository) {
        this.tipmotatenRepository = tipmotatenRepository;
        this.tipmotatenSearchRepository = tipmotatenSearchRepository;
    }

    /**
     * POST  /tipmotatens : Create a new tipmotaten.
     *
     * @param tipmotaten the tipmotaten to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipmotaten, or with status 400 (Bad Request) if the tipmotaten has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipmotatens")
    @Timed
    public ResponseEntity<Tipmotaten> createTipmotaten(@Valid @RequestBody Tipmotaten tipmotaten) throws URISyntaxException {
        log.debug("REST request to save Tipmotaten : {}", tipmotaten);
        if (tipmotaten.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tipmotaten cannot already have an ID")).body(null);
        }
        Tipmotaten result = tipmotatenRepository.save(tipmotaten);
        tipmotatenSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tipmotatens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipmotatens : Updates an existing tipmotaten.
     *
     * @param tipmotaten the tipmotaten to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipmotaten,
     * or with status 400 (Bad Request) if the tipmotaten is not valid,
     * or with status 500 (Internal Server Error) if the tipmotaten couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tipmotatens")
    @Timed
    public ResponseEntity<Tipmotaten> updateTipmotaten(@Valid @RequestBody Tipmotaten tipmotaten) throws URISyntaxException {
        log.debug("REST request to update Tipmotaten : {}", tipmotaten);
        if (tipmotaten.getId() == null) {
            return createTipmotaten(tipmotaten);
        }
        Tipmotaten result = tipmotatenRepository.save(tipmotaten);
        tipmotatenSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tipmotaten.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipmotatens : get all the tipmotatens.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tipmotatens in body
     */
    @GetMapping("/tipmotatens")
    @Timed
    public List<Tipmotaten> getAllTipmotatens() {
        log.debug("REST request to get all Tipmotatens");
        return tipmotatenRepository.findAll();
        }

    /**
     * GET  /tipmotatens/:id : get the "id" tipmotaten.
     *
     * @param id the id of the tipmotaten to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipmotaten, or with status 404 (Not Found)
     */
    @GetMapping("/tipmotatens/{id}")
    @Timed
    public ResponseEntity<Tipmotaten> getTipmotaten(@PathVariable Long id) {
        log.debug("REST request to get Tipmotaten : {}", id);
        Tipmotaten tipmotaten = tipmotatenRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tipmotaten));
    }

    /**
     * DELETE  /tipmotatens/:id : delete the "id" tipmotaten.
     *
     * @param id the id of the tipmotaten to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tipmotatens/{id}")
    @Timed
    public ResponseEntity<Void> deleteTipmotaten(@PathVariable Long id) {
        log.debug("REST request to delete Tipmotaten : {}", id);
        tipmotatenRepository.delete(id);
        tipmotatenSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tipmotatens?query=:query : search for the tipmotaten corresponding
     * to the query.
     *
     * @param query the query of the tipmotaten search
     * @return the result of the search
     */
    @GetMapping("/_search/tipmotatens")
    @Timed
    public List<Tipmotaten> searchTipmotatens(@RequestParam String query) {
        log.debug("REST request to search Tipmotatens for query {}", query);
        return StreamSupport
            .stream(tipmotatenSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
