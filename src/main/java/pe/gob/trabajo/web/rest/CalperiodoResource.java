package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Calperiodo;

import pe.gob.trabajo.repository.CalperiodoRepository;
import pe.gob.trabajo.repository.search.CalperiodoSearchRepository;
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
 * REST controller for managing Calperiodo.
 */
@RestController
@RequestMapping("/api")
public class CalperiodoResource {

    private final Logger log = LoggerFactory.getLogger(CalperiodoResource.class);

    private static final String ENTITY_NAME = "calperiodo";

    private final CalperiodoRepository calperiodoRepository;

    private final CalperiodoSearchRepository calperiodoSearchRepository;

    public CalperiodoResource(CalperiodoRepository calperiodoRepository, CalperiodoSearchRepository calperiodoSearchRepository) {
        this.calperiodoRepository = calperiodoRepository;
        this.calperiodoSearchRepository = calperiodoSearchRepository;
    }

    /**
     * POST  /calperiodos : Create a new calperiodo.
     *
     * @param calperiodo the calperiodo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new calperiodo, or with status 400 (Bad Request) if the calperiodo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/calperiodos")
    @Timed
    public ResponseEntity<Calperiodo> createCalperiodo(@Valid @RequestBody Calperiodo calperiodo) throws URISyntaxException {
        log.debug("REST request to save Calperiodo : {}", calperiodo);
        if (calperiodo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new calperiodo cannot already have an ID")).body(null);
        }
        Calperiodo result = calperiodoRepository.save(calperiodo);
        calperiodoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/calperiodos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /calperiodos : Updates an existing calperiodo.
     *
     * @param calperiodo the calperiodo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated calperiodo,
     * or with status 400 (Bad Request) if the calperiodo is not valid,
     * or with status 500 (Internal Server Error) if the calperiodo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/calperiodos")
    @Timed
    public ResponseEntity<Calperiodo> updateCalperiodo(@Valid @RequestBody Calperiodo calperiodo) throws URISyntaxException {
        log.debug("REST request to update Calperiodo : {}", calperiodo);
        if (calperiodo.getId() == null) {
            return createCalperiodo(calperiodo);
        }
        Calperiodo result = calperiodoRepository.save(calperiodo);
        calperiodoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, calperiodo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /calperiodos : get all the calperiodos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of calperiodos in body
     */
    @GetMapping("/calperiodos")
    @Timed
    public List<Calperiodo> getAllCalperiodos() {
        log.debug("REST request to get all Calperiodos");
        return calperiodoRepository.findAll();
        }

    /**
     * GET  /calperiodos/:id : get the "id" calperiodo.
     *
     * @param id the id of the calperiodo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the calperiodo, or with status 404 (Not Found)
     */
    @GetMapping("/calperiodos/{id}")
    @Timed
    public ResponseEntity<Calperiodo> getCalperiodo(@PathVariable Long id) {
        log.debug("REST request to get Calperiodo : {}", id);
        Calperiodo calperiodo = calperiodoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(calperiodo));
    }

    /** JH
     * GET  /calperiodos/activos : get all the calperiodos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of calperiodos in body
     */
    @GetMapping("/calperiodos/activos")
    @Timed
    public List<Calperiodo> getAll_Activos() {
        log.debug("REST request to get all calperiodos");
        return calperiodoRepository.findAll_Activos();
    }

    /** JH
     * GET  /calperiodos/id_atencion/:id_aten/id_bensocial/:id_bsoc : get all the calperiodos.
     * @param id_aten es el id de la Atencion
     * @param id_bsoc es el id del Bensocial
     * @return the ResponseEntity with status 200 (OK) and the list of calperiodos in body
     */
    @GetMapping("/calperiodos/id_atencion/{id_aten}/id_bensocial/{id_bsoc}")
    @Timed
    public List<Calperiodo> getListCalperiodo_By_IdAtencion_IdBiensocial(@PathVariable Long id_aten, @PathVariable Long id_bsoc) {
        log.debug("REST request to get all calperiodos: id_aten {}, id_bsoc {}",id_aten,id_bsoc);
        return calperiodoRepository.findListCalperiodo_By_IdAtencion_IdBiensocial(id_aten, id_bsoc);
    }

    /** JH
     * GET  /calperiodos/depositos/intereses/id_atencion/:id_aten/id_bensocial/:id_bsoc .
     * @param id_aten es el id de la Atencion
     * @param id_bsoc es el id del Bensocial
     * @return the ResponseEntity with status 200 (OK) and the list of calperiodos in body
     */
    @GetMapping("/calperiodos/depositos/intereses/id_atencion/{id_aten}/id_bensocial/{id_bsoc}")
    @Timed
    public List<Calperiodo> getTotalDeposito_Interes_By_IdAtencion_IdBiensocial(@PathVariable Long id_aten, @PathVariable Long id_bsoc) {
        log.debug("REST request to get all calperiodos: id_aten {}, id_bsoc {}",id_aten,id_bsoc);
        return calperiodoRepository.findTotalDeposito_Interes_By_IdAtencion_IdBiensocial(id_aten, id_bsoc);
    }

    /** JH
     * GET  /calperiodos/RCM_Deposito_Interes/id_calperiodo/:id_calper : get gratificaciones delperiodo.
     * @param id_calper es el id del Calperiodo
     * @return the ResponseEntity with status 200 (OK) and the list of calperiodos in body
     */
    @GetMapping("/calperiodos/RCM_Deposito_Interes/id_calperiodo/{id_calper}")
    @Timed
    public List<Calperiodo> get_RCM_Deposito_Interes_ByIdCalperiodo(@PathVariable Long id_calper) {
        log.debug("REST request to get all calperiodos: id_calper {}",id_calper);
        return calperiodoRepository.find_RCM_Deposito_Interes_ByIdCalperiodo(id_calper);
    }

    /** JH
     * GET  /calperiodos/gratiperiodo/id_calper/:id_calper : get gratificaciones delperiodo.
     * @param id_calper es el id del Calperiodo
     * @return the ResponseEntity with status 200 (OK) and the list of calperiodos in body
     */
    @GetMapping("/calperiodos/gratiboniperiodo/id_calperiodo/{id_calper}")
    @Timed
    public List<Calperiodo> get_Grati_Bonifi_ByIdCalperiodo(@PathVariable Long id_calper) {
        log.debug("REST request to get all calperiodos: id_calper {}",id_calper);
        return calperiodoRepository.find_Grati_Bonifi_ByIdCalperiodo(id_calper);
    }

    /**
     * DELETE  /calperiodos/:id : delete the "id" calperiodo.
     *
     * @param id the id of the calperiodo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/calperiodos/{id}")
    @Timed
    public ResponseEntity<Void> deleteCalperiodo(@PathVariable Long id) {
        log.debug("REST request to delete Calperiodo : {}", id);
        calperiodoRepository.delete(id);
        calperiodoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/calperiodos?query=:query : search for the calperiodo corresponding
     * to the query.
     *
     * @param query the query of the calperiodo search
     * @return the result of the search
     */
    @GetMapping("/_search/calperiodos")
    @Timed
    public List<Calperiodo> searchCalperiodos(@RequestParam String query) {
        log.debug("REST request to search Calperiodos for query {}", query);
        return StreamSupport
            .stream(calperiodoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
