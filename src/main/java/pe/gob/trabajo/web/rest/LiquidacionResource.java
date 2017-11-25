package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Liquidacion;

import pe.gob.trabajo.repository.LiquidacionRepository;
import pe.gob.trabajo.repository.search.LiquidacionSearchRepository;
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
 * REST controller for managing Liquidacion.
 */
@RestController
@RequestMapping("/api")
public class LiquidacionResource {

    private final Logger log = LoggerFactory.getLogger(LiquidacionResource.class);

    private static final String ENTITY_NAME = "liquidacion";

    private final LiquidacionRepository liquidacionRepository;

    private final LiquidacionSearchRepository liquidacionSearchRepository;

    public LiquidacionResource(LiquidacionRepository liquidacionRepository, LiquidacionSearchRepository liquidacionSearchRepository) {
        this.liquidacionRepository = liquidacionRepository;
        this.liquidacionSearchRepository = liquidacionSearchRepository;
    }

    /**
     * POST  /liquidacions : Create a new liquidacion.
     *
     * @param liquidacion the liquidacion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new liquidacion, or with status 400 (Bad Request) if the liquidacion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/liquidacions")
    @Timed
    public ResponseEntity<Liquidacion> createLiquidacion(@Valid @RequestBody Liquidacion liquidacion) throws URISyntaxException {
        log.debug("REST request to save Liquidacion : {}", liquidacion);
        if (liquidacion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new liquidacion cannot already have an ID")).body(null);
        }
        Liquidacion result = liquidacionRepository.save(liquidacion);
        liquidacionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/liquidacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /liquidacions : Updates an existing liquidacion.
     *
     * @param liquidacion the liquidacion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated liquidacion,
     * or with status 400 (Bad Request) if the liquidacion is not valid,
     * or with status 500 (Internal Server Error) if the liquidacion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/liquidacions")
    @Timed
    public ResponseEntity<Liquidacion> updateLiquidacion(@Valid @RequestBody Liquidacion liquidacion) throws URISyntaxException {
        log.debug("REST request to update Liquidacion : {}", liquidacion);
        if (liquidacion.getId() == null) {
            return createLiquidacion(liquidacion);
        }
        Liquidacion result = liquidacionRepository.save(liquidacion);
        liquidacionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, liquidacion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /liquidacions : get all the liquidacions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of liquidacions in body
     */
    @GetMapping("/liquidacions")
    @Timed
    public List<Liquidacion> getAllLiquidacions() {
        log.debug("REST request to get all Liquidacions");
        return liquidacionRepository.findAll();
        }

    /**
     * GET  /liquidacions/:id : get the "id" liquidacion.
     *
     * @param id the id of the liquidacion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the liquidacion, or with status 404 (Not Found)
     */
    @GetMapping("/liquidacions/{id}")
    @Timed
    public ResponseEntity<Liquidacion> getLiquidacion(@PathVariable Long id) {
        log.debug("REST request to get Liquidacion : {}", id);
        Liquidacion liquidacion = liquidacionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(liquidacion));
    }

    /**
     * DELETE  /liquidacions/:id : delete the "id" liquidacion.
     *
     * @param id the id of the liquidacion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/liquidacions/{id}")
    @Timed
    public ResponseEntity<Void> deleteLiquidacion(@PathVariable Long id) {
        log.debug("REST request to delete Liquidacion : {}", id);
        liquidacionRepository.delete(id);
        liquidacionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/liquidacions?query=:query : search for the liquidacion corresponding
     * to the query.
     *
     * @param query the query of the liquidacion search
     * @return the result of the search
     */
    @GetMapping("/_search/liquidacions")
    @Timed
    public List<Liquidacion> searchLiquidacions(@RequestParam String query) {
        log.debug("REST request to search Liquidacions for query {}", query);
        return StreamSupport
            .stream(liquidacionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
