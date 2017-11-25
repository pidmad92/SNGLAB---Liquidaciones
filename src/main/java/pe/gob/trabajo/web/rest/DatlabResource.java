package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Datlab;

import pe.gob.trabajo.repository.DatlabRepository;
import pe.gob.trabajo.repository.search.DatlabSearchRepository;
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
 * REST controller for managing Datlab.
 */
@RestController
@RequestMapping("/api")
public class DatlabResource {

    private final Logger log = LoggerFactory.getLogger(DatlabResource.class);

    private static final String ENTITY_NAME = "datlab";

    private final DatlabRepository datlabRepository;

    private final DatlabSearchRepository datlabSearchRepository;

    public DatlabResource(DatlabRepository datlabRepository, DatlabSearchRepository datlabSearchRepository) {
        this.datlabRepository = datlabRepository;
        this.datlabSearchRepository = datlabSearchRepository;
    }

    /**
     * POST  /datlabs : Create a new datlab.
     *
     * @param datlab the datlab to create
     * @return the ResponseEntity with status 201 (Created) and with body the new datlab, or with status 400 (Bad Request) if the datlab has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/datlabs")
    @Timed
    public ResponseEntity<Datlab> createDatlab(@Valid @RequestBody Datlab datlab) throws URISyntaxException {
        log.debug("REST request to save Datlab : {}", datlab);
        if (datlab.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new datlab cannot already have an ID")).body(null);
        }
        Datlab result = datlabRepository.save(datlab);
        datlabSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/datlabs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /datlabs : Updates an existing datlab.
     *
     * @param datlab the datlab to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated datlab,
     * or with status 400 (Bad Request) if the datlab is not valid,
     * or with status 500 (Internal Server Error) if the datlab couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/datlabs")
    @Timed
    public ResponseEntity<Datlab> updateDatlab(@Valid @RequestBody Datlab datlab) throws URISyntaxException {
        log.debug("REST request to update Datlab : {}", datlab);
        if (datlab.getId() == null) {
            return createDatlab(datlab);
        }
        Datlab result = datlabRepository.save(datlab);
        datlabSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, datlab.getId().toString()))
            .body(result);
    }

    /**
     * GET  /datlabs : get all the datlabs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of datlabs in body
     */
    @GetMapping("/datlabs")
    @Timed
    public List<Datlab> getAllDatlabs() {
        log.debug("REST request to get all Datlabs");
        return datlabRepository.findAll();
        }

    /**
     * GET  /datlabs/:id : get the "id" datlab.
     *
     * @param id the id of the datlab to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the datlab, or with status 404 (Not Found)
     */
    @GetMapping("/datlabs/{id}")
    @Timed
    public ResponseEntity<Datlab> getDatlab(@PathVariable Long id) {
        log.debug("REST request to get Datlab : {}", id);
        Datlab datlab = datlabRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(datlab));
    }

    /**
     * DELETE  /datlabs/:id : delete the "id" datlab.
     *
     * @param id the id of the datlab to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/datlabs/{id}")
    @Timed
    public ResponseEntity<Void> deleteDatlab(@PathVariable Long id) {
        log.debug("REST request to delete Datlab : {}", id);
        datlabRepository.delete(id);
        datlabSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/datlabs?query=:query : search for the datlab corresponding
     * to the query.
     *
     * @param query the query of the datlab search
     * @return the result of the search
     */
    @GetMapping("/_search/datlabs")
    @Timed
    public List<Datlab> searchDatlabs(@RequestParam String query) {
        log.debug("REST request to search Datlabs for query {}", query);
        return StreamSupport
            .stream(datlabSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
