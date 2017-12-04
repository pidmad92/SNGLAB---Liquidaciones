package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Dirperjuri;

import pe.gob.trabajo.repository.DirperjuriRepository;
import pe.gob.trabajo.repository.search.DirperjuriSearchRepository;
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
 * REST controller for managing Dirperjuri.
 */
@RestController
@RequestMapping("/api")
public class DirperjuriResource {

    private final Logger log = LoggerFactory.getLogger(DirperjuriResource.class);

    private static final String ENTITY_NAME = "dirperjuri";

    private final DirperjuriRepository dirperjuriRepository;

    private final DirperjuriSearchRepository dirperjuriSearchRepository;

    public DirperjuriResource(DirperjuriRepository dirperjuriRepository, DirperjuriSearchRepository dirperjuriSearchRepository) {
        this.dirperjuriRepository = dirperjuriRepository;
        this.dirperjuriSearchRepository = dirperjuriSearchRepository;
    }

    /**
     * POST  /dirperjuris : Create a new dirperjuri.
     *
     * @param dirperjuri the dirperjuri to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dirperjuri, or with status 400 (Bad Request) if the dirperjuri has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/dirperjuris")
    @Timed
    public ResponseEntity<Dirperjuri> createDirperjuri(@Valid @RequestBody Dirperjuri dirperjuri) throws URISyntaxException {
        log.debug("REST request to save Dirperjuri : {}", dirperjuri);
        if (dirperjuri.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dirperjuri cannot already have an ID")).body(null);
        }
        Dirperjuri result = dirperjuriRepository.save(dirperjuri);
        dirperjuriSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/dirperjuris/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dirperjuris : Updates an existing dirperjuri.
     *
     * @param dirperjuri the dirperjuri to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dirperjuri,
     * or with status 400 (Bad Request) if the dirperjuri is not valid,
     * or with status 500 (Internal Server Error) if the dirperjuri couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/dirperjuris")
    @Timed
    public ResponseEntity<Dirperjuri> updateDirperjuri(@Valid @RequestBody Dirperjuri dirperjuri) throws URISyntaxException {
        log.debug("REST request to update Dirperjuri : {}", dirperjuri);
        if (dirperjuri.getId() == null) {
            return createDirperjuri(dirperjuri);
        }
        Dirperjuri result = dirperjuriRepository.save(dirperjuri);
        dirperjuriSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dirperjuri.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dirperjuris : get all the dirperjuris.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dirperjuris in body
     */
    @GetMapping("/dirperjuris")
    @Timed
    public List<Dirperjuri> getAllDirperjuris() {
        log.debug("REST request to get all Dirperjuris");
        return dirperjuriRepository.findAll();
        }

    /**
     * GET  /dirperjuris/:id : get the "id" dirperjuri.
     *
     * @param id the id of the dirperjuri to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dirperjuri, or with status 404 (Not Found)
     */
    @GetMapping("/dirperjuris/{id}")
    @Timed
    public ResponseEntity<Dirperjuri> getDirperjuri(@PathVariable Long id) {
        log.debug("REST request to get Dirperjuri : {}", id);
        Dirperjuri dirperjuri = dirperjuriRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dirperjuri));
    }

    /** JH
     * GET  /dirperjuris : get all the dirperjuris.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dirperjuris in body
     */
    @GetMapping("/dirperjuris/activos")
    @Timed
    public List<Dirperjuri> getAll_Activos() {
        log.debug("REST request to get all dirperjuris");
        return dirperjuriRepository.findAll_Activos();
    }

     /** JH
     * GET  /dirperjuri/empleador/id/:id_empl : 
     * @param id_empl es el id del Empleador
     * @return the ResponseEntity with status 200 (OK) and with body the dirperjuri, or with status 404 (Not Found)
     */
	@GetMapping("/dirperjuris/empleador/id/{id_empl}")
    @Timed
    public List<Dirperjuri> getListDireccionesEmpleadorById(@PathVariable Long id_empl) {
        log.debug("REST request to get Perjuridire : id_empl {}", id_empl);
        return dirperjuriRepository.findListDireccionesEmpleadorById(id_empl);
    }

    /**
     * DELETE  /dirperjuris/:id : delete the "id" dirperjuri.
     *
     * @param id the id of the dirperjuri to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/dirperjuris/{id}")
    @Timed
    public ResponseEntity<Void> deleteDirperjuri(@PathVariable Long id) {
        log.debug("REST request to delete Dirperjuri : {}", id);
        dirperjuriRepository.delete(id);
        dirperjuriSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/dirperjuris?query=:query : search for the dirperjuri corresponding
     * to the query.
     *
     * @param query the query of the dirperjuri search
     * @return the result of the search
     */
    @GetMapping("/_search/dirperjuris")
    @Timed
    public List<Dirperjuri> searchDirperjuris(@RequestParam String query) {
        log.debug("REST request to search Dirperjuris for query {}", query);
        return StreamSupport
            .stream(dirperjuriSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
