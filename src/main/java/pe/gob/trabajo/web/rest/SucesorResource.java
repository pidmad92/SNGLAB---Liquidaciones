package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Sucesor;

import pe.gob.trabajo.repository.SucesorRepository;
import pe.gob.trabajo.repository.search.SucesorSearchRepository;
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
 * REST controller for managing Sucesor.
 */
@RestController
@RequestMapping("/api")
public class SucesorResource {

    private final Logger log = LoggerFactory.getLogger(SucesorResource.class);

    private static final String ENTITY_NAME = "sucesor";

    private final SucesorRepository sucesorRepository;

    private final SucesorSearchRepository sucesorSearchRepository;

    public SucesorResource(SucesorRepository sucesorRepository, SucesorSearchRepository sucesorSearchRepository) {
        this.sucesorRepository = sucesorRepository;
        this.sucesorSearchRepository = sucesorSearchRepository;
    }

    /**
     * POST  /sucesors : Create a new sucesor.
     *
     * @param sucesor the sucesor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sucesor, or with status 400 (Bad Request) if the sucesor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sucesors")
    @Timed
    public ResponseEntity<Sucesor> createSucesor(@Valid @RequestBody Sucesor sucesor) throws URISyntaxException {
        log.debug("REST request to save Sucesor : {}", sucesor);
        if (sucesor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sucesor cannot already have an ID")).body(null);
        }
        Sucesor result = sucesorRepository.save(sucesor);
        sucesorSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sucesors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sucesors : Updates an existing sucesor.
     *
     * @param sucesor the sucesor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sucesor,
     * or with status 400 (Bad Request) if the sucesor is not valid,
     * or with status 500 (Internal Server Error) if the sucesor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sucesors")
    @Timed
    public ResponseEntity<Sucesor> updateSucesor(@Valid @RequestBody Sucesor sucesor) throws URISyntaxException {
        log.debug("REST request to update Sucesor : {}", sucesor);
        if (sucesor.getId() == null) {
            return createSucesor(sucesor);
        }
        Sucesor result = sucesorRepository.save(sucesor);
        sucesorSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sucesor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sucesors : get all the sucesors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sucesors in body
     */
    @GetMapping("/sucesors")
    @Timed
    public List<Sucesor> getAllSucesors() {
        log.debug("REST request to get all Sucesors");
        return sucesorRepository.findAll();
        }

    /**
     * GET  /sucesors/:id : get the "id" sucesor.
     *
     * @param id the id of the sucesor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sucesor, or with status 404 (Not Found)
     */
    @GetMapping("/sucesors/{id}")
    @Timed
    public ResponseEntity<Sucesor> getSucesor(@PathVariable Long id) {
        log.debug("REST request to get Sucesor : {}", id);
        Sucesor sucesor = sucesorRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sucesor));
    }

    /**
     * DELETE  /sucesors/:id : delete the "id" sucesor.
     *
     * @param id the id of the sucesor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sucesors/{id}")
    @Timed
    public ResponseEntity<Void> deleteSucesor(@PathVariable Long id) {
        log.debug("REST request to delete Sucesor : {}", id);
        sucesorRepository.delete(id);
        sucesorSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sucesors?query=:query : search for the sucesor corresponding
     * to the query.
     *
     * @param query the query of the sucesor search
     * @return the result of the search
     */
    @GetMapping("/_search/sucesors")
    @Timed
    public List<Sucesor> searchSucesors(@RequestParam String query) {
        log.debug("REST request to search Sucesors for query {}", query);
        return StreamSupport
            .stream(sucesorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
