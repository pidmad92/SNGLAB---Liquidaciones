package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Pasegl;

import pe.gob.trabajo.repository.PaseglRepository;
import pe.gob.trabajo.repository.search.PaseglSearchRepository;
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
 * REST controller for managing Pasegl.
 */
@RestController
@RequestMapping("/api")
public class PaseglResource {

    private final Logger log = LoggerFactory.getLogger(PaseglResource.class);

    private static final String ENTITY_NAME = "pasegl";

    private final PaseglRepository paseglRepository;

    private final PaseglSearchRepository paseglSearchRepository;

    public PaseglResource(PaseglRepository paseglRepository, PaseglSearchRepository paseglSearchRepository) {
        this.paseglRepository = paseglRepository;
        this.paseglSearchRepository = paseglSearchRepository;
    }

    /**
     * POST  /pasegls : Create a new pasegl.
     *
     * @param pasegl the pasegl to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pasegl, or with status 400 (Bad Request) if the pasegl has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pasegls")
    @Timed
    public ResponseEntity<Pasegl> createPasegl(@Valid @RequestBody Pasegl pasegl) throws URISyntaxException {
        log.debug("REST request to save Pasegl : {}", pasegl);
        if (pasegl.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pasegl cannot already have an ID")).body(null);
        }
        Pasegl result = paseglRepository.save(pasegl);
        paseglSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pasegls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pasegls : Updates an existing pasegl.
     *
     * @param pasegl the pasegl to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pasegl,
     * or with status 400 (Bad Request) if the pasegl is not valid,
     * or with status 500 (Internal Server Error) if the pasegl couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pasegls")
    @Timed
    public ResponseEntity<Pasegl> updatePasegl(@Valid @RequestBody Pasegl pasegl) throws URISyntaxException {
        log.debug("REST request to update Pasegl : {}", pasegl);
        if (pasegl.getId() == null) {
            return createPasegl(pasegl);
        }
        Pasegl result = paseglRepository.save(pasegl);
        paseglSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pasegl.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pasegls : get all the pasegls.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pasegls in body
     */
    @GetMapping("/pasegls")
    @Timed
    public List<Pasegl> getAllPasegls() {
        log.debug("REST request to get all Pasegls");
        return paseglRepository.findAll();
        }

    /**
     * GET  /pasegls/:id : get the "id" pasegl.
     *
     * @param id the id of the pasegl to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pasegl, or with status 404 (Not Found)
     */
    @GetMapping("/pasegls/{id}")
    @Timed
    public ResponseEntity<Pasegl> getPasegl(@PathVariable Long id) {
        log.debug("REST request to get Pasegl : {}", id);
        Pasegl pasegl = paseglRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pasegl));
    }

     /** JH
     * GET  /pasegls/pases/trabajador/:id_trab/oficina/:id_ofic/estado/:vEstado
     * @param id_trab es el id del Trabajador
     * @param id_ofic es el id de la Oficina que Atiende el pase
     * @param vEstado es el estado del pase
     * @return the ResponseEntity with status 200 (OK) and with body the pasegl, or with status 404 (Not Found)
     */
	@GetMapping("/pasegls/pases/trabajador/{id_trab}/oficina/{id_ofic}/estado/{vEstado}")
    @Timed
    public List<Pasegl> getPasegl_Pendientes_By_IdTrabajador_IdOficina(@PathVariable Long id_trab,@PathVariable Long id_ofic, @PathVariable String vEstado) {
        log.debug("REST request to get pasegls : id_trab {} - id_ofic {}", id_trab, id_ofic, vEstado);
        return paseglRepository.findPasegl_Pendientes_By_IdTrabajador_IdOficina(id_trab, id_ofic, vEstado);
    }

    /** JH
     * GET  /pasegls : get all the pasegls.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pasegls in body
     */
    @GetMapping("/pasegls/activos")
    @Timed
    public List<Pasegl> getAll_Activos() {
        log.debug("REST request to get all pasegls");
        return paseglRepository.findAll_Activos();
    }

     /** JH
     * GET  /pasegls/pase/id/:id_pase/ : get the "id_pase" 
     * @param id_pase es el id del Pasegl
     * @return the ResponseEntity with status 200 (OK) and with body the Pasegl, or with status 404 (Not Found)
     */
	@GetMapping("/pasegls/pase/id/{id_pase}")
    @Timed
    public List<Pasegl> getPaseglById(@PathVariable Long id_pase) {
        log.debug("REST request to get Pasegl : id_pase {} ", id_pase);
        return paseglRepository.findPaseglById(id_pase);
    }

    /**
     * DELETE  /pasegls/:id : delete the "id" pasegl.
     *
     * @param id the id of the pasegl to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pasegls/{id}")
    @Timed
    public ResponseEntity<Void> deletePasegl(@PathVariable Long id) {
        log.debug("REST request to delete Pasegl : {}", id);
        paseglRepository.delete(id);
        paseglSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pasegls?query=:query : search for the pasegl corresponding
     * to the query.
     *
     * @param query the query of the pasegl search
     * @return the result of the search
     */
    @GetMapping("/_search/pasegls")
    @Timed
    public List<Pasegl> searchPasegls(@RequestParam String query) {
        log.debug("REST request to search Pasegls for query {}", query);
        return StreamSupport
            .stream(paseglSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
