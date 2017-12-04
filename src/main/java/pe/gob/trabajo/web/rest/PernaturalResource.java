package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Pernatural;

import pe.gob.trabajo.repository.PernaturalRepository;
import pe.gob.trabajo.repository.search.PernaturalSearchRepository;
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
 * REST controller for managing Pernatural.
 */
@RestController
@RequestMapping("/api")
public class PernaturalResource {

    private final Logger log = LoggerFactory.getLogger(PernaturalResource.class);

    private static final String ENTITY_NAME = "pernatural";

    private final PernaturalRepository pernaturalRepository;

    private final PernaturalSearchRepository pernaturalSearchRepository;

    public PernaturalResource(PernaturalRepository pernaturalRepository, PernaturalSearchRepository pernaturalSearchRepository) {
        this.pernaturalRepository = pernaturalRepository;
        this.pernaturalSearchRepository = pernaturalSearchRepository;
    }

    /**
     * POST  /pernaturals : Create a new pernatural.
     *
     * @param pernatural the pernatural to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pernatural, or with status 400 (Bad Request) if the pernatural has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pernaturals")
    @Timed
    public ResponseEntity<Pernatural> createPernatural(@Valid @RequestBody Pernatural pernatural) throws URISyntaxException {
        log.debug("REST request to save Pernatural : {}", pernatural);
        if (pernatural.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pernatural cannot already have an ID")).body(null);
        }
        Pernatural result = pernaturalRepository.save(pernatural);
        pernaturalSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pernaturals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pernaturals : Updates an existing pernatural.
     *
     * @param pernatural the pernatural to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pernatural,
     * or with status 400 (Bad Request) if the pernatural is not valid,
     * or with status 500 (Internal Server Error) if the pernatural couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pernaturals")
    @Timed
    public ResponseEntity<Pernatural> updatePernatural(@Valid @RequestBody Pernatural pernatural) throws URISyntaxException {
        log.debug("REST request to update Pernatural : {}", pernatural);
        if (pernatural.getId() == null) {
            return createPernatural(pernatural);
        }
        Pernatural result = pernaturalRepository.save(pernatural);
        pernaturalSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pernatural.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pernaturals : get all the pernaturals.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pernaturals in body
     */
    @GetMapping("/pernaturals")
    @Timed
    public List<Pernatural> getAllPernaturals() {
        log.debug("REST request to get all Pernaturals");
        return pernaturalRepository.findAll();
        }

    /**
     * GET  /pernaturals/:id : get the "id" pernatural.
     *
     * @param id the id of the pernatural to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pernatural, or with status 404 (Not Found)
     */
    @GetMapping("/pernaturals/{id}")
    @Timed
    public ResponseEntity<Pernatural> getPernatural(@PathVariable Long id) {
        log.debug("REST request to get Pernatural : {}", id);
        Pernatural pernatural = pernaturalRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pernatural));
    }

    /** JH
     * GET  /pernaturals : get all the pernaturals.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pernaturals in body
     */
    @GetMapping("/pernaturals/activos")
    @Timed
    public List<Pernatural> getAll_Activos() {
        log.debug("REST request to get all pernaturals");
        return pernaturalRepository.findAll_Activos();
    }

     /** JH
     * GET  /pernatural/tipdoc/:id_tdoc/numdoc/:ndoc : get the "id_tdoc" Tipo de documento de identidad de la persona natural
     *  y "ndoc" Número de documento de identidad de la persona natural.
     * @param id_tdoc es el id del tipo de documento de identidad de la persona natural
     * @param ndoc el numero de documento de identidad de la persona natural
     * @return the ResponseEntity with status 200 (OK) and with body the pernatural, or with status 404 (Not Found)
     */
	@GetMapping("/pernaturals/tipdoc/{id_tdoc}/numdoc/{ndoc}")
    @Timed
    public ResponseEntity<Pernatural> getPersonanaturByIdentDoc(@PathVariable Long id_tdoc, @PathVariable String ndoc) {
        log.debug("REST request to get Pernatural : tipdoc {} - numdoc {}", id_tdoc, ndoc);
        Pernatural pernatural = pernaturalRepository.findpersonanaturByIdentDoc(id_tdoc,ndoc);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pernatural));
    }

     /** JH
     * GET  /pernaturals/id/:id_per/nomcompleto
     * @param id_per es id de la persona Natural
     * @return the ResponseEntity with status 200 (OK) and with body the pernatural, or with status 404 (Not Found)
     */
	@GetMapping("/pernaturals/id/{id_per}/nomcompleto")
    @Timed
    public String getNomCompleto_PernaturalById(@PathVariable Long id_per) {
        log.debug("REST request to get Nombre Completo : id_per {}", id_per);
        String nomcompleto = pernaturalRepository.findNomCompleto_PernaturalById(id_per);
        return nomcompleto;
    }

    /**
     * DELETE  /pernaturals/:id : delete the "id" pernatural.
     *
     * @param id the id of the pernatural to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pernaturals/{id}")
    @Timed
    public ResponseEntity<Void> deletePernatural(@PathVariable Long id) {
        log.debug("REST request to delete Pernatural : {}", id);
        pernaturalRepository.delete(id);
        pernaturalSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pernaturals?query=:query : search for the pernatural corresponding
     * to the query.
     *
     * @param query the query of the pernatural search
     * @return the result of the search
     */
    @GetMapping("/_search/pernaturals")
    @Timed
    public List<Pernatural> searchPernaturals(@RequestParam String query) {
        log.debug("REST request to search Pernaturals for query {}", query);
        return StreamSupport
            .stream(pernaturalSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
