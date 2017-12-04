package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Docinperdlb;

import pe.gob.trabajo.repository.DocinperdlbRepository;
import pe.gob.trabajo.repository.search.DocinperdlbSearchRepository;
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
 * REST controller for managing Docinperdlb.
 */
@RestController
@RequestMapping("/api")
public class DocinperdlbResource {

    private final Logger log = LoggerFactory.getLogger(DocinperdlbResource.class);

    private static final String ENTITY_NAME = "docinperdlb";

    private final DocinperdlbRepository docinperdlbRepository;

    private final DocinperdlbSearchRepository docinperdlbSearchRepository;

    public DocinperdlbResource(DocinperdlbRepository docinperdlbRepository, DocinperdlbSearchRepository docinperdlbSearchRepository) {
        this.docinperdlbRepository = docinperdlbRepository;
        this.docinperdlbSearchRepository = docinperdlbSearchRepository;
    }

    /**
     * POST  /docinperdlbs : Create a new docinperdlb.
     *
     * @param docinperdlb the docinperdlb to create
     * @return the ResponseEntity with status 201 (Created) and with body the new docinperdlb, or with status 400 (Bad Request) if the docinperdlb has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/docinperdlbs")
    @Timed
    public ResponseEntity<Docinperdlb> createDocinperdlb(@Valid @RequestBody Docinperdlb docinperdlb) throws URISyntaxException {
        log.debug("REST request to save Docinperdlb : {}", docinperdlb);
        if (docinperdlb.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new docinperdlb cannot already have an ID")).body(null);
        }
        Docinperdlb result = docinperdlbRepository.save(docinperdlb);
        docinperdlbSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/docinperdlbs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /docinperdlbs : Updates an existing docinperdlb.
     *
     * @param docinperdlb the docinperdlb to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated docinperdlb,
     * or with status 400 (Bad Request) if the docinperdlb is not valid,
     * or with status 500 (Internal Server Error) if the docinperdlb couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/docinperdlbs")
    @Timed
    public ResponseEntity<Docinperdlb> updateDocinperdlb(@Valid @RequestBody Docinperdlb docinperdlb) throws URISyntaxException {
        log.debug("REST request to update Docinperdlb : {}", docinperdlb);
        if (docinperdlb.getId() == null) {
            return createDocinperdlb(docinperdlb);
        }
        Docinperdlb result = docinperdlbRepository.save(docinperdlb);
        docinperdlbSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, docinperdlb.getId().toString()))
            .body(result);
    }

    /**
     * GET  /docinperdlbs : get all the docinperdlbs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of docinperdlbs in body
     */
    @GetMapping("/docinperdlbs")
    @Timed
    public List<Docinperdlb> getAllDocinperdlbs() {
        log.debug("REST request to get all Docinperdlbs");
        return docinperdlbRepository.findAll();
        }

    /**
     * GET  /docinperdlbs/:id : get the "id" docinperdlb.
     *
     * @param id the id of the docinperdlb to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the docinperdlb, or with status 404 (Not Found)
     */
    @GetMapping("/docinperdlbs/{id}")
    @Timed
    public ResponseEntity<Docinperdlb> getDocinperdlb(@PathVariable Long id) {
        log.debug("REST request to get Docinperdlb : {}", id);
        Docinperdlb docinperdlb = docinperdlbRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(docinperdlb));
    }

    /** JH
     * GET  /docinperdlbs : get all the docinperdlbs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of docinperdlbs in body
     */
    @GetMapping("/docinperdlbs/activos")
    @Timed
    public List<Docinperdlb> getAll_Activos() {
        log.debug("REST request to get all docinperdlbs");
        return docinperdlbRepository.findAll_Activos();
    }

     /** JH
     * GET  /docinperdlbs/atencion/id/:id_aten : Documento de ingresos percibidos registrado en una atencion
     * @param id_aten es el id de la atencion
     * @return the ResponseEntity with status 200 (OK) and with body the Docinperdlb, or with status 404 (Not Found)
     */
	@GetMapping("/docinperdlbs/atencion/id/{id_aten}")
    @Timed
    public List<Docinperdlb> getListDocinperdlbById_Atencion(@PathVariable Long id_aten) {
        log.debug("REST request to get docinperdlbs : id_aten {}", id_aten);
        return docinperdlbRepository.findListDocinperdlbById_Atencion(id_aten);
    }

    /**
     * DELETE  /docinperdlbs/:id : delete the "id" docinperdlb.
     *
     * @param id the id of the docinperdlb to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/docinperdlbs/{id}")
    @Timed
    public ResponseEntity<Void> deleteDocinperdlb(@PathVariable Long id) {
        log.debug("REST request to delete Docinperdlb : {}", id);
        docinperdlbRepository.delete(id);
        docinperdlbSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/docinperdlbs?query=:query : search for the docinperdlb corresponding
     * to the query.
     *
     * @param query the query of the docinperdlb search
     * @return the result of the search
     */
    @GetMapping("/_search/docinperdlbs")
    @Timed
    public List<Docinperdlb> searchDocinperdlbs(@RequestParam String query) {
        log.debug("REST request to search Docinperdlbs for query {}", query);
        return StreamSupport
            .stream(docinperdlbSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
