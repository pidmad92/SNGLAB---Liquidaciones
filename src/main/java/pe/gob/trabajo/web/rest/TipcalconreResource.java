package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Tipcalconre;

import pe.gob.trabajo.repository.TipcalconreRepository;
import pe.gob.trabajo.repository.search.TipcalconreSearchRepository;
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
 * REST controller for managing Tipcalconre.
 */
@RestController
@RequestMapping("/api")
public class TipcalconreResource {

    private final Logger log = LoggerFactory.getLogger(TipcalconreResource.class);

    private static final String ENTITY_NAME = "tipcalconre";

    private final TipcalconreRepository tipcalconreRepository;

    private final TipcalconreSearchRepository tipcalconreSearchRepository;

    public TipcalconreResource(TipcalconreRepository tipcalconreRepository, TipcalconreSearchRepository tipcalconreSearchRepository) {
        this.tipcalconreRepository = tipcalconreRepository;
        this.tipcalconreSearchRepository = tipcalconreSearchRepository;
    }

    /**
     * POST  /tipcalconres : Create a new tipcalconre.
     *
     * @param tipcalconre the tipcalconre to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipcalconre, or with status 400 (Bad Request) if the tipcalconre has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipcalconres")
    @Timed
    public ResponseEntity<Tipcalconre> createTipcalconre(@Valid @RequestBody Tipcalconre tipcalconre) throws URISyntaxException {
        log.debug("REST request to save Tipcalconre : {}", tipcalconre);
        if (tipcalconre.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tipcalconre cannot already have an ID")).body(null);
        }
        Tipcalconre result = tipcalconreRepository.save(tipcalconre);
        tipcalconreSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tipcalconres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipcalconres : Updates an existing tipcalconre.
     *
     * @param tipcalconre the tipcalconre to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipcalconre,
     * or with status 400 (Bad Request) if the tipcalconre is not valid,
     * or with status 500 (Internal Server Error) if the tipcalconre couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tipcalconres")
    @Timed
    public ResponseEntity<Tipcalconre> updateTipcalconre(@Valid @RequestBody Tipcalconre tipcalconre) throws URISyntaxException {
        log.debug("REST request to update Tipcalconre : {}", tipcalconre);
        if (tipcalconre.getId() == null) {
            return createTipcalconre(tipcalconre);
        }
        Tipcalconre result = tipcalconreRepository.save(tipcalconre);
        tipcalconreSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tipcalconre.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipcalconres : get all the tipcalconres.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tipcalconres in body
     */
    @GetMapping("/tipcalconres")
    @Timed
    public List<Tipcalconre> getAllTipcalconres() {
        log.debug("REST request to get all Tipcalconres");
        return tipcalconreRepository.findAll();
        }

    /**
     * GET  /tipcalconres/:id : get the "id" tipcalconre.
     *
     * @param id the id of the tipcalconre to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipcalconre, or with status 404 (Not Found)
     */
    @GetMapping("/tipcalconres/{id}")
    @Timed
    public ResponseEntity<Tipcalconre> getTipcalconre(@PathVariable Long id) {
        log.debug("REST request to get Tipcalconre : {}", id);
        Tipcalconre tipcalconre = tipcalconreRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tipcalconre));
    }

    /** JH
     * GET  /tipcalconres/activos : get all the tipcalconres.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tipcalconres in body
     */
    @GetMapping("/tipcalconres/activos")
    @Timed
    public List<Tipcalconre> getAll_Activos() {
        log.debug("REST request to get all tipcalconres");
        return tipcalconreRepository.findAll_Activos();
        }

    /**
     * DELETE  /tipcalconres/:id : delete the "id" tipcalconre.
     *
     * @param id the id of the tipcalconre to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tipcalconres/{id}")
    @Timed
    public ResponseEntity<Void> deleteTipcalconre(@PathVariable Long id) {
        log.debug("REST request to delete Tipcalconre : {}", id);
        tipcalconreRepository.delete(id);
        tipcalconreSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tipcalconres?query=:query : search for the tipcalconre corresponding
     * to the query.
     *
     * @param query the query of the tipcalconre search
     * @return the result of the search
     */
    @GetMapping("/_search/tipcalconres")
    @Timed
    public List<Tipcalconre> searchTipcalconres(@RequestParam String query) {
        log.debug("REST request to search Tipcalconres for query {}", query);
        return StreamSupport
            .stream(tipcalconreSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
