package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Conceprem;

import pe.gob.trabajo.repository.ConcepremRepository;
import pe.gob.trabajo.repository.search.ConcepremSearchRepository;
import pe.gob.trabajo.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing Conceprem.
 */
@RestController
@RequestMapping("/api")
public class ConcepremResource {

    private final Logger log = LoggerFactory.getLogger(ConcepremResource.class);

    private static final String ENTITY_NAME = "conceprem";

    private final ConcepremRepository concepremRepository;

    private final ConcepremSearchRepository concepremSearchRepository;

    public ConcepremResource(ConcepremRepository concepremRepository, ConcepremSearchRepository concepremSearchRepository) {
        this.concepremRepository = concepremRepository;
        this.concepremSearchRepository = concepremSearchRepository;
    }

    /**
     * POST  /conceprems : Create a new conceprem.
     *
     * @param conceprem the conceprem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new conceprem, or with status 400 (Bad Request) if the conceprem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/conceprems")
    @Timed
    public ResponseEntity<Conceprem> createConceprem(@Valid @RequestBody Conceprem conceprem) throws URISyntaxException {
        log.debug("REST request to save Conceprem : {}", conceprem);
        if (conceprem.getId() != null) {
            // throw new BadRequestAlertException("A new conceprem cannot already have an ID", ENTITY_NAME, "idexists");
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new conceprem cannot already have an ID")).body(null);
        }
        Conceprem result = concepremRepository.save(conceprem);
        concepremSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/conceprems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /conceprems : Updates an existing conceprem.
     *
     * @param conceprem the conceprem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated conceprem,
     * or with status 400 (Bad Request) if the conceprem is not valid,
     * or with status 500 (Internal Server Error) if the conceprem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/conceprems")
    @Timed
    public ResponseEntity<Conceprem> updateConceprem(@Valid @RequestBody Conceprem conceprem) throws URISyntaxException {
        log.debug("REST request to update Conceprem : {}", conceprem);
        if (conceprem.getId() == null) {
            return createConceprem(conceprem);
        }
        Conceprem result = concepremRepository.save(conceprem);
        concepremSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, conceprem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /conceprems : get all the conceprems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of conceprems in body
     */
    @GetMapping("/conceprems")
    @Timed
    public List<Conceprem> getAllConceprems() {
        log.debug("REST request to get all Conceprems");
        return concepremRepository.findAll();
        }

    /**
     * GET  /conceprems/:id : get the "id" conceprem.
     *
     * @param id the id of the conceprem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the conceprem, or with status 404 (Not Found)
     */
    @GetMapping("/conceprems/{id}")
    @Timed
    public ResponseEntity<Conceprem> getConceprem(@PathVariable Long id) {
        log.debug("REST request to get Conceprem : {}", id);
        Conceprem conceprem = concepremRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(conceprem));
    }

    /** JH
     * GET  /conceprems/activos : get all the conceprems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of conceprems in body
     */
    @GetMapping("/conceprems/activos")
    @Timed
    public List<Conceprem> getAll_Activos() {
        log.debug("REST request to get all conceprems");
        return concepremRepository.findAll_Activos();
        }

    /** JH
     * GET  /conceprems/id_atencion/{id_aten}/id_bensocial/{id_bsoc} : get all the conceprems.
     * @param id_aten es el id de la Atencion
     * @param id_bsoc es el id del Bensocial
     * @return the ResponseEntity with status 200 (OK) and the list of conceprems in body
     */
    @GetMapping("/conceprems/id_calperiodo/{id_calper}")
    @Timed
    public List<Conceprem> getListCalperiodo_By_IdAtencion_IdBiensocial(@PathVariable Long id_calper) {
        log.debug("REST request to get all conceprems: id_calper {}",id_calper);
        return concepremRepository.findListConceprem_ByIdCalperiodo(id_calper);
    }

    /** JH
     * GET /conceprems/id_conceprem/:id_concep/id_tipcalconre/:id_tipccr : get all the conceprems.
     * @param id_concep es el id del Concepto Remunerativo
     * @param id_tipccr es el id del Tipo de Calculo de Concepto Remunerativo
     * @return the ResponseEntity with status 200 (OK) and the list of conceprems in body
     */
    @GetMapping("/conceprems/id_conceprem/{id_concep}/id_tipcalconre/{id_tipccr}")
    @Timed
    public List<Conceprem> getListConcepremHijo_ByIdPadreIdTipo(@PathVariable Long id_concep, @PathVariable Long id_tipccr) {
        log.debug("REST request to get all conceprems: id_concep {}, id_tipcalconre {}",id_concep,id_tipccr);
        return concepremRepository.findListConcepremHijo_ByIdPadreIdTipo(id_concep, id_tipccr);
    }

    /** JH
     * GET /conceprems/id_atencion/:id_aten/id_bensocial/:id_bensoc : get all the conceprems.
     * @param id_aten es el id de Atencion
     * @param id_bensoc es el id de Bensoc
     * @return the ResponseEntity with status 200 (OK) and the list of conceprems in body
     */
    @GetMapping("/conceprems/id_atencion/{id_aten}/id_bensocial/{id_bensoc}")
    @Timed
    public List<Conceprem> getListConceprem_ByIdAtencionIdBensocial(@PathVariable Long id_aten, @PathVariable Long id_bensoc) {
        log.debug("REST request to get all conceprems: id_aten {}, id_bensoc {}",id_aten,id_bensoc);
        return concepremRepository.findListConceprem_ByIdAtencionIdBensocial(id_aten, id_bensoc);
    }

    /** JH
     * GET /conceprems/id_conceprem/:id_concep : get all the conceprems hijos.
     * @param id_concep es el id del Concepto Remunerativo
     * @return the ResponseEntity with status 200 (OK) and the list of conceprems in body
     */
    @GetMapping("/conceprems/id_conceprem/{id_concep}")
    @Timed
    public List<Conceprem> getListConcepremHijo_ByIdPadre(@PathVariable Long id_concep) {
        log.debug("REST request to get all conceprems: id_concep {}",id_concep);
        return concepremRepository.findListConcepremHijo_ByIdPadre(id_concep);
    }

    /**
     * DELETE  /conceprems/:id : delete the "id" conceprem.
     *
     * @param id the id of the conceprem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/conceprems/{id}")
    @Timed
    public ResponseEntity<Void> deleteConceprem(@PathVariable Long id) {
        log.debug("REST request to delete Conceprem : {}", id);
        concepremRepository.delete(id);
        concepremSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/conceprems?query=:query : search for the conceprem corresponding
     * to the query.
     *
     * @param query the query of the conceprem search
     * @return the result of the search
     */
    @GetMapping("/_search/conceprems")
    @Timed
    public List<Conceprem> searchConceprems(@RequestParam String query) {
        log.debug("REST request to search Conceprems for query {}", query);
        return StreamSupport
            .stream(concepremSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
