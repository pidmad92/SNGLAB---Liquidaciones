package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Conceprem;
import pe.gob.trabajo.repository.ConcepremRepository;
import pe.gob.trabajo.repository.search.ConcepremSearchRepository;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ConcepremResource REST controller.
 *
 * @see ConcepremResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class ConcepremResourceIntTest {

    private static final String DEFAULT_V_NOMCONREM = "AAAAAAAAAA";
    private static final String UPDATED_V_NOMCONREM = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_N_VALCONREM = new BigDecimal(1);
    private static final BigDecimal UPDATED_N_VALCONREM = new BigDecimal(2);

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
    private ConcepremRepository concepremRepository;

    @Autowired
    private ConcepremSearchRepository concepremSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConcepremMockMvc;

    private Conceprem conceprem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConcepremResource concepremResource = new ConcepremResource(concepremRepository, concepremSearchRepository);
        this.restConcepremMockMvc = MockMvcBuilders.standaloneSetup(concepremResource)
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
    public static Conceprem createEntity(EntityManager em) {
        Conceprem conceprem = new Conceprem()
            .vNomconrem(DEFAULT_V_NOMCONREM)
            .nValconrem(DEFAULT_N_VALCONREM)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return conceprem;
    }

    @Before
    public void initTest() {
        concepremSearchRepository.deleteAll();
        conceprem = createEntity(em);
    }

    @Test
    @Transactional
    public void createConceprem() throws Exception {
        int databaseSizeBeforeCreate = concepremRepository.findAll().size();

        // Create the Conceprem
        restConcepremMockMvc.perform(post("/api/conceprems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conceprem)))
            .andExpect(status().isCreated());

        // Validate the Conceprem in the database
        List<Conceprem> concepremList = concepremRepository.findAll();
        assertThat(concepremList).hasSize(databaseSizeBeforeCreate + 1);
        Conceprem testConceprem = concepremList.get(concepremList.size() - 1);
        assertThat(testConceprem.getvNomconrem()).isEqualTo(DEFAULT_V_NOMCONREM);
        assertThat(testConceprem.getnValconrem()).isEqualTo(DEFAULT_N_VALCONREM);
        assertThat(testConceprem.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testConceprem.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testConceprem.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testConceprem.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testConceprem.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testConceprem.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testConceprem.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Conceprem in Elasticsearch
        Conceprem concepremEs = concepremSearchRepository.findOne(testConceprem.getId());
        assertThat(concepremEs).isEqualToComparingFieldByField(testConceprem);
    }

    @Test
    @Transactional
    public void createConcepremWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = concepremRepository.findAll().size();

        // Create the Conceprem with an existing ID
        conceprem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConcepremMockMvc.perform(post("/api/conceprems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conceprem)))
            .andExpect(status().isBadRequest());

        // Validate the Conceprem in the database
        List<Conceprem> concepremList = concepremRepository.findAll();
        assertThat(concepremList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvNomconremIsRequired() throws Exception {
        int databaseSizeBeforeTest = concepremRepository.findAll().size();
        // set the field null
        conceprem.setvNomconrem(null);

        // Create the Conceprem, which fails.

        restConcepremMockMvc.perform(post("/api/conceprems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conceprem)))
            .andExpect(status().isBadRequest());

        List<Conceprem> concepremList = concepremRepository.findAll();
        assertThat(concepremList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = concepremRepository.findAll().size();
        // set the field null
        conceprem.setnUsuareg(null);

        // Create the Conceprem, which fails.

        restConcepremMockMvc.perform(post("/api/conceprems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conceprem)))
            .andExpect(status().isBadRequest());

        List<Conceprem> concepremList = concepremRepository.findAll();
        assertThat(concepremList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = concepremRepository.findAll().size();
        // set the field null
        conceprem.settFecreg(null);

        // Create the Conceprem, which fails.

        restConcepremMockMvc.perform(post("/api/conceprems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conceprem)))
            .andExpect(status().isBadRequest());

        List<Conceprem> concepremList = concepremRepository.findAll();
        assertThat(concepremList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = concepremRepository.findAll().size();
        // set the field null
        conceprem.setnFlgactivo(null);

        // Create the Conceprem, which fails.

        restConcepremMockMvc.perform(post("/api/conceprems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conceprem)))
            .andExpect(status().isBadRequest());

        List<Conceprem> concepremList = concepremRepository.findAll();
        assertThat(concepremList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = concepremRepository.findAll().size();
        // set the field null
        conceprem.setnSedereg(null);

        // Create the Conceprem, which fails.

        restConcepremMockMvc.perform(post("/api/conceprems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conceprem)))
            .andExpect(status().isBadRequest());

        List<Conceprem> concepremList = concepremRepository.findAll();
        assertThat(concepremList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConceprems() throws Exception {
        // Initialize the database
        concepremRepository.saveAndFlush(conceprem);

        // Get all the concepremList
        restConcepremMockMvc.perform(get("/api/conceprems?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conceprem.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNomconrem").value(hasItem(DEFAULT_V_NOMCONREM.toString())))
            .andExpect(jsonPath("$.[*].nValconrem").value(hasItem(DEFAULT_N_VALCONREM.intValue())))
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
    public void getConceprem() throws Exception {
        // Initialize the database
        concepremRepository.saveAndFlush(conceprem);

        // Get the conceprem
        restConcepremMockMvc.perform(get("/api/conceprems/{id}", conceprem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(conceprem.getId().intValue()))
            .andExpect(jsonPath("$.vNomconrem").value(DEFAULT_V_NOMCONREM.toString()))
            .andExpect(jsonPath("$.nValconrem").value(DEFAULT_N_VALCONREM.intValue()))
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
    public void getNonExistingConceprem() throws Exception {
        // Get the conceprem
        restConcepremMockMvc.perform(get("/api/conceprems/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConceprem() throws Exception {
        // Initialize the database
        concepremRepository.saveAndFlush(conceprem);
        concepremSearchRepository.save(conceprem);
        int databaseSizeBeforeUpdate = concepremRepository.findAll().size();

        // Update the conceprem
        Conceprem updatedConceprem = concepremRepository.findOne(conceprem.getId());
        updatedConceprem
            .vNomconrem(UPDATED_V_NOMCONREM)
            .nValconrem(UPDATED_N_VALCONREM)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restConcepremMockMvc.perform(put("/api/conceprems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConceprem)))
            .andExpect(status().isOk());

        // Validate the Conceprem in the database
        List<Conceprem> concepremList = concepremRepository.findAll();
        assertThat(concepremList).hasSize(databaseSizeBeforeUpdate);
        Conceprem testConceprem = concepremList.get(concepremList.size() - 1);
        assertThat(testConceprem.getvNomconrem()).isEqualTo(UPDATED_V_NOMCONREM);
        assertThat(testConceprem.getnValconrem()).isEqualTo(UPDATED_N_VALCONREM);
        assertThat(testConceprem.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testConceprem.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testConceprem.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testConceprem.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testConceprem.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testConceprem.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testConceprem.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Conceprem in Elasticsearch
        Conceprem concepremEs = concepremSearchRepository.findOne(testConceprem.getId());
        assertThat(concepremEs).isEqualToComparingFieldByField(testConceprem);
    }

    @Test
    @Transactional
    public void updateNonExistingConceprem() throws Exception {
        int databaseSizeBeforeUpdate = concepremRepository.findAll().size();

        // Create the Conceprem

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restConcepremMockMvc.perform(put("/api/conceprems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conceprem)))
            .andExpect(status().isCreated());

        // Validate the Conceprem in the database
        List<Conceprem> concepremList = concepremRepository.findAll();
        assertThat(concepremList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteConceprem() throws Exception {
        // Initialize the database
        concepremRepository.saveAndFlush(conceprem);
        concepremSearchRepository.save(conceprem);
        int databaseSizeBeforeDelete = concepremRepository.findAll().size();

        // Get the conceprem
        restConcepremMockMvc.perform(delete("/api/conceprems/{id}", conceprem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean concepremExistsInEs = concepremSearchRepository.exists(conceprem.getId());
        assertThat(concepremExistsInEs).isFalse();

        // Validate the database is empty
        List<Conceprem> concepremList = concepremRepository.findAll();
        assertThat(concepremList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchConceprem() throws Exception {
        // Initialize the database
        concepremRepository.saveAndFlush(conceprem);
        concepremSearchRepository.save(conceprem);

        // Search the conceprem
        restConcepremMockMvc.perform(get("/api/_search/conceprems?query=id:" + conceprem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conceprem.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNomconrem").value(hasItem(DEFAULT_V_NOMCONREM.toString())))
            .andExpect(jsonPath("$.[*].nValconrem").value(hasItem(DEFAULT_N_VALCONREM.intValue())))
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
        TestUtil.equalsVerifier(Conceprem.class);
        Conceprem conceprem1 = new Conceprem();
        conceprem1.setId(1L);
        Conceprem conceprem2 = new Conceprem();
        conceprem2.setId(conceprem1.getId());
        assertThat(conceprem1).isEqualTo(conceprem2);
        conceprem2.setId(2L);
        assertThat(conceprem1).isNotEqualTo(conceprem2);
        conceprem1.setId(null);
        assertThat(conceprem1).isNotEqualTo(conceprem2);
    }
}
