package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Empleador;

import pe.gob.trabajo.repository.EmpleadorRepository;
import pe.gob.trabajo.repository.search.EmpleadorSearchRepository;
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
 * REST controller for managing Empleador.
 */
@RestController
@RequestMapping("/api")
public class EmpleadorResource {

    private final Logger log = LoggerFactory.getLogger(EmpleadorResource.class);

    private static final String ENTITY_NAME = "empleador";

    private final EmpleadorRepository empleadorRepository;

    private final EmpleadorSearchRepository empleadorSearchRepository;

    public EmpleadorResource(EmpleadorRepository empleadorRepository, EmpleadorSearchRepository empleadorSearchRepository) {
        this.empleadorRepository = empleadorRepository;
        this.empleadorSearchRepository = empleadorSearchRepository;
    }

    /**
     * POST  /empleadors : Create a new empleador.
     *
     * @param empleador the empleador to create
     * @return the ResponseEntity with status 201 (Created) and with body the new empleador, or with status 400 (Bad Request) if the empleador has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/empleadors")
    @Timed
    public ResponseEntity<Empleador> createEmpleador(@Valid @RequestBody Empleador empleador) throws URISyntaxException {
        log.debug("REST request to save Empleador : {}", empleador);
        if (empleador.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new empleador cannot already have an ID")).body(null);
        }
        Empleador result = empleadorRepository.save(empleador);
        empleadorSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/empleadors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /empleadors : Updates an existing empleador.
     *
     * @param empleador the empleador to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated empleador,
     * or with status 400 (Bad Request) if the empleador is not valid,
     * or with status 500 (Internal Server Error) if the empleador couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/empleadors")
    @Timed
    public ResponseEntity<Empleador> updateEmpleador(@Valid @RequestBody Empleador empleador) throws URISyntaxException {
        log.debug("REST request to update Empleador : {}", empleador);
        if (empleador.getId() == null) {
            return createEmpleador(empleador);
        }
        Empleador result = empleadorRepository.save(empleador);
        empleadorSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, empleador.getId().toString()))
            .body(result);
    }

    /**
     * GET  /empleadors : get all the empleadors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of empleadors in body
     */
    @GetMapping("/empleadors")
    @Timed
    public List<Empleador> getAllEmpleadors() {
        log.debug("REST request to get all Empleadors");
        return empleadorRepository.findAll();
        }

    /**
     * GET  /empleadors/:id : get the "id" empleador.
     *
     * @param id the id of the empleador to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the empleador, or with status 404 (Not Found)
     */
    @GetMapping("/empleadors/{id}")
    @Timed
    public ResponseEntity<Empleador> getEmpleador(@PathVariable Long id) {
        log.debug("REST request to get Empleador : {}", id);
        Empleador empleador = empleadorRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(empleador));
    }

    /**
     * DELETE  /empleadors/:id : delete the "id" empleador.
     *
     * @param id the id of the empleador to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/empleadors/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmpleador(@PathVariable Long id) {
        log.debug("REST request to delete Empleador : {}", id);
        empleadorRepository.delete(id);
        empleadorSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/empleadors?query=:query : search for the empleador corresponding
     * to the query.
     *
     * @param query the query of the empleador search
     * @return the result of the search
     */
    @GetMapping("/_search/empleadors")
    @Timed
    public List<Empleador> searchEmpleadors(@RequestParam String query) {
        log.debug("REST request to search Empleadors for query {}", query);
        return StreamSupport
            .stream(empleadorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
