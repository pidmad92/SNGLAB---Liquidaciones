package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Tippersona;
import pe.gob.trabajo.repository.TippersonaRepository;
import pe.gob.trabajo.repository.search.TippersonaSearchRepository;
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
 * Test class for the TippersonaResource REST controller.
 *
 * @see TippersonaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class TippersonaResourceIntTest {

    private static final String DEFAULT_V_DESTIPPER = "AAAAAAAAAA";
    private static final String UPDATED_V_DESTIPPER = "BBBBBBBBBB";

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
    private TippersonaRepository tippersonaRepository;

    @Autowired
    private TippersonaSearchRepository tippersonaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTippersonaMockMvc;

    private Tippersona tippersona;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TippersonaResource tippersonaResource = new TippersonaResource(tippersonaRepository, tippersonaSearchRepository);
        this.restTippersonaMockMvc = MockMvcBuilders.standaloneSetup(tippersonaResource)
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
    public static Tippersona createEntity(EntityManager em) {
        Tippersona tippersona = new Tippersona()
            .vDestipper(DEFAULT_V_DESTIPPER)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return tippersona;
    }

    @Before
    public void initTest() {
        tippersonaSearchRepository.deleteAll();
        tippersona = createEntity(em);
    }

    @Test
    @Transactional
    public void createTippersona() throws Exception {
        int databaseSizeBeforeCreate = tippersonaRepository.findAll().size();

        // Create the Tippersona
        restTippersonaMockMvc.perform(post("/api/tippersonas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tippersona)))
            .andExpect(status().isCreated());

        // Validate the Tippersona in the database
        List<Tippersona> tippersonaList = tippersonaRepository.findAll();
        assertThat(tippersonaList).hasSize(databaseSizeBeforeCreate + 1);
        Tippersona testTippersona = tippersonaList.get(tippersonaList.size() - 1);
        assertThat(testTippersona.getvDestipper()).isEqualTo(DEFAULT_V_DESTIPPER);
        assertThat(testTippersona.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testTippersona.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testTippersona.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testTippersona.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testTippersona.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testTippersona.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testTippersona.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Tippersona in Elasticsearch
        Tippersona tippersonaEs = tippersonaSearchRepository.findOne(testTippersona.getId());
        assertThat(tippersonaEs).isEqualToComparingFieldByField(testTippersona);
    }

    @Test
    @Transactional
    public void createTippersonaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tippersonaRepository.findAll().size();

        // Create the Tippersona with an existing ID
        tippersona.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTippersonaMockMvc.perform(post("/api/tippersonas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tippersona)))
            .andExpect(status().isBadRequest());

        // Validate the Tippersona in the database
        List<Tippersona> tippersonaList = tippersonaRepository.findAll();
        assertThat(tippersonaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDestipperIsRequired() throws Exception {
        int databaseSizeBeforeTest = tippersonaRepository.findAll().size();
        // set the field null
        tippersona.setvDestipper(null);

        // Create the Tippersona, which fails.

        restTippersonaMockMvc.perform(post("/api/tippersonas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tippersona)))
            .andExpect(status().isBadRequest());

        List<Tippersona> tippersonaList = tippersonaRepository.findAll();
        assertThat(tippersonaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tippersonaRepository.findAll().size();
        // set the field null
        tippersona.setnUsuareg(null);

        // Create the Tippersona, which fails.

        restTippersonaMockMvc.perform(post("/api/tippersonas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tippersona)))
            .andExpect(status().isBadRequest());

        List<Tippersona> tippersonaList = tippersonaRepository.findAll();
        assertThat(tippersonaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tippersonaRepository.findAll().size();
        // set the field null
        tippersona.settFecreg(null);

        // Create the Tippersona, which fails.

        restTippersonaMockMvc.perform(post("/api/tippersonas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tippersona)))
            .andExpect(status().isBadRequest());

        List<Tippersona> tippersonaList = tippersonaRepository.findAll();
        assertThat(tippersonaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tippersonaRepository.findAll().size();
        // set the field null
        tippersona.setnFlgactivo(null);

        // Create the Tippersona, which fails.

        restTippersonaMockMvc.perform(post("/api/tippersonas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tippersona)))
            .andExpect(status().isBadRequest());

        List<Tippersona> tippersonaList = tippersonaRepository.findAll();
        assertThat(tippersonaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = tippersonaRepository.findAll().size();
        // set the field null
        tippersona.setnSedereg(null);

        // Create the Tippersona, which fails.

        restTippersonaMockMvc.perform(post("/api/tippersonas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tippersona)))
            .andExpect(status().isBadRequest());

        List<Tippersona> tippersonaList = tippersonaRepository.findAll();
        assertThat(tippersonaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTippersonas() throws Exception {
        // Initialize the database
        tippersonaRepository.saveAndFlush(tippersona);

        // Get all the tippersonaList
        restTippersonaMockMvc.perform(get("/api/tippersonas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tippersona.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDestipper").value(hasItem(DEFAULT_V_DESTIPPER.toString())))
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
    public void getTippersona() throws Exception {
        // Initialize the database
        tippersonaRepository.saveAndFlush(tippersona);

        // Get the tippersona
        restTippersonaMockMvc.perform(get("/api/tippersonas/{id}", tippersona.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tippersona.getId().intValue()))
            .andExpect(jsonPath("$.vDestipper").value(DEFAULT_V_DESTIPPER.toString()))
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
    public void getNonExistingTippersona() throws Exception {
        // Get the tippersona
        restTippersonaMockMvc.perform(get("/api/tippersonas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTippersona() throws Exception {
        // Initialize the database
        tippersonaRepository.saveAndFlush(tippersona);
        tippersonaSearchRepository.save(tippersona);
        int databaseSizeBeforeUpdate = tippersonaRepository.findAll().size();

        // Update the tippersona
        Tippersona updatedTippersona = tippersonaRepository.findOne(tippersona.getId());
        updatedTippersona
            .vDestipper(UPDATED_V_DESTIPPER)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restTippersonaMockMvc.perform(put("/api/tippersonas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTippersona)))
            .andExpect(status().isOk());

        // Validate the Tippersona in the database
        List<Tippersona> tippersonaList = tippersonaRepository.findAll();
        assertThat(tippersonaList).hasSize(databaseSizeBeforeUpdate);
        Tippersona testTippersona = tippersonaList.get(tippersonaList.size() - 1);
        assertThat(testTippersona.getvDestipper()).isEqualTo(UPDATED_V_DESTIPPER);
        assertThat(testTippersona.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testTippersona.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testTippersona.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testTippersona.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testTippersona.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testTippersona.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testTippersona.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Tippersona in Elasticsearch
        Tippersona tippersonaEs = tippersonaSearchRepository.findOne(testTippersona.getId());
        assertThat(tippersonaEs).isEqualToComparingFieldByField(testTippersona);
    }

    @Test
    @Transactional
    public void updateNonExistingTippersona() throws Exception {
        int databaseSizeBeforeUpdate = tippersonaRepository.findAll().size();

        // Create the Tippersona

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTippersonaMockMvc.perform(put("/api/tippersonas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tippersona)))
            .andExpect(status().isCreated());

        // Validate the Tippersona in the database
        List<Tippersona> tippersonaList = tippersonaRepository.findAll();
        assertThat(tippersonaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTippersona() throws Exception {
        // Initialize the database
        tippersonaRepository.saveAndFlush(tippersona);
        tippersonaSearchRepository.save(tippersona);
        int databaseSizeBeforeDelete = tippersonaRepository.findAll().size();

        // Get the tippersona
        restTippersonaMockMvc.perform(delete("/api/tippersonas/{id}", tippersona.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tippersonaExistsInEs = tippersonaSearchRepository.exists(tippersona.getId());
        assertThat(tippersonaExistsInEs).isFalse();

        // Validate the database is empty
        List<Tippersona> tippersonaList = tippersonaRepository.findAll();
        assertThat(tippersonaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTippersona() throws Exception {
        // Initialize the database
        tippersonaRepository.saveAndFlush(tippersona);
        tippersonaSearchRepository.save(tippersona);

        // Search the tippersona
        restTippersonaMockMvc.perform(get("/api/_search/tippersonas?query=id:" + tippersona.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tippersona.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDestipper").value(hasItem(DEFAULT_V_DESTIPPER.toString())))
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
        TestUtil.equalsVerifier(Tippersona.class);
        Tippersona tippersona1 = new Tippersona();
        tippersona1.setId(1L);
        Tippersona tippersona2 = new Tippersona();
        tippersona2.setId(tippersona1.getId());
        assertThat(tippersona1).isEqualTo(tippersona2);
        tippersona2.setId(2L);
        assertThat(tippersona1).isNotEqualTo(tippersona2);
        tippersona1.setId(null);
        assertThat(tippersona1).isNotEqualTo(tippersona2);
    }
}
