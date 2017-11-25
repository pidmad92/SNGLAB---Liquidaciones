package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Docingrper;

import pe.gob.trabajo.repository.DocingrperRepository;
import pe.gob.trabajo.repository.search.DocingrperSearchRepository;
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
 * REST controller for managing Docingrper.
 */
@RestController
@RequestMapping("/api")
public class DocingrperResource {

    private final Logger log = LoggerFactory.getLogger(DocingrperResource.class);

    private static final String ENTITY_NAME = "docingrper";

    private final DocingrperRepository docingrperRepository;

    private final DocingrperSearchRepository docingrperSearchRepository;

    public DocingrperResource(DocingrperRepository docingrperRepository, DocingrperSearchRepository docingrperSearchRepository) {
        this.docingrperRepository = docingrperRepository;
        this.docingrperSearchRepository = docingrperSearchRepository;
    }

    /**
     * POST  /docingrpers : Create a new docingrper.
     *
     * @param docingrper the docingrper to create
     * @return the ResponseEntity with status 201 (Created) and with body the new docingrper, or with status 400 (Bad Request) if the docingrper has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/docingrpers")
    @Timed
    public ResponseEntity<Docingrper> createDocingrper(@Valid @RequestBody Docingrper docingrper) throws URISyntaxException {
        log.debug("REST request to save Docingrper : {}", docingrper);
        if (docingrper.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new docingrper cannot already have an ID")).body(null);
        }
        Docingrper result = docingrperRepository.save(docingrper);
        docingrperSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/docingrpers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /docingrpers : Updates an existing docingrper.
     *
     * @param docingrper the docingrper to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated docingrper,
     * or with status 400 (Bad Request) if the docingrper is not valid,
     * or with status 500 (Internal Server Error) if the docingrper couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/docingrpers")
    @Timed
    public ResponseEntity<Docingrper> updateDocingrper(@Valid @RequestBody Docingrper docingrper) throws URISyntaxException {
        log.debug("REST request to update Docingrper : {}", docingrper);
        if (docingrper.getId() == null) {
            return createDocingrper(docingrper);
        }
        Docingrper result = docingrperRepository.save(docingrper);
        docingrperSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, docingrper.getId().toString()))
            .body(result);
    }

    /**
     * GET  /docingrpers : get all the docingrpers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of docingrpers in body
     */
    @GetMapping("/docingrpers")
    @Timed
    public List<Docingrper> getAllDocingrpers() {
        log.debug("REST request to get all Docingrpers");
        return docingrperRepository.findAll();
        }

    /**
     * GET  /docingrpers/:id : get the "id" docingrper.
     *
     * @param id the id of the docingrper to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the docingrper, or with status 404 (Not Found)
     */
    @GetMapping("/docingrpers/{id}")
    @Timed
    public ResponseEntity<Docingrper> getDocingrper(@PathVariable Long id) {
        log.debug("REST request to get Docingrper : {}", id);
        Docingrper docingrper = docingrperRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(docingrper));
    }

    /**
     * DELETE  /docingrpers/:id : delete the "id" docingrper.
     *
     * @param id the id of the docingrper to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/docingrpers/{id}")
    @Timed
    public ResponseEntity<Void> deleteDocingrper(@PathVariable Long id) {
        log.debug("REST request to delete Docingrper : {}", id);
        docingrperRepository.delete(id);
        docingrperSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/docingrpers?query=:query : search for the docingrper corresponding
     * to the query.
     *
     * @param query the query of the docingrper search
     * @return the result of the search
     */
    @GetMapping("/_search/docingrpers")
    @Timed
    public List<Docingrper> searchDocingrpers(@RequestParam String query) {
        log.debug("REST request to search Docingrpers for query {}", query);
        return StreamSupport
            .stream(docingrperSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
