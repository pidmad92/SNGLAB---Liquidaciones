package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Motivpase;
import pe.gob.trabajo.repository.MotivpaseRepository;
import pe.gob.trabajo.repository.search.MotivpaseSearchRepository;
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
 * Test class for the MotivpaseResource REST controller.
 *
 * @see MotivpaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class MotivpaseResourceIntTest {

    private static final String DEFAULT_V_OBSMOTPAS = "AAAAAAAAAA";
    private static final String UPDATED_V_OBSMOTPAS = "BBBBBBBBBB";

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
    private MotivpaseRepository motivpaseRepository;

    @Autowired
    private MotivpaseSearchRepository motivpaseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMotivpaseMockMvc;

    private Motivpase motivpase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MotivpaseResource motivpaseResource = new MotivpaseResource(motivpaseRepository, motivpaseSearchRepository);
        this.restMotivpaseMockMvc = MockMvcBuilders.standaloneSetup(motivpaseResource)
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
    public static Motivpase createEntity(EntityManager em) {
        Motivpase motivpase = new Motivpase()
            .vObsmotpas(DEFAULT_V_OBSMOTPAS)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return motivpase;
    }

    @Before
    public void initTest() {
        motivpaseSearchRepository.deleteAll();
        motivpase = createEntity(em);
    }

    @Test
    @Transactional
    public void createMotivpase() throws Exception {
        int databaseSizeBeforeCreate = motivpaseRepository.findAll().size();

        // Create the Motivpase
        restMotivpaseMockMvc.perform(post("/api/motivpases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motivpase)))
            .andExpect(status().isCreated());

        // Validate the Motivpase in the database
        List<Motivpase> motivpaseList = motivpaseRepository.findAll();
        assertThat(motivpaseList).hasSize(databaseSizeBeforeCreate + 1);
        Motivpase testMotivpase = motivpaseList.get(motivpaseList.size() - 1);
        assertThat(testMotivpase.getvObsmotpas()).isEqualTo(DEFAULT_V_OBSMOTPAS);
        assertThat(testMotivpase.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testMotivpase.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testMotivpase.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testMotivpase.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testMotivpase.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testMotivpase.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testMotivpase.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Motivpase in Elasticsearch
        Motivpase motivpaseEs = motivpaseSearchRepository.findOne(testMotivpase.getId());
        assertThat(motivpaseEs).isEqualToComparingFieldByField(testMotivpase);
    }

    @Test
    @Transactional
    public void createMotivpaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = motivpaseRepository.findAll().size();

        // Create the Motivpase with an existing ID
        motivpase.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMotivpaseMockMvc.perform(post("/api/motivpases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motivpase)))
            .andExpect(status().isBadRequest());

        // Validate the Motivpase in the database
        List<Motivpase> motivpaseList = motivpaseRepository.findAll();
        assertThat(motivpaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = motivpaseRepository.findAll().size();
        // set the field null
        motivpase.setnUsuareg(null);

        // Create the Motivpase, which fails.

        restMotivpaseMockMvc.perform(post("/api/motivpases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motivpase)))
            .andExpect(status().isBadRequest());

        List<Motivpase> motivpaseList = motivpaseRepository.findAll();
        assertThat(motivpaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = motivpaseRepository.findAll().size();
        // set the field null
        motivpase.settFecreg(null);

        // Create the Motivpase, which fails.

        restMotivpaseMockMvc.perform(post("/api/motivpases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motivpase)))
            .andExpect(status().isBadRequest());

        List<Motivpase> motivpaseList = motivpaseRepository.findAll();
        assertThat(motivpaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = motivpaseRepository.findAll().size();
        // set the field null
        motivpase.setnFlgactivo(null);

        // Create the Motivpase, which fails.

        restMotivpaseMockMvc.perform(post("/api/motivpases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motivpase)))
            .andExpect(status().isBadRequest());

        List<Motivpase> motivpaseList = motivpaseRepository.findAll();
        assertThat(motivpaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = motivpaseRepository.findAll().size();
        // set the field null
        motivpase.setnSedereg(null);

        // Create the Motivpase, which fails.

        restMotivpaseMockMvc.perform(post("/api/motivpases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motivpase)))
            .andExpect(status().isBadRequest());

        List<Motivpase> motivpaseList = motivpaseRepository.findAll();
        assertThat(motivpaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMotivpases() throws Exception {
        // Initialize the database
        motivpaseRepository.saveAndFlush(motivpase);

        // Get all the motivpaseList
        restMotivpaseMockMvc.perform(get("/api/motivpases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motivpase.getId().intValue())))
            .andExpect(jsonPath("$.[*].vObsmotpas").value(hasItem(DEFAULT_V_OBSMOTPAS.toString())))
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
    public void getMotivpase() throws Exception {
        // Initialize the database
        motivpaseRepository.saveAndFlush(motivpase);

        // Get the motivpase
        restMotivpaseMockMvc.perform(get("/api/motivpases/{id}", motivpase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(motivpase.getId().intValue()))
            .andExpect(jsonPath("$.vObsmotpas").value(DEFAULT_V_OBSMOTPAS.toString()))
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
    public void getNonExistingMotivpase() throws Exception {
        // Get the motivpase
        restMotivpaseMockMvc.perform(get("/api/motivpases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMotivpase() throws Exception {
        // Initialize the database
        motivpaseRepository.saveAndFlush(motivpase);
        motivpaseSearchRepository.save(motivpase);
        int databaseSizeBeforeUpdate = motivpaseRepository.findAll().size();

        // Update the motivpase
        Motivpase updatedMotivpase = motivpaseRepository.findOne(motivpase.getId());
        updatedMotivpase
            .vObsmotpas(UPDATED_V_OBSMOTPAS)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restMotivpaseMockMvc.perform(put("/api/motivpases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMotivpase)))
            .andExpect(status().isOk());

        // Validate the Motivpase in the database
        List<Motivpase> motivpaseList = motivpaseRepository.findAll();
        assertThat(motivpaseList).hasSize(databaseSizeBeforeUpdate);
        Motivpase testMotivpase = motivpaseList.get(motivpaseList.size() - 1);
        assertThat(testMotivpase.getvObsmotpas()).isEqualTo(UPDATED_V_OBSMOTPAS);
        assertThat(testMotivpase.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testMotivpase.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testMotivpase.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testMotivpase.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testMotivpase.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testMotivpase.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testMotivpase.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Motivpase in Elasticsearch
        Motivpase motivpaseEs = motivpaseSearchRepository.findOne(testMotivpase.getId());
        assertThat(motivpaseEs).isEqualToComparingFieldByField(testMotivpase);
    }

    @Test
    @Transactional
    public void updateNonExistingMotivpase() throws Exception {
        int databaseSizeBeforeUpdate = motivpaseRepository.findAll().size();

        // Create the Motivpase

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMotivpaseMockMvc.perform(put("/api/motivpases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motivpase)))
            .andExpect(status().isCreated());

        // Validate the Motivpase in the database
        List<Motivpase> motivpaseList = motivpaseRepository.findAll();
        assertThat(motivpaseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMotivpase() throws Exception {
        // Initialize the database
        motivpaseRepository.saveAndFlush(motivpase);
        motivpaseSearchRepository.save(motivpase);
        int databaseSizeBeforeDelete = motivpaseRepository.findAll().size();

        // Get the motivpase
        restMotivpaseMockMvc.perform(delete("/api/motivpases/{id}", motivpase.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean motivpaseExistsInEs = motivpaseSearchRepository.exists(motivpase.getId());
        assertThat(motivpaseExistsInEs).isFalse();

        // Validate the database is empty
        List<Motivpase> motivpaseList = motivpaseRepository.findAll();
        assertThat(motivpaseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMotivpase() throws Exception {
        // Initialize the database
        motivpaseRepository.saveAndFlush(motivpase);
        motivpaseSearchRepository.save(motivpase);

        // Search the motivpase
        restMotivpaseMockMvc.perform(get("/api/_search/motivpases?query=id:" + motivpase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motivpase.getId().intValue())))
            .andExpect(jsonPath("$.[*].vObsmotpas").value(hasItem(DEFAULT_V_OBSMOTPAS.toString())))
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
        TestUtil.equalsVerifier(Motivpase.class);
        Motivpase motivpase1 = new Motivpase();
        motivpase1.setId(1L);
        Motivpase motivpase2 = new Motivpase();
        motivpase2.setId(motivpase1.getId());
        assertThat(motivpase1).isEqualTo(motivpase2);
        motivpase2.setId(2L);
        assertThat(motivpase1).isNotEqualTo(motivpase2);
        motivpase1.setId(null);
        assertThat(motivpase1).isNotEqualTo(motivpase2);
    }
}
