package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Accionadop;

import pe.gob.trabajo.repository.AccionadopRepository;
import pe.gob.trabajo.repository.search.AccionadopSearchRepository;
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
 * REST controller for managing Accionadop.
 */
@RestController
@RequestMapping("/api")
public class AccionadopResource {

    private final Logger log = LoggerFactory.getLogger(AccionadopResource.class);

    private static final String ENTITY_NAME = "accionadop";

    private final AccionadopRepository accionadopRepository;

    private final AccionadopSearchRepository accionadopSearchRepository;

    public AccionadopResource(AccionadopRepository accionadopRepository, AccionadopSearchRepository accionadopSearchRepository) {
        this.accionadopRepository = accionadopRepository;
        this.accionadopSearchRepository = accionadopSearchRepository;
    }

    /**
     * POST  /accionadops : Create a new accionadop.
     *
     * @param accionadop the accionadop to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accionadop, or with status 400 (Bad Request) if the accionadop has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/accionadops")
    @Timed
    public ResponseEntity<Accionadop> createAccionadop(@Valid @RequestBody Accionadop accionadop) throws URISyntaxException {
        log.debug("REST request to save Accionadop : {}", accionadop);
        if (accionadop.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new accionadop cannot already have an ID")).body(null);
        }
        Accionadop result = accionadopRepository.save(accionadop);
        accionadopSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/accionadops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /accionadops : Updates an existing accionadop.
     *
     * @param accionadop the accionadop to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accionadop,
     * or with status 400 (Bad Request) if the accionadop is not valid,
     * or with status 500 (Internal Server Error) if the accionadop couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/accionadops")
    @Timed
    public ResponseEntity<Accionadop> updateAccionadop(@Valid @RequestBody Accionadop accionadop) throws URISyntaxException {
        log.debug("REST request to update Accionadop : {}", accionadop);
        if (accionadop.getId() == null) {
            return createAccionadop(accionadop);
        }
        Accionadop result = accionadopRepository.save(accionadop);
        accionadopSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accionadop.getId().toString()))
            .body(result);
    }

    /**
     * GET  /accionadops : get all the accionadops.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accionadops in body
     */
    @GetMapping("/accionadops")
    @Timed
    public List<Accionadop> getAllAccionadops() {
        log.debug("REST request to get all Accionadops");
        return accionadopRepository.findAll();
        }

    /**
     * GET  /accionadops/:id : get the "id" accionadop.
     *
     * @param id the id of the accionadop to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accionadop, or with status 404 (Not Found)
     */
    @GetMapping("/accionadops/{id}")
    @Timed
    public ResponseEntity<Accionadop> getAccionadop(@PathVariable Long id) {
        log.debug("REST request to get Accionadop : {}", id);
        Accionadop accionadop = accionadopRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accionadop));
    }

    /**
     * DELETE  /accionadops/:id : delete the "id" accionadop.
     *
     * @param id the id of the accionadop to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/accionadops/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccionadop(@PathVariable Long id) {
        log.debug("REST request to delete Accionadop : {}", id);
        accionadopRepository.delete(id);
        accionadopSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/accionadops?query=:query : search for the accionadop corresponding
     * to the query.
     *
     * @param query the query of the accionadop search
     * @return the result of the search
     */
    @GetMapping("/_search/accionadops")
    @Timed
    public List<Accionadop> searchAccionadops(@RequestParam String query) {
        log.debug("REST request to search Accionadops for query {}", query);
        return StreamSupport
            .stream(accionadopSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
