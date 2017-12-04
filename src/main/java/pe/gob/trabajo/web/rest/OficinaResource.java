package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Oficina;

import pe.gob.trabajo.repository.OficinaRepository;
import pe.gob.trabajo.repository.search.OficinaSearchRepository;
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
 * REST controller for managing Oficina.
 */
@RestController
@RequestMapping("/api")
public class OficinaResource {

    private final Logger log = LoggerFactory.getLogger(OficinaResource.class);

    private static final String ENTITY_NAME = "oficina";

    private final OficinaRepository oficinaRepository;

    private final OficinaSearchRepository oficinaSearchRepository;

    public OficinaResource(OficinaRepository oficinaRepository, OficinaSearchRepository oficinaSearchRepository) {
        this.oficinaRepository = oficinaRepository;
        this.oficinaSearchRepository = oficinaSearchRepository;
    }

    /**
     * POST  /oficinas : Create a new oficina.
     *
     * @param oficina the oficina to create
     * @return the ResponseEntity with status 201 (Created) and with body the new oficina, or with status 400 (Bad Request) if the oficina has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/oficinas")
    @Timed
    public ResponseEntity<Oficina> createOficina(@Valid @RequestBody Oficina oficina) throws URISyntaxException {
        log.debug("REST request to save Oficina : {}", oficina);
        if (oficina.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new oficina cannot already have an ID")).body(null);
        }
        Oficina result = oficinaRepository.save(oficina);
        oficinaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/oficinas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /oficinas : Updates an existing oficina.
     *
     * @param oficina the oficina to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated oficina,
     * or with status 400 (Bad Request) if the oficina is not valid,
     * or with status 500 (Internal Server Error) if the oficina couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/oficinas")
    @Timed
    public ResponseEntity<Oficina> updateOficina(@Valid @RequestBody Oficina oficina) throws URISyntaxException {
        log.debug("REST request to update Oficina : {}", oficina);
        if (oficina.getId() == null) {
            return createOficina(oficina);
        }
        Oficina result = oficinaRepository.save(oficina);
        oficinaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, oficina.getId().toString()))
            .body(result);
    }

    /**
     * GET  /oficinas : get all the oficinas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of oficinas in body
     */
    @GetMapping("/oficinas")
    @Timed
    public List<Oficina> getAllOficinas() {
        log.debug("REST request to get all Oficinas");
        return oficinaRepository.findAll();
        }

    /**
     * GET  /oficinas/:id : get the "id" oficina.
     *
     * @param id the id of the oficina to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the oficina, or with status 404 (Not Found)
     */
    @GetMapping("/oficinas/{id}")
    @Timed
    public ResponseEntity<Oficina> getOficina(@PathVariable Long id) {
        log.debug("REST request to get Oficina : {}", id);
        Oficina oficina = oficinaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(oficina));
    }

    /** JH
     * GET  /oficinas : get all the oficinas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of oficinas in body
     */
    @GetMapping("/oficinas/activos")
    @Timed
    public List<Oficina> getAll_Activos() {
        log.debug("REST request to get all oficinas");
        return oficinaRepository.findAll_Activos();
    }

    /**
     * DELETE  /oficinas/:id : delete the "id" oficina.
     *
     * @param id the id of the oficina to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/oficinas/{id}")
    @Timed
    public ResponseEntity<Void> deleteOficina(@PathVariable Long id) {
        log.debug("REST request to delete Oficina : {}", id);
        oficinaRepository.delete(id);
        oficinaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/oficinas?query=:query : search for the oficina corresponding
     * to the query.
     *
     * @param query the query of the oficina search
     * @return the result of the search
     */
    @GetMapping("/_search/oficinas")
    @Timed
    public List<Oficina> searchOficinas(@RequestParam String query) {
        log.debug("REST request to search Oficinas for query {}", query);
        return StreamSupport
            .stream(oficinaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
