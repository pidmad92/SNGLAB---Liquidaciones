package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Motcese;
import pe.gob.trabajo.repository.MotceseRepository;
import pe.gob.trabajo.repository.search.MotceseSearchRepository;
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
 * Test class for the MotceseResource REST controller.
 *
 * @see MotceseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class MotceseResourceIntTest {

    private static final String DEFAULT_V_DESMOTCES = "AAAAAAAAAA";
    private static final String UPDATED_V_DESMOTCES = "BBBBBBBBBB";

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
    private MotceseRepository motceseRepository;

    @Autowired
    private MotceseSearchRepository motceseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMotceseMockMvc;

    private Motcese motcese;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MotceseResource motceseResource = new MotceseResource(motceseRepository, motceseSearchRepository);
        this.restMotceseMockMvc = MockMvcBuilders.standaloneSetup(motceseResource)
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
    public static Motcese createEntity(EntityManager em) {
        Motcese motcese = new Motcese()
            .vDesmotces(DEFAULT_V_DESMOTCES)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return motcese;
    }

    @Before
    public void initTest() {
        motceseSearchRepository.deleteAll();
        motcese = createEntity(em);
    }

    @Test
    @Transactional
    public void createMotcese() throws Exception {
        int databaseSizeBeforeCreate = motceseRepository.findAll().size();

        // Create the Motcese
        restMotceseMockMvc.perform(post("/api/motcese")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motcese)))
            .andExpect(status().isCreated());

        // Validate the Motcese in the database
        List<Motcese> motceseList = motceseRepository.findAll();
        assertThat(motceseList).hasSize(databaseSizeBeforeCreate + 1);
        Motcese testMotcese = motceseList.get(motceseList.size() - 1);
        assertThat(testMotcese.getvDesmotces()).isEqualTo(DEFAULT_V_DESMOTCES);
        assertThat(testMotcese.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testMotcese.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testMotcese.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testMotcese.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testMotcese.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testMotcese.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testMotcese.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Motcese in Elasticsearch
        Motcese motceseEs = motceseSearchRepository.findOne(testMotcese.getId());
        assertThat(motceseEs).isEqualToComparingFieldByField(testMotcese);
    }

    @Test
    @Transactional
    public void createMotceseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = motceseRepository.findAll().size();

        // Create the Motcese with an existing ID
        motcese.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMotceseMockMvc.perform(post("/api/motcese")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motcese)))
            .andExpect(status().isBadRequest());

        // Validate the Motcese in the database
        List<Motcese> motceseList = motceseRepository.findAll();
        assertThat(motceseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDesmotcesIsRequired() throws Exception {
        int databaseSizeBeforeTest = motceseRepository.findAll().size();
        // set the field null
        motcese.setvDesmotces(null);

        // Create the Motcese, which fails.

        restMotceseMockMvc.perform(post("/api/motcese")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motcese)))
            .andExpect(status().isBadRequest());

        List<Motcese> motceseList = motceseRepository.findAll();
        assertThat(motceseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = motceseRepository.findAll().size();
        // set the field null
        motcese.setnUsuareg(null);

        // Create the Motcese, which fails.

        restMotceseMockMvc.perform(post("/api/motcese")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motcese)))
            .andExpect(status().isBadRequest());

        List<Motcese> motceseList = motceseRepository.findAll();
        assertThat(motceseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = motceseRepository.findAll().size();
        // set the field null
        motcese.settFecreg(null);

        // Create the Motcese, which fails.

        restMotceseMockMvc.perform(post("/api/motcese")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motcese)))
            .andExpect(status().isBadRequest());

        List<Motcese> motceseList = motceseRepository.findAll();
        assertThat(motceseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = motceseRepository.findAll().size();
        // set the field null
        motcese.setnFlgactivo(null);

        // Create the Motcese, which fails.

        restMotceseMockMvc.perform(post("/api/motcese")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motcese)))
            .andExpect(status().isBadRequest());

        List<Motcese> motceseList = motceseRepository.findAll();
        assertThat(motceseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = motceseRepository.findAll().size();
        // set the field null
        motcese.setnSedereg(null);

        // Create the Motcese, which fails.

        restMotceseMockMvc.perform(post("/api/motcese")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motcese)))
            .andExpect(status().isBadRequest());

        List<Motcese> motceseList = motceseRepository.findAll();
        assertThat(motceseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMotcese() throws Exception {
        // Initialize the database
        motceseRepository.saveAndFlush(motcese);

        // Get all the motceseList
        restMotceseMockMvc.perform(get("/api/motcese?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motcese.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesmotces").value(hasItem(DEFAULT_V_DESMOTCES.toString())))
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
    public void getMotcese() throws Exception {
        // Initialize the database
        motceseRepository.saveAndFlush(motcese);

        // Get the motcese
        restMotceseMockMvc.perform(get("/api/motcese/{id}", motcese.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(motcese.getId().intValue()))
            .andExpect(jsonPath("$.vDesmotces").value(DEFAULT_V_DESMOTCES.toString()))
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
    public void getNonExistingMotcese() throws Exception {
        // Get the motcese
        restMotceseMockMvc.perform(get("/api/motcese/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMotcese() throws Exception {
        // Initialize the database
        motceseRepository.saveAndFlush(motcese);
        motceseSearchRepository.save(motcese);
        int databaseSizeBeforeUpdate = motceseRepository.findAll().size();

        // Update the motcese
        Motcese updatedMotcese = motceseRepository.findOne(motcese.getId());
        updatedMotcese
            .vDesmotces(UPDATED_V_DESMOTCES)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restMotceseMockMvc.perform(put("/api/motcese")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMotcese)))
            .andExpect(status().isOk());

        // Validate the Motcese in the database
        List<Motcese> motceseList = motceseRepository.findAll();
        assertThat(motceseList).hasSize(databaseSizeBeforeUpdate);
        Motcese testMotcese = motceseList.get(motceseList.size() - 1);
        assertThat(testMotcese.getvDesmotces()).isEqualTo(UPDATED_V_DESMOTCES);
        assertThat(testMotcese.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testMotcese.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testMotcese.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testMotcese.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testMotcese.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testMotcese.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testMotcese.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Motcese in Elasticsearch
        Motcese motceseEs = motceseSearchRepository.findOne(testMotcese.getId());
        assertThat(motceseEs).isEqualToComparingFieldByField(testMotcese);
    }

    @Test
    @Transactional
    public void updateNonExistingMotcese() throws Exception {
        int databaseSizeBeforeUpdate = motceseRepository.findAll().size();

        // Create the Motcese

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMotceseMockMvc.perform(put("/api/motcese")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motcese)))
            .andExpect(status().isCreated());

        // Validate the Motcese in the database
        List<Motcese> motceseList = motceseRepository.findAll();
        assertThat(motceseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMotcese() throws Exception {
        // Initialize the database
        motceseRepository.saveAndFlush(motcese);
        motceseSearchRepository.save(motcese);
        int databaseSizeBeforeDelete = motceseRepository.findAll().size();

        // Get the motcese
        restMotceseMockMvc.perform(delete("/api/motcese/{id}", motcese.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean motceseExistsInEs = motceseSearchRepository.exists(motcese.getId());
        assertThat(motceseExistsInEs).isFalse();

        // Validate the database is empty
        List<Motcese> motceseList = motceseRepository.findAll();
        assertThat(motceseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMotcese() throws Exception {
        // Initialize the database
        motceseRepository.saveAndFlush(motcese);
        motceseSearchRepository.save(motcese);

        // Search the motcese
        restMotceseMockMvc.perform(get("/api/_search/motcese?query=id:" + motcese.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motcese.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesmotces").value(hasItem(DEFAULT_V_DESMOTCES.toString())))
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
        TestUtil.equalsVerifier(Motcese.class);
        Motcese motcese1 = new Motcese();
        motcese1.setId(1L);
        Motcese motcese2 = new Motcese();
        motcese2.setId(motcese1.getId());
        assertThat(motcese1).isEqualTo(motcese2);
        motcese2.setId(2L);
        assertThat(motcese1).isNotEqualTo(motcese2);
        motcese1.setId(null);
        assertThat(motcese1).isNotEqualTo(motcese2);
    }
}
