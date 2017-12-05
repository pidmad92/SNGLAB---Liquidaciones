package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Moneda;

import pe.gob.trabajo.repository.MonedaRepository;
import pe.gob.trabajo.repository.search.MonedaSearchRepository;
import pe.gob.trabajo.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing Moneda.
 */
@RestController
@RequestMapping("/api")
public class MonedaResource {

    private final Logger log = LoggerFactory.getLogger(MonedaResource.class);

    private static final String ENTITY_NAME = "moneda";

    private final MonedaRepository monedaRepository;

    private final MonedaSearchRepository monedaSearchRepository;

    public MonedaResource(MonedaRepository monedaRepository, MonedaSearchRepository monedaSearchRepository) {
        this.monedaRepository = monedaRepository;
        this.monedaSearchRepository = monedaSearchRepository;
    }

    /**
     * POST  /monedas : Create a new moneda.
     *
     * @param moneda the moneda to create
     * @return the ResponseEntity with status 201 (Created) and with body the new moneda, or with status 400 (Bad Request) if the moneda has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/monedas")
    @Timed
    public ResponseEntity<Moneda> createMoneda(@Valid @RequestBody Moneda moneda) throws URISyntaxException {
        log.debug("REST request to save Moneda : {}", moneda);
        if (moneda.getId() != null) {
            throw new BadRequestAlertException("A new moneda cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Moneda result = monedaRepository.save(moneda);
        monedaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/monedas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /monedas : Updates an existing moneda.
     *
     * @param moneda the moneda to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated moneda,
     * or with status 400 (Bad Request) if the moneda is not valid,
     * or with status 500 (Internal Server Error) if the moneda couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/monedas")
    @Timed
    public ResponseEntity<Moneda> updateMoneda(@Valid @RequestBody Moneda moneda) throws URISyntaxException {
        log.debug("REST request to update Moneda : {}", moneda);
        if (moneda.getId() == null) {
            return createMoneda(moneda);
        }
        Moneda result = monedaRepository.save(moneda);
        monedaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, moneda.getId().toString()))
            .body(result);
    }

    /**
     * GET  /monedas : get all the monedas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of monedas in body
     */
    @GetMapping("/monedas")
    @Timed
    public List<Moneda> getAllMonedas() {
        log.debug("REST request to get all Monedas");
        return monedaRepository.findAll();
        }

    /**
     * GET  /monedas/:id : get the "id" moneda.
     *
     * @param id the id of the moneda to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the moneda, or with status 404 (Not Found)
     */
    @GetMapping("/monedas/{id}")
    @Timed
    public ResponseEntity<Moneda> getMoneda(@PathVariable Long id) {
        log.debug("REST request to get Moneda : {}", id);
        Moneda moneda = monedaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(moneda));
    }

    /**
     * DELETE  /monedas/:id : delete the "id" moneda.
     *
     * @param id the id of the moneda to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/monedas/{id}")
    @Timed
    public ResponseEntity<Void> deleteMoneda(@PathVariable Long id) {
        log.debug("REST request to delete Moneda : {}", id);
        monedaRepository.delete(id);
        monedaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/monedas?query=:query : search for the moneda corresponding
     * to the query.
     *
     * @param query the query of the moneda search
     * @return the result of the search
     */
    @GetMapping("/_search/monedas")
    @Timed
    public List<Moneda> searchMonedas(@RequestParam String query) {
        log.debug("REST request to search Monedas for query {}", query);
        return StreamSupport
            .stream(monedaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
