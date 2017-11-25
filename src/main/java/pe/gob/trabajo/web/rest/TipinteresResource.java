package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Tipinteres;

import pe.gob.trabajo.repository.TipinteresRepository;
import pe.gob.trabajo.repository.search.TipinteresSearchRepository;
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
 * REST controller for managing Tipinteres.
 */
@RestController
@RequestMapping("/api")
public class TipinteresResource {

    private final Logger log = LoggerFactory.getLogger(TipinteresResource.class);

    private static final String ENTITY_NAME = "tipinteres";

    private final TipinteresRepository tipinteresRepository;

    private final TipinteresSearchRepository tipinteresSearchRepository;

    public TipinteresResource(TipinteresRepository tipinteresRepository, TipinteresSearchRepository tipinteresSearchRepository) {
        this.tipinteresRepository = tipinteresRepository;
        this.tipinteresSearchRepository = tipinteresSearchRepository;
    }

    /**
     * POST  /tipinteres : Create a new tipinteres.
     *
     * @param tipinteres the tipinteres to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipinteres, or with status 400 (Bad Request) if the tipinteres has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipinteres")
    @Timed
    public ResponseEntity<Tipinteres> createTipinteres(@Valid @RequestBody Tipinteres tipinteres) throws URISyntaxException {
        log.debug("REST request to save Tipinteres : {}", tipinteres);
        if (tipinteres.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tipinteres cannot already have an ID")).body(null);
        }
        Tipinteres result = tipinteresRepository.save(tipinteres);
        tipinteresSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tipinteres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipinteres : Updates an existing tipinteres.
     *
     * @param tipinteres the tipinteres to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipinteres,
     * or with status 400 (Bad Request) if the tipinteres is not valid,
     * or with status 500 (Internal Server Error) if the tipinteres couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tipinteres")
    @Timed
    public ResponseEntity<Tipinteres> updateTipinteres(@Valid @RequestBody Tipinteres tipinteres) throws URISyntaxException {
        log.debug("REST request to update Tipinteres : {}", tipinteres);
        if (tipinteres.getId() == null) {
            return createTipinteres(tipinteres);
        }
        Tipinteres result = tipinteresRepository.save(tipinteres);
        tipinteresSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tipinteres.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipinteres : get all the tipinteres.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tipinteres in body
     */
    @GetMapping("/tipinteres")
    @Timed
    public List<Tipinteres> getAllTipinteres() {
        log.debug("REST request to get all Tipinteres");
        return tipinteresRepository.findAll();
        }

    /**
     * GET  /tipinteres/:id : get the "id" tipinteres.
     *
     * @param id the id of the tipinteres to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipinteres, or with status 404 (Not Found)
     */
    @GetMapping("/tipinteres/{id}")
    @Timed
    public ResponseEntity<Tipinteres> getTipinteres(@PathVariable Long id) {
        log.debug("REST request to get Tipinteres : {}", id);
        Tipinteres tipinteres = tipinteresRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tipinteres));
    }

    /**
     * DELETE  /tipinteres/:id : delete the "id" tipinteres.
     *
     * @param id the id of the tipinteres to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tipinteres/{id}")
    @Timed
    public ResponseEntity<Void> deleteTipinteres(@PathVariable Long id) {
        log.debug("REST request to delete Tipinteres : {}", id);
        tipinteresRepository.delete(id);
        tipinteresSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tipinteres?query=:query : search for the tipinteres corresponding
     * to the query.
     *
     * @param query the query of the tipinteres search
     * @return the result of the search
     */
    @GetMapping("/_search/tipinteres")
    @Timed
    public List<Tipinteres> searchTipinteres(@RequestParam String query) {
        log.debug("REST request to search Tipinteres for query {}", query);
        return StreamSupport
            .stream(tipinteresSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
