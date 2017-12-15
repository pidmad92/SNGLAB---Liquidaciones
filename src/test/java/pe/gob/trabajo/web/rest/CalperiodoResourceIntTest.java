package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Calperiodo;
import pe.gob.trabajo.repository.CalperiodoRepository;
import pe.gob.trabajo.repository.search.CalperiodoSearchRepository;
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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static pe.gob.trabajo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CalperiodoResource REST controller.
 *
 * @see CalperiodoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class CalperiodoResourceIntTest {

    private static final BigDecimal DEFAULT_N_CALPER = new BigDecimal(1);
    private static final BigDecimal UPDATED_N_CALPER = new BigDecimal(2);

    private static final BigDecimal DEFAULT_N_CALPER_2 = new BigDecimal(1);
    private static final BigDecimal UPDATED_N_CALPER_2 = new BigDecimal(2);

    private static final Integer DEFAULT_N_NUMPER = 1;
    private static final Integer UPDATED_N_NUMPER = 2;

    private static final LocalDate DEFAULT_T_FECINI = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_T_FECINI = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_T_FECFIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_T_FECFIN = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_N_TNOCOMPUT = 1;
    private static final Integer UPDATED_N_TNOCOMPUT = 2;

    private static final Integer DEFAULT_N_TCOMPUT = 1;
    private static final Integer UPDATED_N_TCOMPUT = 2;

    private static final Long DEFAULT_N_CODHIJO_1 = 1L;
    private static final Long UPDATED_N_CODHIJO_1 = 2L;

    private static final Long DEFAULT_N_CODHIJO_2 = 1L;
    private static final Long UPDATED_N_CODHIJO_2 = 2L;

    private static final Integer DEFAULT_N_DGOZADOS = 1;
    private static final Integer UPDATED_N_DGOZADOS = 2;

    private static final Integer DEFAULT_N_DADEUDOS = 1;
    private static final Integer UPDATED_N_DADEUDOS = 2;

    private static final Integer DEFAULT_N_ANOBASE = 1;
    private static final Integer UPDATED_N_ANOBASE = 2;

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
    private CalperiodoRepository calperiodoRepository;

    @Autowired
    private CalperiodoSearchRepository calperiodoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCalperiodoMockMvc;

    private Calperiodo calperiodo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CalperiodoResource calperiodoResource = new CalperiodoResource(calperiodoRepository, calperiodoSearchRepository);
        this.restCalperiodoMockMvc = MockMvcBuilders.standaloneSetup(calperiodoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calperiodo createEntity(EntityManager em) {
        Calperiodo calperiodo = new Calperiodo()
            .nCalper(DEFAULT_N_CALPER)
            .nCalper2(DEFAULT_N_CALPER_2)
            .nNumper(DEFAULT_N_NUMPER)
            .tFecini(DEFAULT_T_FECINI)
            .tFecfin(DEFAULT_T_FECFIN)
            .nTnocomput(DEFAULT_N_TNOCOMPUT)
            .nTcomput(DEFAULT_N_TCOMPUT)
            .nCodhijo1(DEFAULT_N_CODHIJO_1)
            .nCodhijo2(DEFAULT_N_CODHIJO_2)
            .nDgozados(DEFAULT_N_DGOZADOS)
            .nDadeudos(DEFAULT_N_DADEUDOS)
            .nAnobase(DEFAULT_N_ANOBASE)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return calperiodo;
    }

    @Before
    public void initTest() {
        calperiodoSearchRepository.deleteAll();
        calperiodo = createEntity(em);
    }

    @Test
    @Transactional
    public void createCalperiodo() throws Exception {
        int databaseSizeBeforeCreate = calperiodoRepository.findAll().size();

        // Create the Calperiodo
        restCalperiodoMockMvc.perform(post("/api/calperiodos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calperiodo)))
            .andExpect(status().isCreated());

        // Validate the Calperiodo in the database
        List<Calperiodo> calperiodoList = calperiodoRepository.findAll();
        assertThat(calperiodoList).hasSize(databaseSizeBeforeCreate + 1);
        Calperiodo testCalperiodo = calperiodoList.get(calperiodoList.size() - 1);
        assertThat(testCalperiodo.getnCalper()).isEqualTo(DEFAULT_N_CALPER);
        assertThat(testCalperiodo.getnCalper2()).isEqualTo(DEFAULT_N_CALPER_2);
        assertThat(testCalperiodo.getnNumper()).isEqualTo(DEFAULT_N_NUMPER);
        assertThat(testCalperiodo.gettFecini()).isEqualTo(DEFAULT_T_FECINI);
        assertThat(testCalperiodo.gettFecfin()).isEqualTo(DEFAULT_T_FECFIN);
        assertThat(testCalperiodo.getnTnocomput()).isEqualTo(DEFAULT_N_TNOCOMPUT);
        assertThat(testCalperiodo.getnTcomput()).isEqualTo(DEFAULT_N_TCOMPUT);
        assertThat(testCalperiodo.getnCodhijo1()).isEqualTo(DEFAULT_N_CODHIJO_1);
        assertThat(testCalperiodo.getnCodhijo2()).isEqualTo(DEFAULT_N_CODHIJO_2);
        assertThat(testCalperiodo.getnDgozados()).isEqualTo(DEFAULT_N_DGOZADOS);
        assertThat(testCalperiodo.getnDadeudos()).isEqualTo(DEFAULT_N_DADEUDOS);
        assertThat(testCalperiodo.getnAnobase()).isEqualTo(DEFAULT_N_ANOBASE);
        assertThat(testCalperiodo.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testCalperiodo.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testCalperiodo.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testCalperiodo.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testCalperiodo.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testCalperiodo.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testCalperiodo.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Calperiodo in Elasticsearch
        Calperiodo calperiodoEs = calperiodoSearchRepository.findOne(testCalperiodo.getId());
        assertThat(calperiodoEs).isEqualToComparingFieldByField(testCalperiodo);
    }

    @Test
    @Transactional
    public void createCalperiodoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = calperiodoRepository.findAll().size();

        // Create the Calperiodo with an existing ID
        calperiodo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalperiodoMockMvc.perform(post("/api/calperiodos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calperiodo)))
            .andExpect(status().isBadRequest());

        // Validate the Calperiodo in the database
        List<Calperiodo> calperiodoList = calperiodoRepository.findAll();
        assertThat(calperiodoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknNumperIsRequired() throws Exception {
        int databaseSizeBeforeTest = calperiodoRepository.findAll().size();
        // set the field null
        calperiodo.setnNumper(null);

        // Create the Calperiodo, which fails.

        restCalperiodoMockMvc.perform(post("/api/calperiodos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calperiodo)))
            .andExpect(status().isBadRequest());

        List<Calperiodo> calperiodoList = calperiodoRepository.findAll();
        assertThat(calperiodoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFeciniIsRequired() throws Exception {
        int databaseSizeBeforeTest = calperiodoRepository.findAll().size();
        // set the field null
        calperiodo.settFecini(null);

        // Create the Calperiodo, which fails.

        restCalperiodoMockMvc.perform(post("/api/calperiodos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calperiodo)))
            .andExpect(status().isBadRequest());

        List<Calperiodo> calperiodoList = calperiodoRepository.findAll();
        assertThat(calperiodoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecfinIsRequired() throws Exception {
        int databaseSizeBeforeTest = calperiodoRepository.findAll().size();
        // set the field null
        calperiodo.settFecfin(null);

        // Create the Calperiodo, which fails.

        restCalperiodoMockMvc.perform(post("/api/calperiodos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calperiodo)))
            .andExpect(status().isBadRequest());

        List<Calperiodo> calperiodoList = calperiodoRepository.findAll();
        assertThat(calperiodoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = calperiodoRepository.findAll().size();
        // set the field null
        calperiodo.setnUsuareg(null);

        // Create the Calperiodo, which fails.

        restCalperiodoMockMvc.perform(post("/api/calperiodos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calperiodo)))
            .andExpect(status().isBadRequest());

        List<Calperiodo> calperiodoList = calperiodoRepository.findAll();
        assertThat(calperiodoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = calperiodoRepository.findAll().size();
        // set the field null
        calperiodo.settFecreg(null);

        // Create the Calperiodo, which fails.

        restCalperiodoMockMvc.perform(post("/api/calperiodos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calperiodo)))
            .andExpect(status().isBadRequest());

        List<Calperiodo> calperiodoList = calperiodoRepository.findAll();
        assertThat(calperiodoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = calperiodoRepository.findAll().size();
        // set the field null
        calperiodo.setnFlgactivo(null);

        // Create the Calperiodo, which fails.

        restCalperiodoMockMvc.perform(post("/api/calperiodos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calperiodo)))
            .andExpect(status().isBadRequest());

        List<Calperiodo> calperiodoList = calperiodoRepository.findAll();
        assertThat(calperiodoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = calperiodoRepository.findAll().size();
        // set the field null
        calperiodo.setnSedereg(null);

        // Create the Calperiodo, which fails.

        restCalperiodoMockMvc.perform(post("/api/calperiodos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calperiodo)))
            .andExpect(status().isBadRequest());

        List<Calperiodo> calperiodoList = calperiodoRepository.findAll();
        assertThat(calperiodoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCalperiodos() throws Exception {
        // Initialize the database
        calperiodoRepository.saveAndFlush(calperiodo);

        // Get all the calperiodoList
        restCalperiodoMockMvc.perform(get("/api/calperiodos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calperiodo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nCalper").value(hasItem(DEFAULT_N_CALPER.intValue())))
            .andExpect(jsonPath("$.[*].nCalper2").value(hasItem(DEFAULT_N_CALPER_2.intValue())))
            .andExpect(jsonPath("$.[*].nNumper").value(hasItem(DEFAULT_N_NUMPER)))
            .andExpect(jsonPath("$.[*].tFecini").value(hasItem(DEFAULT_T_FECINI.toString())))
            .andExpect(jsonPath("$.[*].tFecfin").value(hasItem(DEFAULT_T_FECFIN.toString())))
            .andExpect(jsonPath("$.[*].nTnocomput").value(hasItem(DEFAULT_N_TNOCOMPUT)))
            .andExpect(jsonPath("$.[*].nTcomput").value(hasItem(DEFAULT_N_TCOMPUT)))
            .andExpect(jsonPath("$.[*].nCodhijo1").value(hasItem(DEFAULT_N_CODHIJO_1.intValue())))
            .andExpect(jsonPath("$.[*].nCodhijo2").value(hasItem(DEFAULT_N_CODHIJO_2.intValue())))
            .andExpect(jsonPath("$.[*].nDgozados").value(hasItem(DEFAULT_N_DGOZADOS)))
            .andExpect(jsonPath("$.[*].nDadeudos").value(hasItem(DEFAULT_N_DADEUDOS)))
            .andExpect(jsonPath("$.[*].nAnobase").value(hasItem(DEFAULT_N_ANOBASE)))
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
    public void getCalperiodo() throws Exception {
        // Initialize the database
        calperiodoRepository.saveAndFlush(calperiodo);

        // Get the calperiodo
        restCalperiodoMockMvc.perform(get("/api/calperiodos/{id}", calperiodo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(calperiodo.getId().intValue()))
            .andExpect(jsonPath("$.nCalper").value(DEFAULT_N_CALPER.intValue()))
            .andExpect(jsonPath("$.nCalper2").value(DEFAULT_N_CALPER_2.intValue()))
            .andExpect(jsonPath("$.nNumper").value(DEFAULT_N_NUMPER))
            .andExpect(jsonPath("$.tFecini").value(DEFAULT_T_FECINI.toString()))
            .andExpect(jsonPath("$.tFecfin").value(DEFAULT_T_FECFIN.toString()))
            .andExpect(jsonPath("$.nTnocomput").value(DEFAULT_N_TNOCOMPUT))
            .andExpect(jsonPath("$.nTcomput").value(DEFAULT_N_TCOMPUT))
            .andExpect(jsonPath("$.nCodhijo1").value(DEFAULT_N_CODHIJO_1.intValue()))
            .andExpect(jsonPath("$.nCodhijo2").value(DEFAULT_N_CODHIJO_2.intValue()))
            .andExpect(jsonPath("$.nDgozados").value(DEFAULT_N_DGOZADOS))
            .andExpect(jsonPath("$.nDadeudos").value(DEFAULT_N_DADEUDOS))
            .andExpect(jsonPath("$.nAnobase").value(DEFAULT_N_ANOBASE))
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
    public void getNonExistingCalperiodo() throws Exception {
        // Get the calperiodo
        restCalperiodoMockMvc.perform(get("/api/calperiodos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCalperiodo() throws Exception {
        // Initialize the database
        calperiodoRepository.saveAndFlush(calperiodo);
        calperiodoSearchRepository.save(calperiodo);
        int databaseSizeBeforeUpdate = calperiodoRepository.findAll().size();

        // Update the calperiodo
        Calperiodo updatedCalperiodo = calperiodoRepository.findOne(calperiodo.getId());
        updatedCalperiodo
            .nCalper(UPDATED_N_CALPER)
            .nCalper2(UPDATED_N_CALPER_2)
            .nNumper(UPDATED_N_NUMPER)
            .tFecini(UPDATED_T_FECINI)
            .tFecfin(UPDATED_T_FECFIN)
            .nTnocomput(UPDATED_N_TNOCOMPUT)
            .nTcomput(UPDATED_N_TCOMPUT)
            .nCodhijo1(UPDATED_N_CODHIJO_1)
            .nCodhijo2(UPDATED_N_CODHIJO_2)
            .nDgozados(UPDATED_N_DGOZADOS)
            .nDadeudos(UPDATED_N_DADEUDOS)
            .nAnobase(UPDATED_N_ANOBASE)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restCalperiodoMockMvc.perform(put("/api/calperiodos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCalperiodo)))
            .andExpect(status().isOk());

        // Validate the Calperiodo in the database
        List<Calperiodo> calperiodoList = calperiodoRepository.findAll();
        assertThat(calperiodoList).hasSize(databaseSizeBeforeUpdate);
        Calperiodo testCalperiodo = calperiodoList.get(calperiodoList.size() - 1);
        assertThat(testCalperiodo.getnCalper()).isEqualTo(UPDATED_N_CALPER);
        assertThat(testCalperiodo.getnCalper2()).isEqualTo(UPDATED_N_CALPER_2);
        assertThat(testCalperiodo.getnNumper()).isEqualTo(UPDATED_N_NUMPER);
        assertThat(testCalperiodo.gettFecini()).isEqualTo(UPDATED_T_FECINI);
        assertThat(testCalperiodo.gettFecfin()).isEqualTo(UPDATED_T_FECFIN);
        assertThat(testCalperiodo.getnTnocomput()).isEqualTo(UPDATED_N_TNOCOMPUT);
        assertThat(testCalperiodo.getnTcomput()).isEqualTo(UPDATED_N_TCOMPUT);
        assertThat(testCalperiodo.getnCodhijo1()).isEqualTo(UPDATED_N_CODHIJO_1);
        assertThat(testCalperiodo.getnCodhijo2()).isEqualTo(UPDATED_N_CODHIJO_2);
        assertThat(testCalperiodo.getnDgozados()).isEqualTo(UPDATED_N_DGOZADOS);
        assertThat(testCalperiodo.getnDadeudos()).isEqualTo(UPDATED_N_DADEUDOS);
        assertThat(testCalperiodo.getnAnobase()).isEqualTo(UPDATED_N_ANOBASE);
        assertThat(testCalperiodo.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testCalperiodo.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testCalperiodo.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testCalperiodo.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testCalperiodo.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testCalperiodo.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testCalperiodo.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Calperiodo in Elasticsearch
        Calperiodo calperiodoEs = calperiodoSearchRepository.findOne(testCalperiodo.getId());
        assertThat(calperiodoEs).isEqualToComparingFieldByField(testCalperiodo);
    }

    @Test
    @Transactional
    public void updateNonExistingCalperiodo() throws Exception {
        int databaseSizeBeforeUpdate = calperiodoRepository.findAll().size();

        // Create the Calperiodo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCalperiodoMockMvc.perform(put("/api/calperiodos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calperiodo)))
            .andExpect(status().isCreated());

        // Validate the Calperiodo in the database
        List<Calperiodo> calperiodoList = calperiodoRepository.findAll();
        assertThat(calperiodoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCalperiodo() throws Exception {
        // Initialize the database
        calperiodoRepository.saveAndFlush(calperiodo);
        calperiodoSearchRepository.save(calperiodo);
        int databaseSizeBeforeDelete = calperiodoRepository.findAll().size();

        // Get the calperiodo
        restCalperiodoMockMvc.perform(delete("/api/calperiodos/{id}", calperiodo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean calperiodoExistsInEs = calperiodoSearchRepository.exists(calperiodo.getId());
        assertThat(calperiodoExistsInEs).isFalse();

        // Validate the database is empty
        List<Calperiodo> calperiodoList = calperiodoRepository.findAll();
        assertThat(calperiodoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCalperiodo() throws Exception {
        // Initialize the database
        calperiodoRepository.saveAndFlush(calperiodo);
        calperiodoSearchRepository.save(calperiodo);

        // Search the calperiodo
        restCalperiodoMockMvc.perform(get("/api/_search/calperiodos?query=id:" + calperiodo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calperiodo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nCalper").value(hasItem(DEFAULT_N_CALPER.intValue())))
            .andExpect(jsonPath("$.[*].nCalper2").value(hasItem(DEFAULT_N_CALPER_2.intValue())))
            .andExpect(jsonPath("$.[*].nNumper").value(hasItem(DEFAULT_N_NUMPER)))
            .andExpect(jsonPath("$.[*].tFecini").value(hasItem(DEFAULT_T_FECINI.toString())))
            .andExpect(jsonPath("$.[*].tFecfin").value(hasItem(DEFAULT_T_FECFIN.toString())))
            .andExpect(jsonPath("$.[*].nTnocomput").value(hasItem(DEFAULT_N_TNOCOMPUT)))
            .andExpect(jsonPath("$.[*].nTcomput").value(hasItem(DEFAULT_N_TCOMPUT)))
            .andExpect(jsonPath("$.[*].nCodhijo1").value(hasItem(DEFAULT_N_CODHIJO_1.intValue())))
            .andExpect(jsonPath("$.[*].nCodhijo2").value(hasItem(DEFAULT_N_CODHIJO_2.intValue())))
            .andExpect(jsonPath("$.[*].nDgozados").value(hasItem(DEFAULT_N_DGOZADOS)))
            .andExpect(jsonPath("$.[*].nDadeudos").value(hasItem(DEFAULT_N_DADEUDOS)))
            .andExpect(jsonPath("$.[*].nAnobase").value(hasItem(DEFAULT_N_ANOBASE)))
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
        TestUtil.equalsVerifier(Calperiodo.class);
        Calperiodo calperiodo1 = new Calperiodo();
        calperiodo1.setId(1L);
        Calperiodo calperiodo2 = new Calperiodo();
        calperiodo2.setId(calperiodo1.getId());
        assertThat(calperiodo1).isEqualTo(calperiodo2);
        calperiodo2.setId(2L);
        assertThat(calperiodo1).isNotEqualTo(calperiodo2);
        calperiodo1.setId(null);
        assertThat(calperiodo1).isNotEqualTo(calperiodo2);
    }
}
