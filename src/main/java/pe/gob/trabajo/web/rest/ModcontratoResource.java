package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Modcontrato;

import pe.gob.trabajo.repository.ModcontratoRepository;
import pe.gob.trabajo.repository.search.ModcontratoSearchRepository;
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
 * REST controller for managing Modcontrato.
 */
@RestController
@RequestMapping("/api")
public class ModcontratoResource {

    private final Logger log = LoggerFactory.getLogger(ModcontratoResource.class);

    private static final String ENTITY_NAME = "modcontrato";

    private final ModcontratoRepository modcontratoRepository;

    private final ModcontratoSearchRepository modcontratoSearchRepository;

    public ModcontratoResource(ModcontratoRepository modcontratoRepository, ModcontratoSearchRepository modcontratoSearchRepository) {
        this.modcontratoRepository = modcontratoRepository;
        this.modcontratoSearchRepository = modcontratoSearchRepository;
    }

    /**
     * POST  /modcontratoes : Create a new modcontrato.
     *
     * @param modcontrato the modcontrato to create
     * @return the ResponseEntity with status 201 (Created) and with body the new modcontrato, or with status 400 (Bad Request) if the modcontrato has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/modcontratoes")
    @Timed
    public ResponseEntity<Modcontrato> createModcontrato(@Valid @RequestBody Modcontrato modcontrato) throws URISyntaxException {
        log.debug("REST request to save Modcontrato : {}", modcontrato);
        if (modcontrato.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new modcontrato cannot already have an ID")).body(null);
        }
        Modcontrato result = modcontratoRepository.save(modcontrato);
        modcontratoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/modcontratoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /modcontratoes : Updates an existing modcontrato.
     *
     * @param modcontrato the modcontrato to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated modcontrato,
     * or with status 400 (Bad Request) if the modcontrato is not valid,
     * or with status 500 (Internal Server Error) if the modcontrato couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/modcontratoes")
    @Timed
    public ResponseEntity<Modcontrato> updateModcontrato(@Valid @RequestBody Modcontrato modcontrato) throws URISyntaxException {
        log.debug("REST request to update Modcontrato : {}", modcontrato);
        if (modcontrato.getId() == null) {
            return createModcontrato(modcontrato);
        }
        Modcontrato result = modcontratoRepository.save(modcontrato);
        modcontratoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, modcontrato.getId().toString()))
            .body(result);
    }

    /**
     * GET  /modcontratoes : get all the modcontratoes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of modcontratoes in body
     */
    @GetMapping("/modcontratoes")
    @Timed
    public List<Modcontrato> getAllModcontratoes() {
        log.debug("REST request to get all Modcontratoes");
        return modcontratoRepository.findAll();
        }

    /**
     * GET  /modcontratoes/:id : get the "id" modcontrato.
     *
     * @param id the id of the modcontrato to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the modcontrato, or with status 404 (Not Found)
     */
    @GetMapping("/modcontratoes/{id}")
    @Timed
    public ResponseEntity<Modcontrato> getModcontrato(@PathVariable Long id) {
        log.debug("REST request to get Modcontrato : {}", id);
        Modcontrato modcontrato = modcontratoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(modcontrato));
    }

    /**
     * DELETE  /modcontratoes/:id : delete the "id" modcontrato.
     *
     * @param id the id of the modcontrato to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/modcontratoes/{id}")
    @Timed
    public ResponseEntity<Void> deleteModcontrato(@PathVariable Long id) {
        log.debug("REST request to delete Modcontrato : {}", id);
        modcontratoRepository.delete(id);
        modcontratoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/modcontratoes?query=:query : search for the modcontrato corresponding
     * to the query.
     *
     * @param query the query of the modcontrato search
     * @return the result of the search
     */
    @GetMapping("/_search/modcontratoes")
    @Timed
    public List<Modcontrato> searchModcontratoes(@RequestParam String query) {
        log.debug("REST request to search Modcontratoes for query {}", query);
        return StreamSupport
            .stream(modcontratoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
