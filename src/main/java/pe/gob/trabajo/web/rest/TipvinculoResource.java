package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Tipvinculo;

import pe.gob.trabajo.repository.TipvinculoRepository;
import pe.gob.trabajo.repository.search.TipvinculoSearchRepository;
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
 * REST controller for managing Tipvinculo.
 */
@RestController
@RequestMapping("/api")
public class TipvinculoResource {

    private final Logger log = LoggerFactory.getLogger(TipvinculoResource.class);

    private static final String ENTITY_NAME = "tipvinculo";

    private final TipvinculoRepository tipvinculoRepository;

    private final TipvinculoSearchRepository tipvinculoSearchRepository;

    public TipvinculoResource(TipvinculoRepository tipvinculoRepository, TipvinculoSearchRepository tipvinculoSearchRepository) {
        this.tipvinculoRepository = tipvinculoRepository;
        this.tipvinculoSearchRepository = tipvinculoSearchRepository;
    }

    /**
     * POST  /tipvinculos : Create a new tipvinculo.
     *
     * @param tipvinculo the tipvinculo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipvinculo, or with status 400 (Bad Request) if the tipvinculo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipvinculos")
    @Timed
    public ResponseEntity<Tipvinculo> createTipvinculo(@Valid @RequestBody Tipvinculo tipvinculo) throws URISyntaxException {
        log.debug("REST request to save Tipvinculo : {}", tipvinculo);
        if (tipvinculo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tipvinculo cannot already have an ID")).body(null);
        }
        Tipvinculo result = tipvinculoRepository.save(tipvinculo);
        tipvinculoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tipvinculos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipvinculos : Updates an existing tipvinculo.
     *
     * @param tipvinculo the tipvinculo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipvinculo,
     * or with status 400 (Bad Request) if the tipvinculo is not valid,
     * or with status 500 (Internal Server Error) if the tipvinculo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tipvinculos")
    @Timed
    public ResponseEntity<Tipvinculo> updateTipvinculo(@Valid @RequestBody Tipvinculo tipvinculo) throws URISyntaxException {
        log.debug("REST request to update Tipvinculo : {}", tipvinculo);
        if (tipvinculo.getId() == null) {
            return createTipvinculo(tipvinculo);
        }
        Tipvinculo result = tipvinculoRepository.save(tipvinculo);
        tipvinculoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tipvinculo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipvinculos : get all the tipvinculos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tipvinculos in body
     */
    @GetMapping("/tipvinculos")
    @Timed
    public List<Tipvinculo> getAllTipvinculos() {
        log.debug("REST request to get all Tipvinculos");
        return tipvinculoRepository.findAll();
        }

    /**
     * GET  /tipvinculos/:id : get the "id" tipvinculo.
     *
     * @param id the id of the tipvinculo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipvinculo, or with status 404 (Not Found)
     */
    @GetMapping("/tipvinculos/{id}")
    @Timed
    public ResponseEntity<Tipvinculo> getTipvinculo(@PathVariable Long id) {
        log.debug("REST request to get Tipvinculo : {}", id);
        Tipvinculo tipvinculo = tipvinculoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tipvinculo));
    }

    /**
     * DELETE  /tipvinculos/:id : delete the "id" tipvinculo.
     *
     * @param id the id of the tipvinculo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tipvinculos/{id}")
    @Timed
    public ResponseEntity<Void> deleteTipvinculo(@PathVariable Long id) {
        log.debug("REST request to delete Tipvinculo : {}", id);
        tipvinculoRepository.delete(id);
        tipvinculoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tipvinculos?query=:query : search for the tipvinculo corresponding
     * to the query.
     *
     * @param query the query of the tipvinculo search
     * @return the result of the search
     */
    @GetMapping("/_search/tipvinculos")
    @Timed
    public List<Tipvinculo> searchTipvinculos(@RequestParam String query) {
        log.debug("REST request to search Tipvinculos for query {}", query);
        return StreamSupport
            .stream(tipvinculoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
