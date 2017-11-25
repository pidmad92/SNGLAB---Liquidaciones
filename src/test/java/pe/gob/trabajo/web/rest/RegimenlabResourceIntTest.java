package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Regimenlab;
import pe.gob.trabajo.repository.RegimenlabRepository;
import pe.gob.trabajo.repository.search.RegimenlabSearchRepository;
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
 * Test class for the RegimenlabResource REST controller.
 *
 * @see RegimenlabResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class RegimenlabResourceIntTest {

    private static final String DEFAULT_V_DESREGLAB = "AAAAAAAAAA";
    private static final String UPDATED_V_DESREGLAB = "BBBBBBBBBB";

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
    private RegimenlabRepository regimenlabRepository;

    @Autowired
    private RegimenlabSearchRepository regimenlabSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRegimenlabMockMvc;

    private Regimenlab regimenlab;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RegimenlabResource regimenlabResource = new RegimenlabResource(regimenlabRepository, regimenlabSearchRepository);
        this.restRegimenlabMockMvc = MockMvcBuilders.standaloneSetup(regimenlabResource)
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
    public static Regimenlab createEntity(EntityManager em) {
        Regimenlab regimenlab = new Regimenlab()
            .vDesreglab(DEFAULT_V_DESREGLAB)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return regimenlab;
    }

    @Before
    public void initTest() {
        regimenlabSearchRepository.deleteAll();
        regimenlab = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegimenlab() throws Exception {
        int databaseSizeBeforeCreate = regimenlabRepository.findAll().size();

        // Create the Regimenlab
        restRegimenlabMockMvc.perform(post("/api/regimenlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regimenlab)))
            .andExpect(status().isCreated());

        // Validate the Regimenlab in the database
        List<Regimenlab> regimenlabList = regimenlabRepository.findAll();
        assertThat(regimenlabList).hasSize(databaseSizeBeforeCreate + 1);
        Regimenlab testRegimenlab = regimenlabList.get(regimenlabList.size() - 1);
        assertThat(testRegimenlab.getvDesreglab()).isEqualTo(DEFAULT_V_DESREGLAB);
        assertThat(testRegimenlab.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testRegimenlab.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testRegimenlab.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testRegimenlab.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testRegimenlab.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testRegimenlab.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testRegimenlab.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Regimenlab in Elasticsearch
        Regimenlab regimenlabEs = regimenlabSearchRepository.findOne(testRegimenlab.getId());
        assertThat(regimenlabEs).isEqualToComparingFieldByField(testRegimenlab);
    }

    @Test
    @Transactional
    public void createRegimenlabWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = regimenlabRepository.findAll().size();

        // Create the Regimenlab with an existing ID
        regimenlab.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegimenlabMockMvc.perform(post("/api/regimenlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regimenlab)))
            .andExpect(status().isBadRequest());

        // Validate the Regimenlab in the database
        List<Regimenlab> regimenlabList = regimenlabRepository.findAll();
        assertThat(regimenlabList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDesreglabIsRequired() throws Exception {
        int databaseSizeBeforeTest = regimenlabRepository.findAll().size();
        // set the field null
        regimenlab.setvDesreglab(null);

        // Create the Regimenlab, which fails.

        restRegimenlabMockMvc.perform(post("/api/regimenlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regimenlab)))
            .andExpect(status().isBadRequest());

        List<Regimenlab> regimenlabList = regimenlabRepository.findAll();
        assertThat(regimenlabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = regimenlabRepository.findAll().size();
        // set the field null
        regimenlab.setnUsuareg(null);

        // Create the Regimenlab, which fails.

        restRegimenlabMockMvc.perform(post("/api/regimenlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regimenlab)))
            .andExpect(status().isBadRequest());

        List<Regimenlab> regimenlabList = regimenlabRepository.findAll();
        assertThat(regimenlabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = regimenlabRepository.findAll().size();
        // set the field null
        regimenlab.settFecreg(null);

        // Create the Regimenlab, which fails.

        restRegimenlabMockMvc.perform(post("/api/regimenlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regimenlab)))
            .andExpect(status().isBadRequest());

        List<Regimenlab> regimenlabList = regimenlabRepository.findAll();
        assertThat(regimenlabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = regimenlabRepository.findAll().size();
        // set the field null
        regimenlab.setnFlgactivo(null);

        // Create the Regimenlab, which fails.

        restRegimenlabMockMvc.perform(post("/api/regimenlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regimenlab)))
            .andExpect(status().isBadRequest());

        List<Regimenlab> regimenlabList = regimenlabRepository.findAll();
        assertThat(regimenlabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = regimenlabRepository.findAll().size();
        // set the field null
        regimenlab.setnSedereg(null);

        // Create the Regimenlab, which fails.

        restRegimenlabMockMvc.perform(post("/api/regimenlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regimenlab)))
            .andExpect(status().isBadRequest());

        List<Regimenlab> regimenlabList = regimenlabRepository.findAll();
        assertThat(regimenlabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRegimenlabs() throws Exception {
        // Initialize the database
        regimenlabRepository.saveAndFlush(regimenlab);

        // Get all the regimenlabList
        restRegimenlabMockMvc.perform(get("/api/regimenlabs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regimenlab.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesreglab").value(hasItem(DEFAULT_V_DESREGLAB.toString())))
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
    public void getRegimenlab() throws Exception {
        // Initialize the database
        regimenlabRepository.saveAndFlush(regimenlab);

        // Get the regimenlab
        restRegimenlabMockMvc.perform(get("/api/regimenlabs/{id}", regimenlab.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(regimenlab.getId().intValue()))
            .andExpect(jsonPath("$.vDesreglab").value(DEFAULT_V_DESREGLAB.toString()))
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
    public void getNonExistingRegimenlab() throws Exception {
        // Get the regimenlab
        restRegimenlabMockMvc.perform(get("/api/regimenlabs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegimenlab() throws Exception {
        // Initialize the database
        regimenlabRepository.saveAndFlush(regimenlab);
        regimenlabSearchRepository.save(regimenlab);
        int databaseSizeBeforeUpdate = regimenlabRepository.findAll().size();

        // Update the regimenlab
        Regimenlab updatedRegimenlab = regimenlabRepository.findOne(regimenlab.getId());
        updatedRegimenlab
            .vDesreglab(UPDATED_V_DESREGLAB)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restRegimenlabMockMvc.perform(put("/api/regimenlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRegimenlab)))
            .andExpect(status().isOk());

        // Validate the Regimenlab in the database
        List<Regimenlab> regimenlabList = regimenlabRepository.findAll();
        assertThat(regimenlabList).hasSize(databaseSizeBeforeUpdate);
        Regimenlab testRegimenlab = regimenlabList.get(regimenlabList.size() - 1);
        assertThat(testRegimenlab.getvDesreglab()).isEqualTo(UPDATED_V_DESREGLAB);
        assertThat(testRegimenlab.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testRegimenlab.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testRegimenlab.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testRegimenlab.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testRegimenlab.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testRegimenlab.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testRegimenlab.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Regimenlab in Elasticsearch
        Regimenlab regimenlabEs = regimenlabSearchRepository.findOne(testRegimenlab.getId());
        assertThat(regimenlabEs).isEqualToComparingFieldByField(testRegimenlab);
    }

    @Test
    @Transactional
    public void updateNonExistingRegimenlab() throws Exception {
        int databaseSizeBeforeUpdate = regimenlabRepository.findAll().size();

        // Create the Regimenlab

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRegimenlabMockMvc.perform(put("/api/regimenlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regimenlab)))
            .andExpect(status().isCreated());

        // Validate the Regimenlab in the database
        List<Regimenlab> regimenlabList = regimenlabRepository.findAll();
        assertThat(regimenlabList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRegimenlab() throws Exception {
        // Initialize the database
        regimenlabRepository.saveAndFlush(regimenlab);
        regimenlabSearchRepository.save(regimenlab);
        int databaseSizeBeforeDelete = regimenlabRepository.findAll().size();

        // Get the regimenlab
        restRegimenlabMockMvc.perform(delete("/api/regimenlabs/{id}", regimenlab.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean regimenlabExistsInEs = regimenlabSearchRepository.exists(regimenlab.getId());
        assertThat(regimenlabExistsInEs).isFalse();

        // Validate the database is empty
        List<Regimenlab> regimenlabList = regimenlabRepository.findAll();
        assertThat(regimenlabList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRegimenlab() throws Exception {
        // Initialize the database
        regimenlabRepository.saveAndFlush(regimenlab);
        regimenlabSearchRepository.save(regimenlab);

        // Search the regimenlab
        restRegimenlabMockMvc.perform(get("/api/_search/regimenlabs?query=id:" + regimenlab.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regimenlab.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesreglab").value(hasItem(DEFAULT_V_DESREGLAB.toString())))
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
        TestUtil.equalsVerifier(Regimenlab.class);
        Regimenlab regimenlab1 = new Regimenlab();
        regimenlab1.setId(1L);
        Regimenlab regimenlab2 = new Regimenlab();
        regimenlab2.setId(regimenlab1.getId());
        assertThat(regimenlab1).isEqualTo(regimenlab2);
        regimenlab2.setId(2L);
        assertThat(regimenlab1).isNotEqualTo(regimenlab2);
        regimenlab1.setId(null);
        assertThat(regimenlab1).isNotEqualTo(regimenlab2);
    }
}
