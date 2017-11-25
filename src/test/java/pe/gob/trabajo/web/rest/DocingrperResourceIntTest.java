package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Docingrper;
import pe.gob.trabajo.repository.DocingrperRepository;
import pe.gob.trabajo.repository.search.DocingrperSearchRepository;
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
 * Test class for the DocingrperResource REST controller.
 *
 * @see DocingrperResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class DocingrperResourceIntTest {

    private static final String DEFAULT_V_DESDOCING = "AAAAAAAAAA";
    private static final String UPDATED_V_DESDOCING = "BBBBBBBBBB";

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
    private DocingrperRepository docingrperRepository;

    @Autowired
    private DocingrperSearchRepository docingrperSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDocingrperMockMvc;

    private Docingrper docingrper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocingrperResource docingrperResource = new DocingrperResource(docingrperRepository, docingrperSearchRepository);
        this.restDocingrperMockMvc = MockMvcBuilders.standaloneSetup(docingrperResource)
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
    public static Docingrper createEntity(EntityManager em) {
        Docingrper docingrper = new Docingrper()
            .vDesdocing(DEFAULT_V_DESDOCING)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return docingrper;
    }

    @Before
    public void initTest() {
        docingrperSearchRepository.deleteAll();
        docingrper = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocingrper() throws Exception {
        int databaseSizeBeforeCreate = docingrperRepository.findAll().size();

        // Create the Docingrper
        restDocingrperMockMvc.perform(post("/api/docingrpers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docingrper)))
            .andExpect(status().isCreated());

        // Validate the Docingrper in the database
        List<Docingrper> docingrperList = docingrperRepository.findAll();
        assertThat(docingrperList).hasSize(databaseSizeBeforeCreate + 1);
        Docingrper testDocingrper = docingrperList.get(docingrperList.size() - 1);
        assertThat(testDocingrper.getvDesdocing()).isEqualTo(DEFAULT_V_DESDOCING);
        assertThat(testDocingrper.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testDocingrper.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testDocingrper.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testDocingrper.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testDocingrper.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testDocingrper.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testDocingrper.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Docingrper in Elasticsearch
        Docingrper docingrperEs = docingrperSearchRepository.findOne(testDocingrper.getId());
        assertThat(docingrperEs).isEqualToComparingFieldByField(testDocingrper);
    }

    @Test
    @Transactional
    public void createDocingrperWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = docingrperRepository.findAll().size();

        // Create the Docingrper with an existing ID
        docingrper.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocingrperMockMvc.perform(post("/api/docingrpers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docingrper)))
            .andExpect(status().isBadRequest());

        // Validate the Docingrper in the database
        List<Docingrper> docingrperList = docingrperRepository.findAll();
        assertThat(docingrperList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDesdocingIsRequired() throws Exception {
        int databaseSizeBeforeTest = docingrperRepository.findAll().size();
        // set the field null
        docingrper.setvDesdocing(null);

        // Create the Docingrper, which fails.

        restDocingrperMockMvc.perform(post("/api/docingrpers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docingrper)))
            .andExpect(status().isBadRequest());

        List<Docingrper> docingrperList = docingrperRepository.findAll();
        assertThat(docingrperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = docingrperRepository.findAll().size();
        // set the field null
        docingrper.setnUsuareg(null);

        // Create the Docingrper, which fails.

        restDocingrperMockMvc.perform(post("/api/docingrpers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docingrper)))
            .andExpect(status().isBadRequest());

        List<Docingrper> docingrperList = docingrperRepository.findAll();
        assertThat(docingrperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = docingrperRepository.findAll().size();
        // set the field null
        docingrper.settFecreg(null);

        // Create the Docingrper, which fails.

        restDocingrperMockMvc.perform(post("/api/docingrpers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docingrper)))
            .andExpect(status().isBadRequest());

        List<Docingrper> docingrperList = docingrperRepository.findAll();
        assertThat(docingrperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = docingrperRepository.findAll().size();
        // set the field null
        docingrper.setnFlgactivo(null);

        // Create the Docingrper, which fails.

        restDocingrperMockMvc.perform(post("/api/docingrpers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docingrper)))
            .andExpect(status().isBadRequest());

        List<Docingrper> docingrperList = docingrperRepository.findAll();
        assertThat(docingrperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = docingrperRepository.findAll().size();
        // set the field null
        docingrper.setnSedereg(null);

        // Create the Docingrper, which fails.

        restDocingrperMockMvc.perform(post("/api/docingrpers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docingrper)))
            .andExpect(status().isBadRequest());

        List<Docingrper> docingrperList = docingrperRepository.findAll();
        assertThat(docingrperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocingrpers() throws Exception {
        // Initialize the database
        docingrperRepository.saveAndFlush(docingrper);

        // Get all the docingrperList
        restDocingrperMockMvc.perform(get("/api/docingrpers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docingrper.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesdocing").value(hasItem(DEFAULT_V_DESDOCING.toString())))
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
    public void getDocingrper() throws Exception {
        // Initialize the database
        docingrperRepository.saveAndFlush(docingrper);

        // Get the docingrper
        restDocingrperMockMvc.perform(get("/api/docingrpers/{id}", docingrper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(docingrper.getId().intValue()))
            .andExpect(jsonPath("$.vDesdocing").value(DEFAULT_V_DESDOCING.toString()))
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
    public void getNonExistingDocingrper() throws Exception {
        // Get the docingrper
        restDocingrperMockMvc.perform(get("/api/docingrpers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocingrper() throws Exception {
        // Initialize the database
        docingrperRepository.saveAndFlush(docingrper);
        docingrperSearchRepository.save(docingrper);
        int databaseSizeBeforeUpdate = docingrperRepository.findAll().size();

        // Update the docingrper
        Docingrper updatedDocingrper = docingrperRepository.findOne(docingrper.getId());
        updatedDocingrper
            .vDesdocing(UPDATED_V_DESDOCING)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restDocingrperMockMvc.perform(put("/api/docingrpers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocingrper)))
            .andExpect(status().isOk());

        // Validate the Docingrper in the database
        List<Docingrper> docingrperList = docingrperRepository.findAll();
        assertThat(docingrperList).hasSize(databaseSizeBeforeUpdate);
        Docingrper testDocingrper = docingrperList.get(docingrperList.size() - 1);
        assertThat(testDocingrper.getvDesdocing()).isEqualTo(UPDATED_V_DESDOCING);
        assertThat(testDocingrper.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testDocingrper.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testDocingrper.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testDocingrper.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testDocingrper.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testDocingrper.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testDocingrper.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Docingrper in Elasticsearch
        Docingrper docingrperEs = docingrperSearchRepository.findOne(testDocingrper.getId());
        assertThat(docingrperEs).isEqualToComparingFieldByField(testDocingrper);
    }

    @Test
    @Transactional
    public void updateNonExistingDocingrper() throws Exception {
        int databaseSizeBeforeUpdate = docingrperRepository.findAll().size();

        // Create the Docingrper

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDocingrperMockMvc.perform(put("/api/docingrpers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docingrper)))
            .andExpect(status().isCreated());

        // Validate the Docingrper in the database
        List<Docingrper> docingrperList = docingrperRepository.findAll();
        assertThat(docingrperList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDocingrper() throws Exception {
        // Initialize the database
        docingrperRepository.saveAndFlush(docingrper);
        docingrperSearchRepository.save(docingrper);
        int databaseSizeBeforeDelete = docingrperRepository.findAll().size();

        // Get the docingrper
        restDocingrperMockMvc.perform(delete("/api/docingrpers/{id}", docingrper.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean docingrperExistsInEs = docingrperSearchRepository.exists(docingrper.getId());
        assertThat(docingrperExistsInEs).isFalse();

        // Validate the database is empty
        List<Docingrper> docingrperList = docingrperRepository.findAll();
        assertThat(docingrperList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDocingrper() throws Exception {
        // Initialize the database
        docingrperRepository.saveAndFlush(docingrper);
        docingrperSearchRepository.save(docingrper);

        // Search the docingrper
        restDocingrperMockMvc.perform(get("/api/_search/docingrpers?query=id:" + docingrper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docingrper.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesdocing").value(hasItem(DEFAULT_V_DESDOCING.toString())))
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
        TestUtil.equalsVerifier(Docingrper.class);
        Docingrper docingrper1 = new Docingrper();
        docingrper1.setId(1L);
        Docingrper docingrper2 = new Docingrper();
        docingrper2.setId(docingrper1.getId());
        assertThat(docingrper1).isEqualTo(docingrper2);
        docingrper2.setId(2L);
        assertThat(docingrper1).isNotEqualTo(docingrper2);
        docingrper1.setId(null);
        assertThat(docingrper1).isNotEqualTo(docingrper2);
    }
}
