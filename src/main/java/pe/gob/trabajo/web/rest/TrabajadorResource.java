package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Trabajador;

import pe.gob.trabajo.repository.TrabajadorRepository;
import pe.gob.trabajo.repository.search.TrabajadorSearchRepository;
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
 * REST controller for managing Trabajador.
 */
@RestController
@RequestMapping("/api")
public class TrabajadorResource {

    private final Logger log = LoggerFactory.getLogger(TrabajadorResource.class);

    private static final String ENTITY_NAME = "trabajador";

    private final TrabajadorRepository trabajadorRepository;

    private final TrabajadorSearchRepository trabajadorSearchRepository;

    public TrabajadorResource(TrabajadorRepository trabajadorRepository, TrabajadorSearchRepository trabajadorSearchRepository) {
        this.trabajadorRepository = trabajadorRepository;
        this.trabajadorSearchRepository = trabajadorSearchRepository;
    }

    /**
     * POST  /trabajadors : Create a new trabajador.
     *
     * @param trabajador the trabajador to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trabajador, or with status 400 (Bad Request) if the trabajador has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/trabajadors")
    @Timed
    public ResponseEntity<Trabajador> createTrabajador(@Valid @RequestBody Trabajador trabajador) throws URISyntaxException {
        log.debug("REST request to save Trabajador : {}", trabajador);
        if (trabajador.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new trabajador cannot already have an ID")).body(null);
        }
        Trabajador result = trabajadorRepository.save(trabajador);
        trabajadorSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/trabajadors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trabajadors : Updates an existing trabajador.
     *
     * @param trabajador the trabajador to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trabajador,
     * or with status 400 (Bad Request) if the trabajador is not valid,
     * or with status 500 (Internal Server Error) if the trabajador couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/trabajadors")
    @Timed
    public ResponseEntity<Trabajador> updateTrabajador(@Valid @RequestBody Trabajador trabajador) throws URISyntaxException {
        log.debug("REST request to update Trabajador : {}", trabajador);
        if (trabajador.getId() == null) {
            return createTrabajador(trabajador);
        }
        Trabajador result = trabajadorRepository.save(trabajador);
        trabajadorSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trabajador.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trabajadors : get all the trabajadors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of trabajadors in body
     */
    @GetMapping("/trabajadors")
    @Timed
    public List<Trabajador> getAllTrabajadors() {
        log.debug("REST request to get all Trabajadors");
        return trabajadorRepository.findAll();
        }

    /**
     * GET  /trabajadors/:id : get the "id" trabajador.
     *
     * @param id the id of the trabajador to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trabajador, or with status 404 (Not Found)
     */
    @GetMapping("/trabajadors/{id}")
    @Timed
    public ResponseEntity<Trabajador> getTrabajador(@PathVariable Long id) {
        log.debug("REST request to get Trabajador : {}", id);
        Trabajador trabajador = trabajadorRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trabajador));
    }

    /** JH
     * GET  /trabajadors : get all the trabajadors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of trabajadors in body
     */
    @GetMapping("/trabajadors/activos")
    @Timed
    public List<Trabajador> getAll_Activos() {
        log.debug("REST request to get all trabajadors");
        return trabajadorRepository.findAll_Activos();
    }

     /** JH
     * GET  /trabajador/tipdoc/:id_tdoc/numdoc/:ndoc : get the "tdoc" Tipo de documento de identidad del trabajador
     *  y "ndoc" Número de documento de identidad del trabajador.
     * @param id_tdoc es el id del tipo de documento de identidad del trabajador
     * @param ndoc el numero de documento de identidad del trabajador
     * @return the ResponseEntity with status 200 (OK) and with body the trabajador, or with status 404 (Not Found)
     */
	@GetMapping("/trabajadors/tipdoc/{id_tdoc}/numdoc/{ndoc}")
    @Timed
    public ResponseEntity<Trabajador> getTrabajadorByIdentDoc(@PathVariable Long id_tdoc, @PathVariable String ndoc) {
        log.debug("REST request to get Trabajador : tipdoc {} - numdoc {}", id_tdoc, ndoc);
        Trabajador trabajador = trabajadorRepository.findTrabajadorByIdentDoc(id_tdoc,ndoc);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trabajador));
    }

     /** JH
     * GET  /trabajadores/tipdoc/:id_tdoc/numdoc/:ndoc : get the "tdoc" Tipo de documento de identidad del trabajador
     *  y "ndoc" Número de documento de identidad del trabajador.
     * @param id_tdoc es el id del tipo de documento de identidad del trabajador
     * @param ndoc el numero de documento de identidad (V_NUMDOCUMENTO), del trabajador
     * @return the ResponseEntity with status 200 (OK) and with body the trabajador, or with status 404 (Not Found)
     */
	@GetMapping("/trabajadors/tipdoc/{id_tdoc}/numdocs/{ndoc}")
    @Timed
    public List<Trabajador> getListTrabajadorByIdentDoc(@PathVariable Long id_tdoc, @PathVariable String ndoc) {
        log.debug("REST request to get Trabajador : tipdoc {} - numdoc {}", id_tdoc, ndoc);
        return trabajadorRepository.findListTrabajadorByIdentDoc(id_tdoc,ndoc);
    }

     /** JH
     * GET  /trabajadores/nombres/:nombres/apellidopat/:apepat/apellidomat/:apemat 
     *  
     * @param nombres es el nombre del trabajador
     * @param apepat el apellido paterno del trabajador
     * @param apemat el apellido materno del trabajador
     * @return the ResponseEntity with status 200 (OK) and with body the trabajador, or with status 404 (Not Found)
     */
    @GetMapping("/trabajadors/nombres/{nombres}/apellidopat/{apepat}/apellidomat/{apemat}")
    @Timed
    public List<Trabajador> getTrabajadoresbyName(@PathVariable String nombres, @PathVariable String apepat, @PathVariable String apemat) {
        log.debug("REST request to get Trabajador : nombres {} - apellidopat {} - apellidomat {}", nombres, apepat, apemat);
        return trabajadorRepository.findListTrabajadorByName(nombres,apepat,apemat);
         
    }

    /**
     * DELETE  /trabajadors/:id : delete the "id" trabajador.
     *
     * @param id the id of the trabajador to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/trabajadors/{id}")
    @Timed
    public ResponseEntity<Void> deleteTrabajador(@PathVariable Long id) {
        log.debug("REST request to delete Trabajador : {}", id);
        trabajadorRepository.delete(id);
        trabajadorSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/trabajadors?query=:query : search for the trabajador corresponding
     * to the query.
     *
     * @param query the query of the trabajador search
     * @return the result of the search
     */
    @GetMapping("/_search/trabajadors")
    @Timed
    public List<Trabajador> searchTrabajadors(@RequestParam String query) {
        log.debug("REST request to search Trabajadors for query {}", query);
        return StreamSupport
            .stream(trabajadorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
