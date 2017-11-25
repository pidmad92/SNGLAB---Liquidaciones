package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Motate;
import pe.gob.trabajo.repository.MotateRepository;
import pe.gob.trabajo.repository.search.MotateSearchRepository;
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
 * Test class for the MotateResource REST controller.
 *
 * @see MotateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class MotateResourceIntTest {

    private static final String DEFAULT_V_DESMOTATE = "AAAAAAAAAA";
    private static final String UPDATED_V_DESMOTATE = "BBBBBBBBBB";

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
    private MotateRepository motateRepository;

    @Autowired
    private MotateSearchRepository motateSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMotateMockMvc;

    private Motate motate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MotateResource motateResource = new MotateResource(motateRepository, motateSearchRepository);
        this.restMotateMockMvc = MockMvcBuilders.standaloneSetup(motateResource)
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
    public static Motate createEntity(EntityManager em) {
        Motate motate = new Motate()
            .vDesmotate(DEFAULT_V_DESMOTATE)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return motate;
    }

    @Before
    public void initTest() {
        motateSearchRepository.deleteAll();
        motate = createEntity(em);
    }

    @Test
    @Transactional
    public void createMotate() throws Exception {
        int databaseSizeBeforeCreate = motateRepository.findAll().size();

        // Create the Motate
        restMotateMockMvc.perform(post("/api/motates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motate)))
            .andExpect(status().isCreated());

        // Validate the Motate in the database
        List<Motate> motateList = motateRepository.findAll();
        assertThat(motateList).hasSize(databaseSizeBeforeCreate + 1);
        Motate testMotate = motateList.get(motateList.size() - 1);
        assertThat(testMotate.getvDesmotate()).isEqualTo(DEFAULT_V_DESMOTATE);
        assertThat(testMotate.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testMotate.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testMotate.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testMotate.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testMotate.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testMotate.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testMotate.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Motate in Elasticsearch
        Motate motateEs = motateSearchRepository.findOne(testMotate.getId());
        assertThat(motateEs).isEqualToComparingFieldByField(testMotate);
    }

    @Test
    @Transactional
    public void createMotateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = motateRepository.findAll().size();

        // Create the Motate with an existing ID
        motate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMotateMockMvc.perform(post("/api/motates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motate)))
            .andExpect(status().isBadRequest());

        // Validate the Motate in the database
        List<Motate> motateList = motateRepository.findAll();
        assertThat(motateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDesmotateIsRequired() throws Exception {
        int databaseSizeBeforeTest = motateRepository.findAll().size();
        // set the field null
        motate.setvDesmotate(null);

        // Create the Motate, which fails.

        restMotateMockMvc.perform(post("/api/motates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motate)))
            .andExpect(status().isBadRequest());

        List<Motate> motateList = motateRepository.findAll();
        assertThat(motateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = motateRepository.findAll().size();
        // set the field null
        motate.setnUsuareg(null);

        // Create the Motate, which fails.

        restMotateMockMvc.perform(post("/api/motates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motate)))
            .andExpect(status().isBadRequest());

        List<Motate> motateList = motateRepository.findAll();
        assertThat(motateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = motateRepository.findAll().size();
        // set the field null
        motate.settFecreg(null);

        // Create the Motate, which fails.

        restMotateMockMvc.perform(post("/api/motates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motate)))
            .andExpect(status().isBadRequest());

        List<Motate> motateList = motateRepository.findAll();
        assertThat(motateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = motateRepository.findAll().size();
        // set the field null
        motate.setnFlgactivo(null);

        // Create the Motate, which fails.

        restMotateMockMvc.perform(post("/api/motates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motate)))
            .andExpect(status().isBadRequest());

        List<Motate> motateList = motateRepository.findAll();
        assertThat(motateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = motateRepository.findAll().size();
        // set the field null
        motate.setnSedereg(null);

        // Create the Motate, which fails.

        restMotateMockMvc.perform(post("/api/motates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motate)))
            .andExpect(status().isBadRequest());

        List<Motate> motateList = motateRepository.findAll();
        assertThat(motateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMotates() throws Exception {
        // Initialize the database
        motateRepository.saveAndFlush(motate);

        // Get all the motateList
        restMotateMockMvc.perform(get("/api/motates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motate.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesmotate").value(hasItem(DEFAULT_V_DESMOTATE.toString())))
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
    public void getMotate() throws Exception {
        // Initialize the database
        motateRepository.saveAndFlush(motate);

        // Get the motate
        restMotateMockMvc.perform(get("/api/motates/{id}", motate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(motate.getId().intValue()))
            .andExpect(jsonPath("$.vDesmotate").value(DEFAULT_V_DESMOTATE.toString()))
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
    public void getNonExistingMotate() throws Exception {
        // Get the motate
        restMotateMockMvc.perform(get("/api/motates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMotate() throws Exception {
        // Initialize the database
        motateRepository.saveAndFlush(motate);
        motateSearchRepository.save(motate);
        int databaseSizeBeforeUpdate = motateRepository.findAll().size();

        // Update the motate
        Motate updatedMotate = motateRepository.findOne(motate.getId());
        updatedMotate
            .vDesmotate(UPDATED_V_DESMOTATE)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restMotateMockMvc.perform(put("/api/motates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMotate)))
            .andExpect(status().isOk());

        // Validate the Motate in the database
        List<Motate> motateList = motateRepository.findAll();
        assertThat(motateList).hasSize(databaseSizeBeforeUpdate);
        Motate testMotate = motateList.get(motateList.size() - 1);
        assertThat(testMotate.getvDesmotate()).isEqualTo(UPDATED_V_DESMOTATE);
        assertThat(testMotate.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testMotate.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testMotate.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testMotate.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testMotate.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testMotate.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testMotate.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Motate in Elasticsearch
        Motate motateEs = motateSearchRepository.findOne(testMotate.getId());
        assertThat(motateEs).isEqualToComparingFieldByField(testMotate);
    }

    @Test
    @Transactional
    public void updateNonExistingMotate() throws Exception {
        int databaseSizeBeforeUpdate = motateRepository.findAll().size();

        // Create the Motate

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMotateMockMvc.perform(put("/api/motates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motate)))
            .andExpect(status().isCreated());

        // Validate the Motate in the database
        List<Motate> motateList = motateRepository.findAll();
        assertThat(motateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMotate() throws Exception {
        // Initialize the database
        motateRepository.saveAndFlush(motate);
        motateSearchRepository.save(motate);
        int databaseSizeBeforeDelete = motateRepository.findAll().size();

        // Get the motate
        restMotateMockMvc.perform(delete("/api/motates/{id}", motate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean motateExistsInEs = motateSearchRepository.exists(motate.getId());
        assertThat(motateExistsInEs).isFalse();

        // Validate the database is empty
        List<Motate> motateList = motateRepository.findAll();
        assertThat(motateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMotate() throws Exception {
        // Initialize the database
        motateRepository.saveAndFlush(motate);
        motateSearchRepository.save(motate);

        // Search the motate
        restMotateMockMvc.perform(get("/api/_search/motates?query=id:" + motate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motate.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesmotate").value(hasItem(DEFAULT_V_DESMOTATE.toString())))
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
        TestUtil.equalsVerifier(Motate.class);
        Motate motate1 = new Motate();
        motate1.setId(1L);
        Motate motate2 = new Motate();
        motate2.setId(motate1.getId());
        assertThat(motate1).isEqualTo(motate2);
        motate2.setId(2L);
        assertThat(motate1).isNotEqualTo(motate2);
        motate1.setId(null);
        assertThat(motate1).isNotEqualTo(motate2);
    }
}
