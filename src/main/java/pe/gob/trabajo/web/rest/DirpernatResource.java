package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Dirpernat;

import pe.gob.trabajo.repository.DirpernatRepository;
import pe.gob.trabajo.repository.search.DirpernatSearchRepository;
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
 * REST controller for managing Dirpernat.
 */
@RestController
@RequestMapping("/api")
public class DirpernatResource {

    private final Logger log = LoggerFactory.getLogger(DirpernatResource.class);

    private static final String ENTITY_NAME = "dirpernat";

    private final DirpernatRepository dirpernatRepository;

    private final DirpernatSearchRepository dirpernatSearchRepository;

    public DirpernatResource(DirpernatRepository dirpernatRepository, DirpernatSearchRepository dirpernatSearchRepository) {
        this.dirpernatRepository = dirpernatRepository;
        this.dirpernatSearchRepository = dirpernatSearchRepository;
    }

    /**
     * POST  /dirpernats : Create a new dirpernat.
     *
     * @param dirpernat the dirpernat to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dirpernat, or with status 400 (Bad Request) if the dirpernat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/dirpernats")
    @Timed
    public ResponseEntity<Dirpernat> createDirpernat(@Valid @RequestBody Dirpernat dirpernat) throws URISyntaxException {
        log.debug("REST request to save Dirpernat : {}", dirpernat);
        if (dirpernat.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dirpernat cannot already have an ID")).body(null);
        }
        Dirpernat result = dirpernatRepository.save(dirpernat);
        dirpernatSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/dirpernats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dirpernats : Updates an existing dirpernat.
     *
     * @param dirpernat the dirpernat to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dirpernat,
     * or with status 400 (Bad Request) if the dirpernat is not valid,
     * or with status 500 (Internal Server Error) if the dirpernat couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/dirpernats")
    @Timed
    public ResponseEntity<Dirpernat> updateDirpernat(@Valid @RequestBody Dirpernat dirpernat) throws URISyntaxException {
        log.debug("REST request to update Dirpernat : {}", dirpernat);
        if (dirpernat.getId() == null) {
            return createDirpernat(dirpernat);
        }
        Dirpernat result = dirpernatRepository.save(dirpernat);
        dirpernatSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dirpernat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dirpernats : get all the dirpernats.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dirpernats in body
     */
    @GetMapping("/dirpernats")
    @Timed
    public List<Dirpernat> getAllDirpernats() {
        log.debug("REST request to get all Dirpernats");
        return dirpernatRepository.findAll();
        }

    /**
     * GET  /dirpernats/:id : get the "id" dirpernat.
     *
     * @param id the id of the dirpernat to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dirpernat, or with status 404 (Not Found)
     */
    @GetMapping("/dirpernats/{id}")
    @Timed
    public ResponseEntity<Dirpernat> getDirpernat(@PathVariable Long id) {
        log.debug("REST request to get Dirpernat : {}", id);
        Dirpernat dirpernat = dirpernatRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dirpernat));
    }

    /**
     * DELETE  /dirpernats/:id : delete the "id" dirpernat.
     *
     * @param id the id of the dirpernat to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/dirpernats/{id}")
    @Timed
    public ResponseEntity<Void> deleteDirpernat(@PathVariable Long id) {
        log.debug("REST request to delete Dirpernat : {}", id);
        dirpernatRepository.delete(id);
        dirpernatSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/dirpernats?query=:query : search for the dirpernat corresponding
     * to the query.
     *
     * @param query the query of the dirpernat search
     * @return the result of the search
     */
    @GetMapping("/_search/dirpernats")
    @Timed
    public List<Dirpernat> searchDirpernats(@RequestParam String query) {
        log.debug("REST request to search Dirpernats for query {}", query);
        return StreamSupport
            .stream(dirpernatSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
