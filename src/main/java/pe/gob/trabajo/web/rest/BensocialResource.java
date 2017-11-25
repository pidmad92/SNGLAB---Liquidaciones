package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Bensocial;

import pe.gob.trabajo.repository.BensocialRepository;
import pe.gob.trabajo.repository.search.BensocialSearchRepository;
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
 * REST controller for managing Bensocial.
 */
@RestController
@RequestMapping("/api")
public class BensocialResource {

    private final Logger log = LoggerFactory.getLogger(BensocialResource.class);

    private static final String ENTITY_NAME = "bensocial";

    private final BensocialRepository bensocialRepository;

    private final BensocialSearchRepository bensocialSearchRepository;

    public BensocialResource(BensocialRepository bensocialRepository, BensocialSearchRepository bensocialSearchRepository) {
        this.bensocialRepository = bensocialRepository;
        this.bensocialSearchRepository = bensocialSearchRepository;
    }

    /**
     * POST  /bensocials : Create a new bensocial.
     *
     * @param bensocial the bensocial to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bensocial, or with status 400 (Bad Request) if the bensocial has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bensocials")
    @Timed
    public ResponseEntity<Bensocial> createBensocial(@Valid @RequestBody Bensocial bensocial) throws URISyntaxException {
        log.debug("REST request to save Bensocial : {}", bensocial);
        if (bensocial.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new bensocial cannot already have an ID")).body(null);
        }
        Bensocial result = bensocialRepository.save(bensocial);
        bensocialSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bensocials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bensocials : Updates an existing bensocial.
     *
     * @param bensocial the bensocial to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bensocial,
     * or with status 400 (Bad Request) if the bensocial is not valid,
     * or with status 500 (Internal Server Error) if the bensocial couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bensocials")
    @Timed
    public ResponseEntity<Bensocial> updateBensocial(@Valid @RequestBody Bensocial bensocial) throws URISyntaxException {
        log.debug("REST request to update Bensocial : {}", bensocial);
        if (bensocial.getId() == null) {
            return createBensocial(bensocial);
        }
        Bensocial result = bensocialRepository.save(bensocial);
        bensocialSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bensocial.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bensocials : get all the bensocials.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bensocials in body
     */
    @GetMapping("/bensocials")
    @Timed
    public List<Bensocial> getAllBensocials() {
        log.debug("REST request to get all Bensocials");
        return bensocialRepository.findAll();
        }

    /**
     * GET  /bensocials/:id : get the "id" bensocial.
     *
     * @param id the id of the bensocial to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bensocial, or with status 404 (Not Found)
     */
    @GetMapping("/bensocials/{id}")
    @Timed
    public ResponseEntity<Bensocial> getBensocial(@PathVariable Long id) {
        log.debug("REST request to get Bensocial : {}", id);
        Bensocial bensocial = bensocialRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bensocial));
    }

    /**
     * DELETE  /bensocials/:id : delete the "id" bensocial.
     *
     * @param id the id of the bensocial to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bensocials/{id}")
    @Timed
    public ResponseEntity<Void> deleteBensocial(@PathVariable Long id) {
        log.debug("REST request to delete Bensocial : {}", id);
        bensocialRepository.delete(id);
        bensocialSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bensocials?query=:query : search for the bensocial corresponding
     * to the query.
     *
     * @param query the query of the bensocial search
     * @return the result of the search
     */
    @GetMapping("/_search/bensocials")
    @Timed
    public List<Bensocial> searchBensocials(@RequestParam String query) {
        log.debug("REST request to search Bensocials for query {}", query);
        return StreamSupport
            .stream(bensocialSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
