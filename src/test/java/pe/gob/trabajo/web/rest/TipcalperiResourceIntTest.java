package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Tipcalperi;
import pe.gob.trabajo.repository.TipcalperiRepository;
import pe.gob.trabajo.repository.search.TipcalperiSearchRepository;
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
 * Test class for the TipcalperiResource REST controller.
 *
 * @see TipcalperiResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class TipcalperiResourceIntTest {

    private static final String DEFAULT_V_NOMTIPCAL = "AAAAAAAAAA";
    private static final String UPDATED_V_NOMTIPCAL = "BBBBBBBBBB";

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
    private TipcalperiRepository tipcalperiRepository;

    @Autowired
    private TipcalperiSearchRepository tipcalperiSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTipcalperiMockMvc;

    private Tipcalperi tipcalperi;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipcalperiResource tipcalperiResource = new TipcalperiResource(tipcalperiRepository, tipcalperiSearchRepository);
        this.restTipcalperiMockMvc = MockMvcBuilders.standaloneSetup(tipcalperiResource)
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
    public static Tipcalperi createEntity(EntityManager em) {
        Tipcalperi tipcalperi = new Tipcalperi()
            .vNomtipcal(DEFAULT_V_NOMTIPCAL)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return tipcalperi;
    }

    @Before
    public void initTest() {
        tipcalperiSearchRepository.deleteAll();
        tipcalperi = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipcalperi() throws Exception {
        int databaseSizeBeforeCreate = tipcalperiRepository.findAll().size();

        // Create the Tipcalperi
        restTipcalperiMockMvc.perform(post("/api/tipcalperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalperi)))
            .andExpect(status().isCreated());

        // Validate the Tipcalperi in the database
        List<Tipcalperi> tipcalperiList = tipcalperiRepository.findAll();
        assertThat(tipcalperiList).hasSize(databaseSizeBeforeCreate + 1);
        Tipcalperi testTipcalperi = tipcalperiList.get(tipcalperiList.size() - 1);
        assertThat(testTipcalperi.getvNomtipcal()).isEqualTo(DEFAULT_V_NOMTIPCAL);
        assertThat(testTipcalperi.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testTipcalperi.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testTipcalperi.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testTipcalperi.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testTipcalperi.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testTipcalperi.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testTipcalperi.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Tipcalperi in Elasticsearch
        Tipcalperi tipcalperiEs = tipcalperiSearchRepository.findOne(testTipcalperi.getId());
        assertThat(tipcalperiEs).isEqualToComparingFieldByField(testTipcalperi);
    }

    @Test
    @Transactional
    public void createTipcalperiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipcalperiRepository.findAll().size();

        // Create the Tipcalperi with an existing ID
        tipcalperi.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipcalperiMockMvc.perform(post("/api/tipcalperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalperi)))
            .andExpect(status().isBadRequest());

        // Validate the Tipcalperi in the database
        List<Tipcalperi> tipcalperiList = tipcalperiRepository.findAll();
        assertThat(tipcalperiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvNomtipcalIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipcalperiRepository.findAll().size();
        // set the field null
        tipcalperi.setvNomtipcal(null);

        // Create the Tipcalperi, which fails.

        restTipcalperiMockMvc.perform(post("/api/tipcalperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalperi)))
            .andExpect(status().isBadRequest());

        List<Tipcalperi> tipcalperiList = tipcalperiRepository.findAll();
        assertThat(tipcalperiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipcalperiRepository.findAll().size();
        // set the field null
        tipcalperi.setnUsuareg(null);

        // Create the Tipcalperi, which fails.

        restTipcalperiMockMvc.perform(post("/api/tipcalperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalperi)))
            .andExpect(status().isBadRequest());

        List<Tipcalperi> tipcalperiList = tipcalperiRepository.findAll();
        assertThat(tipcalperiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipcalperiRepository.findAll().size();
        // set the field null
        tipcalperi.settFecreg(null);

        // Create the Tipcalperi, which fails.

        restTipcalperiMockMvc.perform(post("/api/tipcalperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalperi)))
            .andExpect(status().isBadRequest());

        List<Tipcalperi> tipcalperiList = tipcalperiRepository.findAll();
        assertThat(tipcalperiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipcalperiRepository.findAll().size();
        // set the field null
        tipcalperi.setnFlgactivo(null);

        // Create the Tipcalperi, which fails.

        restTipcalperiMockMvc.perform(post("/api/tipcalperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalperi)))
            .andExpect(status().isBadRequest());

        List<Tipcalperi> tipcalperiList = tipcalperiRepository.findAll();
        assertThat(tipcalperiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipcalperiRepository.findAll().size();
        // set the field null
        tipcalperi.setnSedereg(null);

        // Create the Tipcalperi, which fails.

        restTipcalperiMockMvc.perform(post("/api/tipcalperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalperi)))
            .andExpect(status().isBadRequest());

        List<Tipcalperi> tipcalperiList = tipcalperiRepository.findAll();
        assertThat(tipcalperiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipcalperis() throws Exception {
        // Initialize the database
        tipcalperiRepository.saveAndFlush(tipcalperi);

        // Get all the tipcalperiList
        restTipcalperiMockMvc.perform(get("/api/tipcalperis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipcalperi.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNomtipcal").value(hasItem(DEFAULT_V_NOMTIPCAL.toString())))
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
    public void getTipcalperi() throws Exception {
        // Initialize the database
        tipcalperiRepository.saveAndFlush(tipcalperi);

        // Get the tipcalperi
        restTipcalperiMockMvc.perform(get("/api/tipcalperis/{id}", tipcalperi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipcalperi.getId().intValue()))
            .andExpect(jsonPath("$.vNomtipcal").value(DEFAULT_V_NOMTIPCAL.toString()))
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
    public void getNonExistingTipcalperi() throws Exception {
        // Get the tipcalperi
        restTipcalperiMockMvc.perform(get("/api/tipcalperis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipcalperi() throws Exception {
        // Initialize the database
        tipcalperiRepository.saveAndFlush(tipcalperi);
        tipcalperiSearchRepository.save(tipcalperi);
        int databaseSizeBeforeUpdate = tipcalperiRepository.findAll().size();

        // Update the tipcalperi
        Tipcalperi updatedTipcalperi = tipcalperiRepository.findOne(tipcalperi.getId());
        updatedTipcalperi
            .vNomtipcal(UPDATED_V_NOMTIPCAL)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restTipcalperiMockMvc.perform(put("/api/tipcalperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipcalperi)))
            .andExpect(status().isOk());

        // Validate the Tipcalperi in the database
        List<Tipcalperi> tipcalperiList = tipcalperiRepository.findAll();
        assertThat(tipcalperiList).hasSize(databaseSizeBeforeUpdate);
        Tipcalperi testTipcalperi = tipcalperiList.get(tipcalperiList.size() - 1);
        assertThat(testTipcalperi.getvNomtipcal()).isEqualTo(UPDATED_V_NOMTIPCAL);
        assertThat(testTipcalperi.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testTipcalperi.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testTipcalperi.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testTipcalperi.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testTipcalperi.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testTipcalperi.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testTipcalperi.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Tipcalperi in Elasticsearch
        Tipcalperi tipcalperiEs = tipcalperiSearchRepository.findOne(testTipcalperi.getId());
        assertThat(tipcalperiEs).isEqualToComparingFieldByField(testTipcalperi);
    }

    @Test
    @Transactional
    public void updateNonExistingTipcalperi() throws Exception {
        int databaseSizeBeforeUpdate = tipcalperiRepository.findAll().size();

        // Create the Tipcalperi

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTipcalperiMockMvc.perform(put("/api/tipcalperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalperi)))
            .andExpect(status().isCreated());

        // Validate the Tipcalperi in the database
        List<Tipcalperi> tipcalperiList = tipcalperiRepository.findAll();
        assertThat(tipcalperiList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTipcalperi() throws Exception {
        // Initialize the database
        tipcalperiRepository.saveAndFlush(tipcalperi);
        tipcalperiSearchRepository.save(tipcalperi);
        int databaseSizeBeforeDelete = tipcalperiRepository.findAll().size();

        // Get the tipcalperi
        restTipcalperiMockMvc.perform(delete("/api/tipcalperis/{id}", tipcalperi.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tipcalperiExistsInEs = tipcalperiSearchRepository.exists(tipcalperi.getId());
        assertThat(tipcalperiExistsInEs).isFalse();

        // Validate the database is empty
        List<Tipcalperi> tipcalperiList = tipcalperiRepository.findAll();
        assertThat(tipcalperiList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTipcalperi() throws Exception {
        // Initialize the database
        tipcalperiRepository.saveAndFlush(tipcalperi);
        tipcalperiSearchRepository.save(tipcalperi);

        // Search the tipcalperi
        restTipcalperiMockMvc.perform(get("/api/_search/tipcalperis?query=id:" + tipcalperi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipcalperi.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNomtipcal").value(hasItem(DEFAULT_V_NOMTIPCAL.toString())))
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
        TestUtil.equalsVerifier(Tipcalperi.class);
        Tipcalperi tipcalperi1 = new Tipcalperi();
        tipcalperi1.setId(1L);
        Tipcalperi tipcalperi2 = new Tipcalperi();
        tipcalperi2.setId(tipcalperi1.getId());
        assertThat(tipcalperi1).isEqualTo(tipcalperi2);
        tipcalperi2.setId(2L);
        assertThat(tipcalperi1).isNotEqualTo(tipcalperi2);
        tipcalperi1.setId(null);
        assertThat(tipcalperi1).isNotEqualTo(tipcalperi2);
    }
}
