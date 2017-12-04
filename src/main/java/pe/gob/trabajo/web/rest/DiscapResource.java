package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Discap;

import pe.gob.trabajo.repository.DiscapRepository;
import pe.gob.trabajo.repository.search.DiscapSearchRepository;
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
 * REST controller for managing Discap.
 */
@RestController
@RequestMapping("/api")
public class DiscapResource {

    private final Logger log = LoggerFactory.getLogger(DiscapResource.class);

    private static final String ENTITY_NAME = "discap";

    private final DiscapRepository discapRepository;

    private final DiscapSearchRepository discapSearchRepository;

    public DiscapResource(DiscapRepository discapRepository, DiscapSearchRepository discapSearchRepository) {
        this.discapRepository = discapRepository;
        this.discapSearchRepository = discapSearchRepository;
    }

    /**
     * POST  /discaps : Create a new discap.
     *
     * @param discap the discap to create
     * @return the ResponseEntity with status 201 (Created) and with body the new discap, or with status 400 (Bad Request) if the discap has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/discaps")
    @Timed
    public ResponseEntity<Discap> createDiscap(@Valid @RequestBody Discap discap) throws URISyntaxException {
        log.debug("REST request to save Discap : {}", discap);
        if (discap.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new discap cannot already have an ID")).body(null);
        }
        Discap result = discapRepository.save(discap);
        discapSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/discaps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /discaps : Updates an existing discap.
     *
     * @param discap the discap to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated discap,
     * or with status 400 (Bad Request) if the discap is not valid,
     * or with status 500 (Internal Server Error) if the discap couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/discaps")
    @Timed
    public ResponseEntity<Discap> updateDiscap(@Valid @RequestBody Discap discap) throws URISyntaxException {
        log.debug("REST request to update Discap : {}", discap);
        if (discap.getId() == null) {
            return createDiscap(discap);
        }
        Discap result = discapRepository.save(discap);
        discapSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, discap.getId().toString()))
            .body(result);
    }

    /**
     * GET  /discaps : get all the discaps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of discaps in body
     */
    @GetMapping("/discaps")
    @Timed
    public List<Discap> getAllDiscaps() {
        log.debug("REST request to get all Discaps");
        return discapRepository.findAll();
        }

    /**
     * GET  /discaps/:id : get the "id" discap.
     *
     * @param id the id of the discap to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the discap, or with status 404 (Not Found)
     */
    @GetMapping("/discaps/{id}")
    @Timed
    public ResponseEntity<Discap> getDiscap(@PathVariable Long id) {
        log.debug("REST request to get Discap : {}", id);
        Discap discap = discapRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(discap));
    }

    /** JH
     * GET  /discaps : get all the discaps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of discaps in body
     */
    @GetMapping("/discaps/activos")
    @Timed
    public List<Discap> getAll_Activos() {
        log.debug("REST request to get all discaps");
        return discapRepository.findAll_Activos();
    }

    /**
     * DELETE  /discaps/:id : delete the "id" discap.
     *
     * @param id the id of the discap to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/discaps/{id}")
    @Timed
    public ResponseEntity<Void> deleteDiscap(@PathVariable Long id) {
        log.debug("REST request to delete Discap : {}", id);
        discapRepository.delete(id);
        discapSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/discaps?query=:query : search for the discap corresponding
     * to the query.
     *
     * @param query the query of the discap search
     * @return the result of the search
     */
    @GetMapping("/_search/discaps")
    @Timed
    public List<Discap> searchDiscaps(@RequestParam String query) {
        log.debug("REST request to search Discaps for query {}", query);
        return StreamSupport
            .stream(discapSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
