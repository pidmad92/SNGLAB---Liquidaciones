package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Discapate;

import pe.gob.trabajo.repository.DiscapateRepository;
import pe.gob.trabajo.repository.search.DiscapateSearchRepository;
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
 * REST controller for managing Discapate.
 */
@RestController
@RequestMapping("/api")
public class DiscapateResource {

    private final Logger log = LoggerFactory.getLogger(DiscapateResource.class);

    private static final String ENTITY_NAME = "discapate";

    private final DiscapateRepository discapateRepository;

    private final DiscapateSearchRepository discapateSearchRepository;

    public DiscapateResource(DiscapateRepository discapateRepository, DiscapateSearchRepository discapateSearchRepository) {
        this.discapateRepository = discapateRepository;
        this.discapateSearchRepository = discapateSearchRepository;
    }

    /**
     * POST  /discapates : Create a new discapate.
     *
     * @param discapate the discapate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new discapate, or with status 400 (Bad Request) if the discapate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/discapates")
    @Timed
    public ResponseEntity<Discapate> createDiscapate(@Valid @RequestBody Discapate discapate) throws URISyntaxException {
        log.debug("REST request to save Discapate : {}", discapate);
        if (discapate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new discapate cannot already have an ID")).body(null);
        }
        Discapate result = discapateRepository.save(discapate);
        discapateSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/discapates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /discapates : Updates an existing discapate.
     *
     * @param discapate the discapate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated discapate,
     * or with status 400 (Bad Request) if the discapate is not valid,
     * or with status 500 (Internal Server Error) if the discapate couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/discapates")
    @Timed
    public ResponseEntity<Discapate> updateDiscapate(@Valid @RequestBody Discapate discapate) throws URISyntaxException {
        log.debug("REST request to update Discapate : {}", discapate);
        if (discapate.getId() == null) {
            return createDiscapate(discapate);
        }
        Discapate result = discapateRepository.save(discapate);
        discapateSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, discapate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /discapates : get all the discapates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of discapates in body
     */
    @GetMapping("/discapates")
    @Timed
    public List<Discapate> getAllDiscapates() {
        log.debug("REST request to get all Discapates");
        return discapateRepository.findAll();
        }

    /**
     * GET  /discapates/:id : get the "id" discapate.
     *
     * @param id the id of the discapate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the discapate, or with status 404 (Not Found)
     */
    @GetMapping("/discapates/{id}")
    @Timed
    public ResponseEntity<Discapate> getDiscapate(@PathVariable Long id) {
        log.debug("REST request to get Discapate : {}", id);
        Discapate discapate = discapateRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(discapate));
    }

    /**
     * DELETE  /discapates/:id : delete the "id" discapate.
     *
     * @param id the id of the discapate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/discapates/{id}")
    @Timed
    public ResponseEntity<Void> deleteDiscapate(@PathVariable Long id) {
        log.debug("REST request to delete Discapate : {}", id);
        discapateRepository.delete(id);
        discapateSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/discapates?query=:query : search for the discapate corresponding
     * to the query.
     *
     * @param query the query of the discapate search
     * @return the result of the search
     */
    @GetMapping("/_search/discapates")
    @Timed
    public List<Discapate> searchDiscapates(@RequestParam String query) {
        log.debug("REST request to search Discapates for query {}", query);
        return StreamSupport
            .stream(discapateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
