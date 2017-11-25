package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Tippersona;

import pe.gob.trabajo.repository.TippersonaRepository;
import pe.gob.trabajo.repository.search.TippersonaSearchRepository;
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
 * REST controller for managing Tippersona.
 */
@RestController
@RequestMapping("/api")
public class TippersonaResource {

    private final Logger log = LoggerFactory.getLogger(TippersonaResource.class);

    private static final String ENTITY_NAME = "tippersona";

    private final TippersonaRepository tippersonaRepository;

    private final TippersonaSearchRepository tippersonaSearchRepository;

    public TippersonaResource(TippersonaRepository tippersonaRepository, TippersonaSearchRepository tippersonaSearchRepository) {
        this.tippersonaRepository = tippersonaRepository;
        this.tippersonaSearchRepository = tippersonaSearchRepository;
    }

    /**
     * POST  /tippersonas : Create a new tippersona.
     *
     * @param tippersona the tippersona to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tippersona, or with status 400 (Bad Request) if the tippersona has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tippersonas")
    @Timed
    public ResponseEntity<Tippersona> createTippersona(@Valid @RequestBody Tippersona tippersona) throws URISyntaxException {
        log.debug("REST request to save Tippersona : {}", tippersona);
        if (tippersona.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tippersona cannot already have an ID")).body(null);
        }
        Tippersona result = tippersonaRepository.save(tippersona);
        tippersonaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tippersonas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tippersonas : Updates an existing tippersona.
     *
     * @param tippersona the tippersona to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tippersona,
     * or with status 400 (Bad Request) if the tippersona is not valid,
     * or with status 500 (Internal Server Error) if the tippersona couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tippersonas")
    @Timed
    public ResponseEntity<Tippersona> updateTippersona(@Valid @RequestBody Tippersona tippersona) throws URISyntaxException {
        log.debug("REST request to update Tippersona : {}", tippersona);
        if (tippersona.getId() == null) {
            return createTippersona(tippersona);
        }
        Tippersona result = tippersonaRepository.save(tippersona);
        tippersonaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tippersona.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tippersonas : get all the tippersonas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tippersonas in body
     */
    @GetMapping("/tippersonas")
    @Timed
    public List<Tippersona> getAllTippersonas() {
        log.debug("REST request to get all Tippersonas");
        return tippersonaRepository.findAll();
        }

    /**
     * GET  /tippersonas/:id : get the "id" tippersona.
     *
     * @param id the id of the tippersona to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tippersona, or with status 404 (Not Found)
     */
    @GetMapping("/tippersonas/{id}")
    @Timed
    public ResponseEntity<Tippersona> getTippersona(@PathVariable Long id) {
        log.debug("REST request to get Tippersona : {}", id);
        Tippersona tippersona = tippersonaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tippersona));
    }

    /**
     * DELETE  /tippersonas/:id : delete the "id" tippersona.
     *
     * @param id the id of the tippersona to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tippersonas/{id}")
    @Timed
    public ResponseEntity<Void> deleteTippersona(@PathVariable Long id) {
        log.debug("REST request to delete Tippersona : {}", id);
        tippersonaRepository.delete(id);
        tippersonaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tippersonas?query=:query : search for the tippersona corresponding
     * to the query.
     *
     * @param query the query of the tippersona search
     * @return the result of the search
     */
    @GetMapping("/_search/tippersonas")
    @Timed
    public List<Tippersona> searchTippersonas(@RequestParam String query) {
        log.debug("REST request to search Tippersonas for query {}", query);
        return StreamSupport
            .stream(tippersonaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
