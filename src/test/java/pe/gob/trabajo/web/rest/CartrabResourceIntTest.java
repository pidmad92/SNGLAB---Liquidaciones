package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Cartrab;
import pe.gob.trabajo.repository.CartrabRepository;
import pe.gob.trabajo.repository.search.CartrabSearchRepository;
import pe.gob.trabajo.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CartrabResource REST controller.
 *
 * @see CartrabResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class CartrabResourceIntTest {

    private static final String DEFAULT_V_DESCARTRA = "AAAAAAAAAA";
    private static final String UPDATED_V_DESCARTRA = "BBBBBBBBBB";

    private static final Integer DEFAULT_N_USUAREG = 1;
    private static final Integer UPDATED_N_USUAREG = 2;

    private static final Instant DEFAULT_T_FECREG = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_T_FECREG = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_N_FLGACTIVO = false;
    private static final Boolean UPDATED_N_FLGACTIVO = true;

    private static final Integer DEFAULT_N_SEDEREG = 1;
    private static final Integer UPDATED_N_SEDEREG = 2;

    private static final Integer DEFAULT_N_USUAUPD = 1;
    private static final Integer UPDATED_N_USUAUPD = 2;

    private static final Instant DEFAULT_T_FECUPD = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_T_FECUPD = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_N_SEDEUPD = 1;
    private static final Integer UPDATED_N_SEDEUPD = 2;

    @Autowired
    private CartrabRepository cartrabRepository;

    @Autowired
    private CartrabSearchRepository cartrabSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCartrabMockMvc;

    private Cartrab cartrab;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CartrabResource cartrabResource = new CartrabResource(cartrabRepository, cartrabSearchRepository);
        this.restCartrabMockMvc = MockMvcBuilders.standaloneSetup(cartrabResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cartrab createEntity(EntityManager em) {
        Cartrab cartrab = new Cartrab()
            .vDescartra(DEFAULT_V_DESCARTRA)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return cartrab;
    }

    @Before
    public void initTest() {
        cartrabSearchRepository.deleteAll();
        cartrab = createEntity(em);
    }

    @Test
    @Transactional
    public void createCartrab() throws Exception {
        int databaseSizeBeforeCreate = cartrabRepository.findAll().size();

        // Create the Cartrab
        restCartrabMockMvc.perform(post("/api/cartrabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartrab)))
            .andExpect(status().isCreated());

        // Validate the Cartrab in the database
        List<Cartrab> cartrabList = cartrabRepository.findAll();
        assertThat(cartrabList).hasSize(databaseSizeBeforeCreate + 1);
        Cartrab testCartrab = cartrabList.get(cartrabList.size() - 1);
        assertThat(testCartrab.getvDescartra()).isEqualTo(DEFAULT_V_DESCARTRA);
        assertThat(testCartrab.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testCartrab.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testCartrab.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testCartrab.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testCartrab.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testCartrab.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testCartrab.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Cartrab in Elasticsearch
        Cartrab cartrabEs = cartrabSearchRepository.findOne(testCartrab.getId());
        assertThat(cartrabEs).isEqualToComparingFieldByField(testCartrab);
    }

    @Test
    @Transactional
    public void createCartrabWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cartrabRepository.findAll().size();

        // Create the Cartrab with an existing ID
        cartrab.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartrabMockMvc.perform(post("/api/cartrabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartrab)))
            .andExpect(status().isBadRequest());

        // Validate the Cartrab in the database
        List<Cartrab> cartrabList = cartrabRepository.findAll();
        assertThat(cartrabList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDescartraIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartrabRepository.findAll().size();
        // set the field null
        cartrab.setvDescartra(null);

        // Create the Cartrab, which fails.

        restCartrabMockMvc.perform(post("/api/cartrabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartrab)))
            .andExpect(status().isBadRequest());

        List<Cartrab> cartrabList = cartrabRepository.findAll();
        assertThat(cartrabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartrabRepository.findAll().size();
        // set the field null
        cartrab.setnUsuareg(null);

        // Create the Cartrab, which fails.

        restCartrabMockMvc.perform(post("/api/cartrabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartrab)))
            .andExpect(status().isBadRequest());

        List<Cartrab> cartrabList = cartrabRepository.findAll();
        assertThat(cartrabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartrabRepository.findAll().size();
        // set the field null
        cartrab.settFecreg(null);

        // Create the Cartrab, which fails.

        restCartrabMockMvc.perform(post("/api/cartrabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartrab)))
            .andExpect(status().isBadRequest());

        List<Cartrab> cartrabList = cartrabRepository.findAll();
        assertThat(cartrabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartrabRepository.findAll().size();
        // set the field null
        cartrab.setnFlgactivo(null);

        // Create the Cartrab, which fails.

        restCartrabMockMvc.perform(post("/api/cartrabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartrab)))
            .andExpect(status().isBadRequest());

        List<Cartrab> cartrabList = cartrabRepository.findAll();
        assertThat(cartrabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartrabRepository.findAll().size();
        // set the field null
        cartrab.setnSedereg(null);

        // Create the Cartrab, which fails.

        restCartrabMockMvc.perform(post("/api/cartrabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartrab)))
            .andExpect(status().isBadRequest());

        List<Cartrab> cartrabList = cartrabRepository.findAll();
        assertThat(cartrabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCartrabs() throws Exception {
        // Initialize the database
        cartrabRepository.saveAndFlush(cartrab);

        // Get all the cartrabList
        restCartrabMockMvc.perform(get("/api/cartrabs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartrab.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDescartra").value(hasItem(DEFAULT_V_DESCARTRA.toString())))
            .andExpect(jsonPath("$.[*].nUsuareg").value(hasItem(DEFAULT_N_USUAREG)))
            .andExpect(jsonPath("$.[*].tFecreg").value(hasItem(DEFAULT_T_FECREG.toString())))
            .andExpect(jsonPath("$.[*].nFlgactivo").value(hasItem(DEFAULT_N_FLGACTIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].nSedereg").value(hasItem(DEFAULT_N_SEDEREG)))
            .andExpect(jsonPath("$.[*].nUsuaupd").value(hasItem(DEFAULT_N_USUAUPD)))
            .andExpect(jsonPath("$.[*].tFecupd").value(hasItem(DEFAULT_T_FECUPD.toString())))
            .andExpect(jsonPath("$.[*].nSedeupd").value(hasItem(DEFAULT_N_SEDEUPD)));
    }

    @Test
    @Transactional
    public void getCartrab() throws Exception {
        // Initialize the database
        cartrabRepository.saveAndFlush(cartrab);

        // Get the cartrab
        restCartrabMockMvc.perform(get("/api/cartrabs/{id}", cartrab.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cartrab.getId().intValue()))
            .andExpect(jsonPath("$.vDescartra").value(DEFAULT_V_DESCARTRA.toString()))
            .andExpect(jsonPath("$.nUsuareg").value(DEFAULT_N_USUAREG))
            .andExpect(jsonPath("$.tFecreg").value(DEFAULT_T_FECREG.toString()))
            .andExpect(jsonPath("$.nFlgactivo").value(DEFAULT_N_FLGACTIVO.booleanValue()))
            .andExpect(jsonPath("$.nSedereg").value(DEFAULT_N_SEDEREG))
            .andExpect(jsonPath("$.nUsuaupd").value(DEFAULT_N_USUAUPD))
            .andExpect(jsonPath("$.tFecupd").value(DEFAULT_T_FECUPD.toString()))
            .andExpect(jsonPath("$.nSedeupd").value(DEFAULT_N_SEDEUPD));
    }

    @Test
    @Transactional
    public void getNonExistingCartrab() throws Exception {
        // Get the cartrab
        restCartrabMockMvc.perform(get("/api/cartrabs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCartrab() throws Exception {
        // Initialize the database
        cartrabRepository.saveAndFlush(cartrab);
        cartrabSearchRepository.save(cartrab);
        int databaseSizeBeforeUpdate = cartrabRepository.findAll().size();

        // Update the cartrab
        Cartrab updatedCartrab = cartrabRepository.findOne(cartrab.getId());
        updatedCartrab
            .vDescartra(UPDATED_V_DESCARTRA)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restCartrabMockMvc.perform(put("/api/cartrabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCartrab)))
            .andExpect(status().isOk());

        // Validate the Cartrab in the database
        List<Cartrab> cartrabList = cartrabRepository.findAll();
        assertThat(cartrabList).hasSize(databaseSizeBeforeUpdate);
        Cartrab testCartrab = cartrabList.get(cartrabList.size() - 1);
        assertThat(testCartrab.getvDescartra()).isEqualTo(UPDATED_V_DESCARTRA);
        assertThat(testCartrab.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testCartrab.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testCartrab.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testCartrab.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testCartrab.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testCartrab.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testCartrab.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Cartrab in Elasticsearch
        Cartrab cartrabEs = cartrabSearchRepository.findOne(testCartrab.getId());
        assertThat(cartrabEs).isEqualToComparingFieldByField(testCartrab);
    }

    @Test
    @Transactional
    public void updateNonExistingCartrab() throws Exception {
        int databaseSizeBeforeUpdate = cartrabRepository.findAll().size();

        // Create the Cartrab

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCartrabMockMvc.perform(put("/api/cartrabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartrab)))
            .andExpect(status().isCreated());

        // Validate the Cartrab in the database
        List<Cartrab> cartrabList = cartrabRepository.findAll();
        assertThat(cartrabList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCartrab() throws Exception {
        // Initialize the database
        cartrabRepository.saveAndFlush(cartrab);
        cartrabSearchRepository.save(cartrab);
        int databaseSizeBeforeDelete = cartrabRepository.findAll().size();

        // Get the cartrab
        restCartrabMockMvc.perform(delete("/api/cartrabs/{id}", cartrab.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cartrabExistsInEs = cartrabSearchRepository.exists(cartrab.getId());
        assertThat(cartrabExistsInEs).isFalse();

        // Validate the database is empty
        List<Cartrab> cartrabList = cartrabRepository.findAll();
        assertThat(cartrabList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCartrab() throws Exception {
        // Initialize the database
        cartrabRepository.saveAndFlush(cartrab);
        cartrabSearchRepository.save(cartrab);

        // Search the cartrab
        restCartrabMockMvc.perform(get("/api/_search/cartrabs?query=id:" + cartrab.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartrab.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDescartra").value(hasItem(DEFAULT_V_DESCARTRA.toString())))
            .andExpect(jsonPath("$.[*].nUsuareg").value(hasItem(DEFAULT_N_USUAREG)))
            .andExpect(jsonPath("$.[*].tFecreg").value(hasItem(DEFAULT_T_FECREG.toString())))
            .andExpect(jsonPath("$.[*].nFlgactivo").value(hasItem(DEFAULT_N_FLGACTIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].nSedereg").value(hasItem(DEFAULT_N_SEDEREG)))
            .andExpect(jsonPath("$.[*].nUsuaupd").value(hasItem(DEFAULT_N_USUAUPD)))
            .andExpect(jsonPath("$.[*].tFecupd").value(hasItem(DEFAULT_T_FECUPD.toString())))
            .andExpect(jsonPath("$.[*].nSedeupd").value(hasItem(DEFAULT_N_SEDEUPD)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cartrab.class);
        Cartrab cartrab1 = new Cartrab();
        cartrab1.setId(1L);
        Cartrab cartrab2 = new Cartrab();
        cartrab2.setId(cartrab1.getId());
        assertThat(cartrab1).isEqualTo(cartrab2);
        cartrab2.setId(2L);
        assertThat(cartrab1).isNotEqualTo(cartrab2);
        cartrab1.setId(null);
        assertThat(cartrab1).isNotEqualTo(cartrab2);
    }
}
