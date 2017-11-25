package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Tipatencion;

import pe.gob.trabajo.repository.TipatencionRepository;
import pe.gob.trabajo.repository.search.TipatencionSearchRepository;
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
 * REST controller for managing Tipatencion.
 */
@RestController
@RequestMapping("/api")
public class TipatencionResource {

    private final Logger log = LoggerFactory.getLogger(TipatencionResource.class);

    private static final String ENTITY_NAME = "tipatencion";

    private final TipatencionRepository tipatencionRepository;

    private final TipatencionSearchRepository tipatencionSearchRepository;

    public TipatencionResource(TipatencionRepository tipatencionRepository, TipatencionSearchRepository tipatencionSearchRepository) {
        this.tipatencionRepository = tipatencionRepository;
        this.tipatencionSearchRepository = tipatencionSearchRepository;
    }

    /**
     * POST  /tipatencions : Create a new tipatencion.
     *
     * @param tipatencion the tipatencion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipatencion, or with status 400 (Bad Request) if the tipatencion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipatencions")
    @Timed
    public ResponseEntity<Tipatencion> createTipatencion(@Valid @RequestBody Tipatencion tipatencion) throws URISyntaxException {
        log.debug("REST request to save Tipatencion : {}", tipatencion);
        if (tipatencion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tipatencion cannot already have an ID")).body(null);
        }
        Tipatencion result = tipatencionRepository.save(tipatencion);
        tipatencionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tipatencions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipatencions : Updates an existing tipatencion.
     *
     * @param tipatencion the tipatencion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipatencion,
     * or with status 400 (Bad Request) if the tipatencion is not valid,
     * or with status 500 (Internal Server Error) if the tipatencion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tipatencions")
    @Timed
    public ResponseEntity<Tipatencion> updateTipatencion(@Valid @RequestBody Tipatencion tipatencion) throws URISyntaxException {
        log.debug("REST request to update Tipatencion : {}", tipatencion);
        if (tipatencion.getId() == null) {
            return createTipatencion(tipatencion);
        }
        Tipatencion result = tipatencionRepository.save(tipatencion);
        tipatencionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tipatencion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipatencions : get all the tipatencions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tipatencions in body
     */
    @GetMapping("/tipatencions")
    @Timed
    public List<Tipatencion> getAllTipatencions() {
        log.debug("REST request to get all Tipatencions");
        return tipatencionRepository.findAll();
        }

    /**
     * GET  /tipatencions/:id : get the "id" tipatencion.
     *
     * @param id the id of the tipatencion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipatencion, or with status 404 (Not Found)
     */
    @GetMapping("/tipatencions/{id}")
    @Timed
    public ResponseEntity<Tipatencion> getTipatencion(@PathVariable Long id) {
        log.debug("REST request to get Tipatencion : {}", id);
        Tipatencion tipatencion = tipatencionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tipatencion));
    }

    /**
     * DELETE  /tipatencions/:id : delete the "id" tipatencion.
     *
     * @param id the id of the tipatencion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tipatencions/{id}")
    @Timed
    public ResponseEntity<Void> deleteTipatencion(@PathVariable Long id) {
        log.debug("REST request to delete Tipatencion : {}", id);
        tipatencionRepository.delete(id);
        tipatencionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tipatencions?query=:query : search for the tipatencion corresponding
     * to the query.
     *
     * @param query the query of the tipatencion search
     * @return the result of the search
     */
    @GetMapping("/_search/tipatencions")
    @Timed
    public List<Tipatencion> searchTipatencions(@RequestParam String query) {
        log.debug("REST request to search Tipatencions for query {}", query);
        return StreamSupport
            .stream(tipatencionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
