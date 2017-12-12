package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Bancos;

import pe.gob.trabajo.repository.BancosRepository;
import pe.gob.trabajo.repository.search.BancosSearchRepository;
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
 * REST controller for managing Bancos.
 */
@RestController
@RequestMapping("/api")
public class BancosResource {

    private final Logger log = LoggerFactory.getLogger(BancosResource.class);

    private static final String ENTITY_NAME = "bancos";

    private final BancosRepository bancosRepository;

    private final BancosSearchRepository bancosSearchRepository;

    public BancosResource(BancosRepository bancosRepository, BancosSearchRepository bancosSearchRepository) {
        this.bancosRepository = bancosRepository;
        this.bancosSearchRepository = bancosSearchRepository;
    }

    /**
     * POST  /bancos : Create a new bancos.
     *
     * @param bancos the bancos to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bancos, or with status 400 (Bad Request) if the bancos has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bancos")
    @Timed
    public ResponseEntity<Bancos> createBancos(@Valid @RequestBody Bancos bancos) throws URISyntaxException {
        log.debug("REST request to save Bancos : {}", bancos);
        if (bancos.getId() != null) {
            throw new BadRequestAlertException("A new bancos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bancos result = bancosRepository.save(bancos);
        bancosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bancos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bancos : Updates an existing bancos.
     *
     * @param bancos the bancos to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bancos,
     * or with status 400 (Bad Request) if the bancos is not valid,
     * or with status 500 (Internal Server Error) if the bancos couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bancos")
    @Timed
    public ResponseEntity<Bancos> updateBancos(@Valid @RequestBody Bancos bancos) throws URISyntaxException {
        log.debug("REST request to update Bancos : {}", bancos);
        if (bancos.getId() == null) {
            return createBancos(bancos);
        }
        Bancos result = bancosRepository.save(bancos);
        bancosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bancos.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bancos : get all the bancos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bancos in body
     */
    @GetMapping("/bancos")
    @Timed
    public List<Bancos> getAllBancos() {
        log.debug("REST request to get all Bancos");
        return bancosRepository.findAll();
        }

    /**
     * GET  /bancos/:id : get the "id" bancos.
     *
     * @param id the id of the bancos to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bancos, or with status 404 (Not Found)
     */
    @GetMapping("/bancos/{id}")
    @Timed
    public ResponseEntity<Bancos> getBancos(@PathVariable Long id) {
        log.debug("REST request to get Bancos : {}", id);
        Bancos bancos = bancosRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bancos));
    }

    /** JH
     * GET  /bancos/activos : get all the bancos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bancos in body
     */
    @GetMapping("/bancos/activos")
    @Timed
    public List<Bancos> getAll_Activos() {
        log.debug("REST request to get all bancos");
        return bancosRepository.findAll_Activos();
        }

    /**
     * DELETE  /bancos/:id : delete the "id" bancos.
     *
     * @param id the id of the bancos to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bancos/{id}")
    @Timed
    public ResponseEntity<Void> deleteBancos(@PathVariable Long id) {
        log.debug("REST request to delete Bancos : {}", id);
        bancosRepository.delete(id);
        bancosSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bancos?query=:query : search for the bancos corresponding
     * to the query.
     *
     * @param query the query of the bancos search
     * @return the result of the search
     */
    @GetMapping("/_search/bancos")
    @Timed
    public List<Bancos> searchBancos(@RequestParam String query) {
        log.debug("REST request to search Bancos for query {}", query);
        return StreamSupport
            .stream(bancosSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
