package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Perjuridica;

import pe.gob.trabajo.repository.PerjuridicaRepository;
import pe.gob.trabajo.repository.search.PerjuridicaSearchRepository;
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
 * REST controller for managing Perjuridica.
 */
@RestController
@RequestMapping("/api")
public class PerjuridicaResource {

    private final Logger log = LoggerFactory.getLogger(PerjuridicaResource.class);

    private static final String ENTITY_NAME = "perjuridica";

    private final PerjuridicaRepository perjuridicaRepository;

    private final PerjuridicaSearchRepository perjuridicaSearchRepository;

    public PerjuridicaResource(PerjuridicaRepository perjuridicaRepository, PerjuridicaSearchRepository perjuridicaSearchRepository) {
        this.perjuridicaRepository = perjuridicaRepository;
        this.perjuridicaSearchRepository = perjuridicaSearchRepository;
    }

    /**
     * POST  /perjuridicas : Create a new perjuridica.
     *
     * @param perjuridica the perjuridica to create
     * @return the ResponseEntity with status 201 (Created) and with body the new perjuridica, or with status 400 (Bad Request) if the perjuridica has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/perjuridicas")
    @Timed
    public ResponseEntity<Perjuridica> createPerjuridica(@Valid @RequestBody Perjuridica perjuridica) throws URISyntaxException {
        log.debug("REST request to save Perjuridica : {}", perjuridica);
        if (perjuridica.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new perjuridica cannot already have an ID")).body(null);
        }
        Perjuridica result = perjuridicaRepository.save(perjuridica);
        perjuridicaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/perjuridicas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /perjuridicas : Updates an existing perjuridica.
     *
     * @param perjuridica the perjuridica to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated perjuridica,
     * or with status 400 (Bad Request) if the perjuridica is not valid,
     * or with status 500 (Internal Server Error) if the perjuridica couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/perjuridicas")
    @Timed
    public ResponseEntity<Perjuridica> updatePerjuridica(@Valid @RequestBody Perjuridica perjuridica) throws URISyntaxException {
        log.debug("REST request to update Perjuridica : {}", perjuridica);
        if (perjuridica.getId() == null) {
            return createPerjuridica(perjuridica);
        }
        Perjuridica result = perjuridicaRepository.save(perjuridica);
        perjuridicaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, perjuridica.getId().toString()))
            .body(result);
    }

    /**
     * GET  /perjuridicas : get all the perjuridicas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of perjuridicas in body
     */
    @GetMapping("/perjuridicas")
    @Timed
    public List<Perjuridica> getAllPerjuridicas() {
        log.debug("REST request to get all Perjuridicas");
        return perjuridicaRepository.findAll();
        }

    /**
     * GET  /perjuridicas/:id : get the "id" perjuridica.
     *
     * @param id the id of the perjuridica to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the perjuridica, or with status 404 (Not Found)
     */
    @GetMapping("/perjuridicas/{id}")
    @Timed
    public ResponseEntity<Perjuridica> getPerjuridica(@PathVariable Long id) {
        log.debug("REST request to get Perjuridica : {}", id);
        Perjuridica perjuridica = perjuridicaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(perjuridica));
    }

    /** JH
     * GET  /perjuridicas : get all the perjuridicas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of perjuridicas in body
     */
    @GetMapping("/perjuridicas/activos")
    @Timed
    public List<Perjuridica> getAll_Activos() {
        log.debug("REST request to get all perjuridicas");
        return perjuridicaRepository.findAll_Activos();
    }

     /** JH
     * GET  /perjuridicas/tipdoc/:id_tdoc/numdoc/:ndoc : get the "tdoc" Tipo de documento de identidad de la persona juridica
     *  y "ndoc" Número de documento de identidad de la persona juridica.
     * @param id_tdoc es el id del tipo de documento de identidad de la persona juridica
     * @param ndoc el numero de documento de identidad de la persona juridica
     * @return the ResponseEntity with status 200 (OK) and with body the perjuridica, or with status 404 (Not Found)
     */
	@GetMapping("/perjuridicas/tipdoc/{id_tdoc}/numdoc/{ndoc}")
    @Timed
    public ResponseEntity<Perjuridica> getPersonajuridByIdentDoc(@PathVariable Long id_tdoc, @PathVariable String ndoc) {
        log.debug("REST request to get Perjuridica : tipdoc {} - numdoc {}", id_tdoc, ndoc);
        Perjuridica perjuridica = perjuridicaRepository.findPersonajuridByIdentDoc(id_tdoc,ndoc);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(perjuridica));
    }

    /**
     * DELETE  /perjuridicas/:id : delete the "id" perjuridica.
     *
     * @param id the id of the perjuridica to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/perjuridicas/{id}")
    @Timed
    public ResponseEntity<Void> deletePerjuridica(@PathVariable Long id) {
        log.debug("REST request to delete Perjuridica : {}", id);
        perjuridicaRepository.delete(id);
        perjuridicaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/perjuridicas?query=:query : search for the perjuridica corresponding
     * to the query.
     *
     * @param query the query of the perjuridica search
     * @return the result of the search
     */
    @GetMapping("/_search/perjuridicas")
    @Timed
    public List<Perjuridica> searchPerjuridicas(@RequestParam String query) {
        log.debug("REST request to search Perjuridicas for query {}", query);
        return StreamSupport
            .stream(perjuridicaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
