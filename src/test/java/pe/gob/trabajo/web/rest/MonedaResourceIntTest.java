package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Moneda;
import pe.gob.trabajo.repository.MonedaRepository;
import pe.gob.trabajo.repository.search.MonedaSearchRepository;
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

import static pe.gob.trabajo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MonedaResource REST controller.
 *
 * @see MonedaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class MonedaResourceIntTest {

    private static final String DEFAULT_V_NOMMONEDA = "AAAAAAAAAA";
    private static final String UPDATED_V_NOMMONEDA = "BBBBBBBBBB";

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
    private MonedaRepository monedaRepository;

    @Autowired
    private MonedaSearchRepository monedaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMonedaMockMvc;

    private Moneda moneda;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MonedaResource monedaResource = new MonedaResource(monedaRepository, monedaSearchRepository);
        this.restMonedaMockMvc = MockMvcBuilders.standaloneSetup(monedaResource)
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
    public static Moneda createEntity(EntityManager em) {
        Moneda moneda = new Moneda()
            .vNommoneda(DEFAULT_V_NOMMONEDA)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return moneda;
    }

    @Before
    public void initTest() {
        monedaSearchRepository.deleteAll();
        moneda = createEntity(em);
    }

    @Test
    @Transactional
    public void createMoneda() throws Exception {
        int databaseSizeBeforeCreate = monedaRepository.findAll().size();

        // Create the Moneda
        restMonedaMockMvc.perform(post("/api/monedas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moneda)))
            .andExpect(status().isCreated());

        // Validate the Moneda in the database
        List<Moneda> monedaList = monedaRepository.findAll();
        assertThat(monedaList).hasSize(databaseSizeBeforeCreate + 1);
        Moneda testMoneda = monedaList.get(monedaList.size() - 1);
        assertThat(testMoneda.getvNommoneda()).isEqualTo(DEFAULT_V_NOMMONEDA);
        assertThat(testMoneda.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testMoneda.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testMoneda.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testMoneda.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testMoneda.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testMoneda.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testMoneda.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Moneda in Elasticsearch
        Moneda monedaEs = monedaSearchRepository.findOne(testMoneda.getId());
        assertThat(monedaEs).isEqualToComparingFieldByField(testMoneda);
    }

    @Test
    @Transactional
    public void createMonedaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = monedaRepository.findAll().size();

        // Create the Moneda with an existing ID
        moneda.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonedaMockMvc.perform(post("/api/monedas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moneda)))
            .andExpect(status().isBadRequest());

        // Validate the Moneda in the database
        List<Moneda> monedaList = monedaRepository.findAll();
        assertThat(monedaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvNommonedaIsRequired() throws Exception {
        int databaseSizeBeforeTest = monedaRepository.findAll().size();
        // set the field null
        moneda.setvNommoneda(null);

        // Create the Moneda, which fails.

        restMonedaMockMvc.perform(post("/api/monedas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moneda)))
            .andExpect(status().isBadRequest());

        List<Moneda> monedaList = monedaRepository.findAll();
        assertThat(monedaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = monedaRepository.findAll().size();
        // set the field null
        moneda.setnUsuareg(null);

        // Create the Moneda, which fails.

        restMonedaMockMvc.perform(post("/api/monedas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moneda)))
            .andExpect(status().isBadRequest());

        List<Moneda> monedaList = monedaRepository.findAll();
        assertThat(monedaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = monedaRepository.findAll().size();
        // set the field null
        moneda.settFecreg(null);

        // Create the Moneda, which fails.

        restMonedaMockMvc.perform(post("/api/monedas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moneda)))
            .andExpect(status().isBadRequest());

        List<Moneda> monedaList = monedaRepository.findAll();
        assertThat(monedaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = monedaRepository.findAll().size();
        // set the field null
        moneda.setnFlgactivo(null);

        // Create the Moneda, which fails.

        restMonedaMockMvc.perform(post("/api/monedas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moneda)))
            .andExpect(status().isBadRequest());

        List<Moneda> monedaList = monedaRepository.findAll();
        assertThat(monedaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = monedaRepository.findAll().size();
        // set the field null
        moneda.setnSedereg(null);

        // Create the Moneda, which fails.

        restMonedaMockMvc.perform(post("/api/monedas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moneda)))
            .andExpect(status().isBadRequest());

        List<Moneda> monedaList = monedaRepository.findAll();
        assertThat(monedaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMonedas() throws Exception {
        // Initialize the database
        monedaRepository.saveAndFlush(moneda);

        // Get all the monedaList
        restMonedaMockMvc.perform(get("/api/monedas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneda.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNommoneda").value(hasItem(DEFAULT_V_NOMMONEDA.toString())))
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
    public void getMoneda() throws Exception {
        // Initialize the database
        monedaRepository.saveAndFlush(moneda);

        // Get the moneda
        restMonedaMockMvc.perform(get("/api/monedas/{id}", moneda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(moneda.getId().intValue()))
            .andExpect(jsonPath("$.vNommoneda").value(DEFAULT_V_NOMMONEDA.toString()))
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
    public void getNonExistingMoneda() throws Exception {
        // Get the moneda
        restMonedaMockMvc.perform(get("/api/monedas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMoneda() throws Exception {
        // Initialize the database
        monedaRepository.saveAndFlush(moneda);
        monedaSearchRepository.save(moneda);
        int databaseSizeBeforeUpdate = monedaRepository.findAll().size();

        // Update the moneda
        Moneda updatedMoneda = monedaRepository.findOne(moneda.getId());
        updatedMoneda
            .vNommoneda(UPDATED_V_NOMMONEDA)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restMonedaMockMvc.perform(put("/api/monedas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMoneda)))
            .andExpect(status().isOk());

        // Validate the Moneda in the database
        List<Moneda> monedaList = monedaRepository.findAll();
        assertThat(monedaList).hasSize(databaseSizeBeforeUpdate);
        Moneda testMoneda = monedaList.get(monedaList.size() - 1);
        assertThat(testMoneda.getvNommoneda()).isEqualTo(UPDATED_V_NOMMONEDA);
        assertThat(testMoneda.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testMoneda.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testMoneda.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testMoneda.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testMoneda.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testMoneda.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testMoneda.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Moneda in Elasticsearch
        Moneda monedaEs = monedaSearchRepository.findOne(testMoneda.getId());
        assertThat(monedaEs).isEqualToComparingFieldByField(testMoneda);
    }

    @Test
    @Transactional
    public void updateNonExistingMoneda() throws Exception {
        int databaseSizeBeforeUpdate = monedaRepository.findAll().size();

        // Create the Moneda

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMonedaMockMvc.perform(put("/api/monedas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moneda)))
            .andExpect(status().isCreated());

        // Validate the Moneda in the database
        List<Moneda> monedaList = monedaRepository.findAll();
        assertThat(monedaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMoneda() throws Exception {
        // Initialize the database
        monedaRepository.saveAndFlush(moneda);
        monedaSearchRepository.save(moneda);
        int databaseSizeBeforeDelete = monedaRepository.findAll().size();

        // Get the moneda
        restMonedaMockMvc.perform(delete("/api/monedas/{id}", moneda.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean monedaExistsInEs = monedaSearchRepository.exists(moneda.getId());
        assertThat(monedaExistsInEs).isFalse();

        // Validate the database is empty
        List<Moneda> monedaList = monedaRepository.findAll();
        assertThat(monedaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMoneda() throws Exception {
        // Initialize the database
        monedaRepository.saveAndFlush(moneda);
        monedaSearchRepository.save(moneda);

        // Search the moneda
        restMonedaMockMvc.perform(get("/api/_search/monedas?query=id:" + moneda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneda.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNommoneda").value(hasItem(DEFAULT_V_NOMMONEDA.toString())))
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
        TestUtil.equalsVerifier(Moneda.class);
        Moneda moneda1 = new Moneda();
        moneda1.setId(1L);
        Moneda moneda2 = new Moneda();
        moneda2.setId(moneda1.getId());
        assertThat(moneda1).isEqualTo(moneda2);
        moneda2.setId(2L);
        assertThat(moneda1).isNotEqualTo(moneda2);
        moneda1.setId(null);
        assertThat(moneda1).isNotEqualTo(moneda2);
    }
}
