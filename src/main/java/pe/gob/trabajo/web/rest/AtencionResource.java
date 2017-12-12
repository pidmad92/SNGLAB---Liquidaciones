package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Atencion;

import pe.gob.trabajo.repository.AtencionRepository;
import pe.gob.trabajo.repository.search.AtencionSearchRepository;
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
 * REST controller for managing Atencion.
 */
@RestController
@RequestMapping("/api")
public class AtencionResource {

    private final Logger log = LoggerFactory.getLogger(AtencionResource.class);

    private static final String ENTITY_NAME = "atencion";

    private final AtencionRepository atencionRepository;

    private final AtencionSearchRepository atencionSearchRepository;

    public AtencionResource(AtencionRepository atencionRepository, AtencionSearchRepository atencionSearchRepository) {
        this.atencionRepository = atencionRepository;
        this.atencionSearchRepository = atencionSearchRepository;
    }

    /**
     * POST  /atencions : Create a new atencion.
     *
     * @param atencion the atencion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new atencion, or with status 400 (Bad Request) if the atencion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/atencions")
    @Timed
    public ResponseEntity<Atencion> createAtencion(@Valid @RequestBody Atencion atencion) throws URISyntaxException {
        log.debug("REST request to save Atencion : {}", atencion);
        if (atencion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new atencion cannot already have an ID")).body(null);
        }
        Atencion result = atencionRepository.save(atencion);
        atencionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/atencions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /atencions : Updates an existing atencion.
     *
     * @param atencion the atencion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated atencion,
     * or with status 400 (Bad Request) if the atencion is not valid,
     * or with status 500 (Internal Server Error) if the atencion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/atencions")
    @Timed
    public ResponseEntity<Atencion> updateAtencion(@Valid @RequestBody Atencion atencion) throws URISyntaxException {
        log.debug("REST request to update Atencion : {}", atencion);
        if (atencion.getId() == null) {
            return createAtencion(atencion);
        }
        Atencion result = atencionRepository.save(atencion);
        atencionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, atencion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /atencions : get all the atencions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of atencions in body
     */
    @GetMapping("/atencions")
    @Timed
    public List<Atencion> getAllAtencions() {
        log.debug("REST request to get all Atencions");
        return atencionRepository.findAll();
        }

    /**
     * GET  /atencions/:id : get the "id" atencion.
     *
     * @param id the id of the atencion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the atencion, or with status 404 (Not Found)
     */
    @GetMapping("/atencions/{id}")
    @Timed
    public ResponseEntity<Atencion> getAtencion(@PathVariable Long id) {
        log.debug("REST request to get Atencion : {}", id);
        Atencion atencion = atencionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(atencion));
    }

    /** JH
     * GET  /atencions : get all the atencions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of atencions in body
     */
    @GetMapping("/atencions/activos")
    @Timed
    public List<Atencion> getAll_Activos() {
        log.debug("REST request to get all atencions");
        return atencionRepository.findAll_Activos();
        }

     /** JH
     * GET  /atencions/empleador/id/:id_emple/
     * @param id_emple es el id del empleador
     * @return the ResponseEntity with status 200 (OK) and with body the atencion, or with status 404 (Not Found)
     */
	@GetMapping("/atencions/empleador/id/{id_emple}")
    @Timed
    public List<Atencion> getListAtencionByIdEmpleador(@PathVariable Long id_emple) {
        log.debug("REST request to get Atencion : id_emple empleador {}", id_emple);
        return atencionRepository.findListAtencionByIdEmpleador(id_emple);
    }

     /** JH
     * GET  /atencions/trabajador/id/:id_trab/
     * @param id_trab es el id del trabajador
     * @return the ResponseEntity with status 200 (OK) and with body the atencion, or with status 404 (Not Found)
     */
	@GetMapping("/atencions/trabajador/id/{id_trab}")
    @Timed
    public List<Atencion> getListAtencionByIdTrabajador(@PathVariable Long id_trab) {
        log.debug("REST request to get Atencion : id trabajador {}", id_trab);
        return atencionRepository.findListAtencionByIdTrabajador(id_trab);
    }

     /** JH
     * GET  /atencions/atenpase/trabajador/id/:id_trab/
     * @param id_trab es el id del trabajador
     * @return the ResponseEntity with status 200 (OK) and with body the atencion, or with status 404 (Not Found)
     */
	@GetMapping("/atencions/atenpase/trabajador/id/{id_trab}")
    @Timed
    public List<Atencion> getfindListAtenPase_ByIdTrabajador(@PathVariable Long id_trab) {
        log.debug("REST request to get Atencion : id trabajador {}", id_trab);
        return atencionRepository.findListAtenPase_ByIdTrabajador(id_trab);
    }

    /** JH
     * GET  /atencions/liquidaciones/trabajador/id/:id_trab/
     * @param id_trab es el id del trabajador
     * @return the ResponseEntity with status 200 (OK) and the list of atencions in body
     */
    @GetMapping("/atencions/liquidaciones/trabajador/id/{id_trab}")
    @Timed
    public List<Atencion> getLiquidaciones_ByIdTrabajador(@PathVariable Long id_trab) {
        log.debug("REST request to get Atencion : id trabajador {}", id_trab);
        return atencionRepository.findLiquidaciones_ByIdTrabajador(id_trab);
    }

    /**
     * DELETE  /atencions/:id : delete the "id" atencion.
     *
     * @param id the id of the atencion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/atencions/{id}")
    @Timed
    public ResponseEntity<Void> deleteAtencion(@PathVariable Long id) {
        log.debug("REST request to delete Atencion : {}", id);
        atencionRepository.delete(id);
        atencionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/atencions?query=:query : search for the atencion corresponding
     * to the query.
     *
     * @param query the query of the atencion search
     * @return the result of the search
     */
    @GetMapping("/_search/atencions")
    @Timed
    public List<Atencion> searchAtencions(@RequestParam String query) {
        log.debug("REST request to search Atencions for query {}", query);
        return StreamSupport
            .stream(atencionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
