package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Estperical;
import pe.gob.trabajo.repository.EstpericalRepository;
import pe.gob.trabajo.repository.search.EstpericalSearchRepository;
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
 * Test class for the EstpericalResource REST controller.
 *
 * @see EstpericalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class EstpericalResourceIntTest {

    private static final String DEFAULT_V_NOMESTPER = "AAAAAAAAAA";
    private static final String UPDATED_V_NOMESTPER = "BBBBBBBBBB";

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
    private EstpericalRepository estpericalRepository;

    @Autowired
    private EstpericalSearchRepository estpericalSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEstpericalMockMvc;

    private Estperical estperical;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EstpericalResource estpericalResource = new EstpericalResource(estpericalRepository, estpericalSearchRepository);
        this.restEstpericalMockMvc = MockMvcBuilders.standaloneSetup(estpericalResource)
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
    public static Estperical createEntity(EntityManager em) {
        Estperical estperical = new Estperical()
            .vNomestper(DEFAULT_V_NOMESTPER)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return estperical;
    }

    @Before
    public void initTest() {
        estpericalSearchRepository.deleteAll();
        estperical = createEntity(em);
    }

    @Test
    @Transactional
    public void createEstperical() throws Exception {
        int databaseSizeBeforeCreate = estpericalRepository.findAll().size();

        // Create the Estperical
        restEstpericalMockMvc.perform(post("/api/estpericals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estperical)))
            .andExpect(status().isCreated());

        // Validate the Estperical in the database
        List<Estperical> estpericalList = estpericalRepository.findAll();
        assertThat(estpericalList).hasSize(databaseSizeBeforeCreate + 1);
        Estperical testEstperical = estpericalList.get(estpericalList.size() - 1);
        assertThat(testEstperical.getvNomestper()).isEqualTo(DEFAULT_V_NOMESTPER);
        assertThat(testEstperical.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testEstperical.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testEstperical.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testEstperical.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testEstperical.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testEstperical.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testEstperical.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Estperical in Elasticsearch
        Estperical estpericalEs = estpericalSearchRepository.findOne(testEstperical.getId());
        assertThat(estpericalEs).isEqualToComparingFieldByField(testEstperical);
    }

    @Test
    @Transactional
    public void createEstpericalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = estpericalRepository.findAll().size();

        // Create the Estperical with an existing ID
        estperical.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstpericalMockMvc.perform(post("/api/estpericals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estperical)))
            .andExpect(status().isBadRequest());

        // Validate the Estperical in the database
        List<Estperical> estpericalList = estpericalRepository.findAll();
        assertThat(estpericalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvNomestperIsRequired() throws Exception {
        int databaseSizeBeforeTest = estpericalRepository.findAll().size();
        // set the field null
        estperical.setvNomestper(null);

        // Create the Estperical, which fails.

        restEstpericalMockMvc.perform(post("/api/estpericals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estperical)))
            .andExpect(status().isBadRequest());

        List<Estperical> estpericalList = estpericalRepository.findAll();
        assertThat(estpericalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = estpericalRepository.findAll().size();
        // set the field null
        estperical.setnUsuareg(null);

        // Create the Estperical, which fails.

        restEstpericalMockMvc.perform(post("/api/estpericals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estperical)))
            .andExpect(status().isBadRequest());

        List<Estperical> estpericalList = estpericalRepository.findAll();
        assertThat(estpericalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = estpericalRepository.findAll().size();
        // set the field null
        estperical.settFecreg(null);

        // Create the Estperical, which fails.

        restEstpericalMockMvc.perform(post("/api/estpericals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estperical)))
            .andExpect(status().isBadRequest());

        List<Estperical> estpericalList = estpericalRepository.findAll();
        assertThat(estpericalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = estpericalRepository.findAll().size();
        // set the field null
        estperical.setnFlgactivo(null);

        // Create the Estperical, which fails.

        restEstpericalMockMvc.perform(post("/api/estpericals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estperical)))
            .andExpect(status().isBadRequest());

        List<Estperical> estpericalList = estpericalRepository.findAll();
        assertThat(estpericalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = estpericalRepository.findAll().size();
        // set the field null
        estperical.setnSedereg(null);

        // Create the Estperical, which fails.

        restEstpericalMockMvc.perform(post("/api/estpericals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estperical)))
            .andExpect(status().isBadRequest());

        List<Estperical> estpericalList = estpericalRepository.findAll();
        assertThat(estpericalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEstpericals() throws Exception {
        // Initialize the database
        estpericalRepository.saveAndFlush(estperical);

        // Get all the estpericalList
        restEstpericalMockMvc.perform(get("/api/estpericals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estperical.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNomestper").value(hasItem(DEFAULT_V_NOMESTPER.toString())))
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
    public void getEstperical() throws Exception {
        // Initialize the database
        estpericalRepository.saveAndFlush(estperical);

        // Get the estperical
        restEstpericalMockMvc.perform(get("/api/estpericals/{id}", estperical.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(estperical.getId().intValue()))
            .andExpect(jsonPath("$.vNomestper").value(DEFAULT_V_NOMESTPER.toString()))
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
    public void getNonExistingEstperical() throws Exception {
        // Get the estperical
        restEstpericalMockMvc.perform(get("/api/estpericals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstperical() throws Exception {
        // Initialize the database
        estpericalRepository.saveAndFlush(estperical);
        estpericalSearchRepository.save(estperical);
        int databaseSizeBeforeUpdate = estpericalRepository.findAll().size();

        // Update the estperical
        Estperical updatedEstperical = estpericalRepository.findOne(estperical.getId());
        updatedEstperical
            .vNomestper(UPDATED_V_NOMESTPER)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restEstpericalMockMvc.perform(put("/api/estpericals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEstperical)))
            .andExpect(status().isOk());

        // Validate the Estperical in the database
        List<Estperical> estpericalList = estpericalRepository.findAll();
        assertThat(estpericalList).hasSize(databaseSizeBeforeUpdate);
        Estperical testEstperical = estpericalList.get(estpericalList.size() - 1);
        assertThat(testEstperical.getvNomestper()).isEqualTo(UPDATED_V_NOMESTPER);
        assertThat(testEstperical.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testEstperical.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testEstperical.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testEstperical.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testEstperical.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testEstperical.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testEstperical.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Estperical in Elasticsearch
        Estperical estpericalEs = estpericalSearchRepository.findOne(testEstperical.getId());
        assertThat(estpericalEs).isEqualToComparingFieldByField(testEstperical);
    }

    @Test
    @Transactional
    public void updateNonExistingEstperical() throws Exception {
        int databaseSizeBeforeUpdate = estpericalRepository.findAll().size();

        // Create the Estperical

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEstpericalMockMvc.perform(put("/api/estpericals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estperical)))
            .andExpect(status().isCreated());

        // Validate the Estperical in the database
        List<Estperical> estpericalList = estpericalRepository.findAll();
        assertThat(estpericalList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEstperical() throws Exception {
        // Initialize the database
        estpericalRepository.saveAndFlush(estperical);
        estpericalSearchRepository.save(estperical);
        int databaseSizeBeforeDelete = estpericalRepository.findAll().size();

        // Get the estperical
        restEstpericalMockMvc.perform(delete("/api/estpericals/{id}", estperical.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean estpericalExistsInEs = estpericalSearchRepository.exists(estperical.getId());
        assertThat(estpericalExistsInEs).isFalse();

        // Validate the database is empty
        List<Estperical> estpericalList = estpericalRepository.findAll();
        assertThat(estpericalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEstperical() throws Exception {
        // Initialize the database
        estpericalRepository.saveAndFlush(estperical);
        estpericalSearchRepository.save(estperical);

        // Search the estperical
        restEstpericalMockMvc.perform(get("/api/_search/estpericals?query=id:" + estperical.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estperical.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNomestper").value(hasItem(DEFAULT_V_NOMESTPER.toString())))
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
        TestUtil.equalsVerifier(Estperical.class);
        Estperical estperical1 = new Estperical();
        estperical1.setId(1L);
        Estperical estperical2 = new Estperical();
        estperical2.setId(estperical1.getId());
        assertThat(estperical1).isEqualTo(estperical2);
        estperical2.setId(2L);
        assertThat(estperical1).isNotEqualTo(estperical2);
        estperical1.setId(null);
        assertThat(estperical1).isNotEqualTo(estperical2);
    }
}
