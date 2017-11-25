package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Calrcmperi;
import pe.gob.trabajo.repository.CalrcmperiRepository;
import pe.gob.trabajo.repository.search.CalrcmperiSearchRepository;
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
 * Test class for the CalrcmperiResource REST controller.
 *
 * @see CalrcmperiResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class CalrcmperiResourceIntTest {

    private static final BigDecimal DEFAULT_N_CALRCMPER = new BigDecimal(1);
    private static final BigDecimal UPDATED_N_CALRCMPER = new BigDecimal(2);

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
    private CalrcmperiRepository calrcmperiRepository;

    @Autowired
    private CalrcmperiSearchRepository calrcmperiSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCalrcmperiMockMvc;

    private Calrcmperi calrcmperi;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CalrcmperiResource calrcmperiResource = new CalrcmperiResource(calrcmperiRepository, calrcmperiSearchRepository);
        this.restCalrcmperiMockMvc = MockMvcBuilders.standaloneSetup(calrcmperiResource)
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
    public static Calrcmperi createEntity(EntityManager em) {
        Calrcmperi calrcmperi = new Calrcmperi()
            .nCalrcmper(DEFAULT_N_CALRCMPER)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return calrcmperi;
    }

    @Before
    public void initTest() {
        calrcmperiSearchRepository.deleteAll();
        calrcmperi = createEntity(em);
    }

    @Test
    @Transactional
    public void createCalrcmperi() throws Exception {
        int databaseSizeBeforeCreate = calrcmperiRepository.findAll().size();

        // Create the Calrcmperi
        restCalrcmperiMockMvc.perform(post("/api/calrcmperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calrcmperi)))
            .andExpect(status().isCreated());

        // Validate the Calrcmperi in the database
        List<Calrcmperi> calrcmperiList = calrcmperiRepository.findAll();
        assertThat(calrcmperiList).hasSize(databaseSizeBeforeCreate + 1);
        Calrcmperi testCalrcmperi = calrcmperiList.get(calrcmperiList.size() - 1);
        assertThat(testCalrcmperi.getnCalrcmper()).isEqualTo(DEFAULT_N_CALRCMPER);
        assertThat(testCalrcmperi.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testCalrcmperi.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testCalrcmperi.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testCalrcmperi.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testCalrcmperi.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testCalrcmperi.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testCalrcmperi.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Calrcmperi in Elasticsearch
        Calrcmperi calrcmperiEs = calrcmperiSearchRepository.findOne(testCalrcmperi.getId());
        assertThat(calrcmperiEs).isEqualToComparingFieldByField(testCalrcmperi);
    }

    @Test
    @Transactional
    public void createCalrcmperiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = calrcmperiRepository.findAll().size();

        // Create the Calrcmperi with an existing ID
        calrcmperi.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalrcmperiMockMvc.perform(post("/api/calrcmperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calrcmperi)))
            .andExpect(status().isBadRequest());

        // Validate the Calrcmperi in the database
        List<Calrcmperi> calrcmperiList = calrcmperiRepository.findAll();
        assertThat(calrcmperiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = calrcmperiRepository.findAll().size();
        // set the field null
        calrcmperi.setnUsuareg(null);

        // Create the Calrcmperi, which fails.

        restCalrcmperiMockMvc.perform(post("/api/calrcmperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calrcmperi)))
            .andExpect(status().isBadRequest());

        List<Calrcmperi> calrcmperiList = calrcmperiRepository.findAll();
        assertThat(calrcmperiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = calrcmperiRepository.findAll().size();
        // set the field null
        calrcmperi.settFecreg(null);

        // Create the Calrcmperi, which fails.

        restCalrcmperiMockMvc.perform(post("/api/calrcmperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calrcmperi)))
            .andExpect(status().isBadRequest());

        List<Calrcmperi> calrcmperiList = calrcmperiRepository.findAll();
        assertThat(calrcmperiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = calrcmperiRepository.findAll().size();
        // set the field null
        calrcmperi.setnFlgactivo(null);

        // Create the Calrcmperi, which fails.

        restCalrcmperiMockMvc.perform(post("/api/calrcmperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calrcmperi)))
            .andExpect(status().isBadRequest());

        List<Calrcmperi> calrcmperiList = calrcmperiRepository.findAll();
        assertThat(calrcmperiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = calrcmperiRepository.findAll().size();
        // set the field null
        calrcmperi.setnSedereg(null);

        // Create the Calrcmperi, which fails.

        restCalrcmperiMockMvc.perform(post("/api/calrcmperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calrcmperi)))
            .andExpect(status().isBadRequest());

        List<Calrcmperi> calrcmperiList = calrcmperiRepository.findAll();
        assertThat(calrcmperiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCalrcmperis() throws Exception {
        // Initialize the database
        calrcmperiRepository.saveAndFlush(calrcmperi);

        // Get all the calrcmperiList
        restCalrcmperiMockMvc.perform(get("/api/calrcmperis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calrcmperi.getId().intValue())))
            .andExpect(jsonPath("$.[*].nCalrcmper").value(hasItem(DEFAULT_N_CALRCMPER.intValue())))
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
    public void getCalrcmperi() throws Exception {
        // Initialize the database
        calrcmperiRepository.saveAndFlush(calrcmperi);

        // Get the calrcmperi
        restCalrcmperiMockMvc.perform(get("/api/calrcmperis/{id}", calrcmperi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(calrcmperi.getId().intValue()))
            .andExpect(jsonPath("$.nCalrcmper").value(DEFAULT_N_CALRCMPER.intValue()))
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
    public void getNonExistingCalrcmperi() throws Exception {
        // Get the calrcmperi
        restCalrcmperiMockMvc.perform(get("/api/calrcmperis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCalrcmperi() throws Exception {
        // Initialize the database
        calrcmperiRepository.saveAndFlush(calrcmperi);
        calrcmperiSearchRepository.save(calrcmperi);
        int databaseSizeBeforeUpdate = calrcmperiRepository.findAll().size();

        // Update the calrcmperi
        Calrcmperi updatedCalrcmperi = calrcmperiRepository.findOne(calrcmperi.getId());
        updatedCalrcmperi
            .nCalrcmper(UPDATED_N_CALRCMPER)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restCalrcmperiMockMvc.perform(put("/api/calrcmperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCalrcmperi)))
            .andExpect(status().isOk());

        // Validate the Calrcmperi in the database
        List<Calrcmperi> calrcmperiList = calrcmperiRepository.findAll();
        assertThat(calrcmperiList).hasSize(databaseSizeBeforeUpdate);
        Calrcmperi testCalrcmperi = calrcmperiList.get(calrcmperiList.size() - 1);
        assertThat(testCalrcmperi.getnCalrcmper()).isEqualTo(UPDATED_N_CALRCMPER);
        assertThat(testCalrcmperi.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testCalrcmperi.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testCalrcmperi.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testCalrcmperi.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testCalrcmperi.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testCalrcmperi.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testCalrcmperi.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Calrcmperi in Elasticsearch
        Calrcmperi calrcmperiEs = calrcmperiSearchRepository.findOne(testCalrcmperi.getId());
        assertThat(calrcmperiEs).isEqualToComparingFieldByField(testCalrcmperi);
    }

    @Test
    @Transactional
    public void updateNonExistingCalrcmperi() throws Exception {
        int databaseSizeBeforeUpdate = calrcmperiRepository.findAll().size();

        // Create the Calrcmperi

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCalrcmperiMockMvc.perform(put("/api/calrcmperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calrcmperi)))
            .andExpect(status().isCreated());

        // Validate the Calrcmperi in the database
        List<Calrcmperi> calrcmperiList = calrcmperiRepository.findAll();
        assertThat(calrcmperiList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCalrcmperi() throws Exception {
        // Initialize the database
        calrcmperiRepository.saveAndFlush(calrcmperi);
        calrcmperiSearchRepository.save(calrcmperi);
        int databaseSizeBeforeDelete = calrcmperiRepository.findAll().size();

        // Get the calrcmperi
        restCalrcmperiMockMvc.perform(delete("/api/calrcmperis/{id}", calrcmperi.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean calrcmperiExistsInEs = calrcmperiSearchRepository.exists(calrcmperi.getId());
        assertThat(calrcmperiExistsInEs).isFalse();

        // Validate the database is empty
        List<Calrcmperi> calrcmperiList = calrcmperiRepository.findAll();
        assertThat(calrcmperiList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCalrcmperi() throws Exception {
        // Initialize the database
        calrcmperiRepository.saveAndFlush(calrcmperi);
        calrcmperiSearchRepository.save(calrcmperi);

        // Search the calrcmperi
        restCalrcmperiMockMvc.perform(get("/api/_search/calrcmperis?query=id:" + calrcmperi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calrcmperi.getId().intValue())))
            .andExpect(jsonPath("$.[*].nCalrcmper").value(hasItem(DEFAULT_N_CALRCMPER.intValue())))
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
        TestUtil.equalsVerifier(Calrcmperi.class);
        Calrcmperi calrcmperi1 = new Calrcmperi();
        calrcmperi1.setId(1L);
        Calrcmperi calrcmperi2 = new Calrcmperi();
        calrcmperi2.setId(calrcmperi1.getId());
        assertThat(calrcmperi1).isEqualTo(calrcmperi2);
        calrcmperi2.setId(2L);
        assertThat(calrcmperi1).isNotEqualTo(calrcmperi2);
        calrcmperi1.setId(null);
        assertThat(calrcmperi1).isNotEqualTo(calrcmperi2);
    }
}
