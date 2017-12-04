package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.gob.trabajo.domain.Cartrab;

import pe.gob.trabajo.repository.CartrabRepository;
import pe.gob.trabajo.repository.search.CartrabSearchRepository;
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
 * REST controller for managing Cartrab.
 */
@RestController
@RequestMapping("/api")
public class CartrabResource {

    private final Logger log = LoggerFactory.getLogger(CartrabResource.class);

    private static final String ENTITY_NAME = "cartrab";

    private final CartrabRepository cartrabRepository;

    private final CartrabSearchRepository cartrabSearchRepository;

    public CartrabResource(CartrabRepository cartrabRepository, CartrabSearchRepository cartrabSearchRepository) {
        this.cartrabRepository = cartrabRepository;
        this.cartrabSearchRepository = cartrabSearchRepository;
    }

    /**
     * POST  /cartrabs : Create a new cartrab.
     *
     * @param cartrab the cartrab to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cartrab, or with status 400 (Bad Request) if the cartrab has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cartrabs")
    @Timed
    public ResponseEntity<Cartrab> createCartrab(@Valid @RequestBody Cartrab cartrab) throws URISyntaxException {
        log.debug("REST request to save Cartrab : {}", cartrab);
        if (cartrab.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cartrab cannot already have an ID")).body(null);
        }
        Cartrab result = cartrabRepository.save(cartrab);
        cartrabSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cartrabs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cartrabs : Updates an existing cartrab.
     *
     * @param cartrab the cartrab to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cartrab,
     * or with status 400 (Bad Request) if the cartrab is not valid,
     * or with status 500 (Internal Server Error) if the cartrab couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cartrabs")
    @Timed
    public ResponseEntity<Cartrab> updateCartrab(@Valid @RequestBody Cartrab cartrab) throws URISyntaxException {
        log.debug("REST request to update Cartrab : {}", cartrab);
        if (cartrab.getId() == null) {
            return createCartrab(cartrab);
        }
        Cartrab result = cartrabRepository.save(cartrab);
        cartrabSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cartrab.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cartrabs : get all the cartrabs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cartrabs in body
     */
    @GetMapping("/cartrabs")
    @Timed
    public List<Cartrab> getAllCartrabs() {
        log.debug("REST request to get all Cartrabs");
        return cartrabRepository.findAll();
        }

    /**
     * GET  /cartrabs/:id : get the "id" cartrab.
     *
     * @param id the id of the cartrab to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cartrab, or with status 404 (Not Found)
     */
    @GetMapping("/cartrabs/{id}")
    @Timed
    public ResponseEntity<Cartrab> getCartrab(@PathVariable Long id) {
        log.debug("REST request to get Cartrab : {}", id);
        Cartrab cartrab = cartrabRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cartrab));
    }

    /** JH
     * GET  /cartrabs : get all the cartrabs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cartrabs in body
     */
    @GetMapping("/cartrabs/activos")
    @Timed
    public List<Cartrab> getAll_Activos() {
        log.debug("REST request to get all cartrabs");
        return cartrabRepository.findAll_Activos();
        }

    /**
     * DELETE  /cartrabs/:id : delete the "id" cartrab.
     *
     * @param id the id of the cartrab to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cartrabs/{id}")
    @Timed
    public ResponseEntity<Void> deleteCartrab(@PathVariable Long id) {
        log.debug("REST request to delete Cartrab : {}", id);
        cartrabRepository.delete(id);
        cartrabSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cartrabs?query=:query : search for the cartrab corresponding
     * to the query.
     *
     * @param query the query of the cartrab search
     * @return the result of the search
     */
    @GetMapping("/_search/cartrabs")
    @Timed
    public List<Cartrab> searchCartrabs(@RequestParam String query) {
        log.debug("REST request to search Cartrabs for query {}", query);
        return StreamSupport
            .stream(cartrabSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
