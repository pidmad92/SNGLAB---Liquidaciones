package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Tipcalconre;
import pe.gob.trabajo.repository.TipcalconreRepository;
import pe.gob.trabajo.repository.search.TipcalconreSearchRepository;
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
 * Test class for the TipcalconreResource REST controller.
 *
 * @see TipcalconreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class TipcalconreResourceIntTest {

    private static final String DEFAULT_V_NOMTCAL = "AAAAAAAAAA";
    private static final String UPDATED_V_NOMTCAL = "BBBBBBBBBB";

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
    private TipcalconreRepository tipcalconreRepository;

    @Autowired
    private TipcalconreSearchRepository tipcalconreSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTipcalconreMockMvc;

    private Tipcalconre tipcalconre;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipcalconreResource tipcalconreResource = new TipcalconreResource(tipcalconreRepository, tipcalconreSearchRepository);
        this.restTipcalconreMockMvc = MockMvcBuilders.standaloneSetup(tipcalconreResource)
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
    public static Tipcalconre createEntity(EntityManager em) {
        Tipcalconre tipcalconre = new Tipcalconre()
            .vNomtcal(DEFAULT_V_NOMTCAL)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return tipcalconre;
    }

    @Before
    public void initTest() {
        tipcalconreSearchRepository.deleteAll();
        tipcalconre = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipcalconre() throws Exception {
        int databaseSizeBeforeCreate = tipcalconreRepository.findAll().size();

        // Create the Tipcalconre
        restTipcalconreMockMvc.perform(post("/api/tipcalconres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalconre)))
            .andExpect(status().isCreated());

        // Validate the Tipcalconre in the database
        List<Tipcalconre> tipcalconreList = tipcalconreRepository.findAll();
        assertThat(tipcalconreList).hasSize(databaseSizeBeforeCreate + 1);
        Tipcalconre testTipcalconre = tipcalconreList.get(tipcalconreList.size() - 1);
        assertThat(testTipcalconre.getvNomtcal()).isEqualTo(DEFAULT_V_NOMTCAL);
        assertThat(testTipcalconre.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testTipcalconre.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testTipcalconre.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testTipcalconre.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testTipcalconre.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testTipcalconre.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testTipcalconre.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Tipcalconre in Elasticsearch
        Tipcalconre tipcalconreEs = tipcalconreSearchRepository.findOne(testTipcalconre.getId());
        assertThat(tipcalconreEs).isEqualToComparingFieldByField(testTipcalconre);
    }

    @Test
    @Transactional
    public void createTipcalconreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipcalconreRepository.findAll().size();

        // Create the Tipcalconre with an existing ID
        tipcalconre.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipcalconreMockMvc.perform(post("/api/tipcalconres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalconre)))
            .andExpect(status().isBadRequest());

        // Validate the Tipcalconre in the database
        List<Tipcalconre> tipcalconreList = tipcalconreRepository.findAll();
        assertThat(tipcalconreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvNomtcalIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipcalconreRepository.findAll().size();
        // set the field null
        tipcalconre.setvNomtcal(null);

        // Create the Tipcalconre, which fails.

        restTipcalconreMockMvc.perform(post("/api/tipcalconres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalconre)))
            .andExpect(status().isBadRequest());

        List<Tipcalconre> tipcalconreList = tipcalconreRepository.findAll();
        assertThat(tipcalconreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipcalconreRepository.findAll().size();
        // set the field null
        tipcalconre.setnUsuareg(null);

        // Create the Tipcalconre, which fails.

        restTipcalconreMockMvc.perform(post("/api/tipcalconres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalconre)))
            .andExpect(status().isBadRequest());

        List<Tipcalconre> tipcalconreList = tipcalconreRepository.findAll();
        assertThat(tipcalconreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipcalconreRepository.findAll().size();
        // set the field null
        tipcalconre.settFecreg(null);

        // Create the Tipcalconre, which fails.

        restTipcalconreMockMvc.perform(post("/api/tipcalconres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalconre)))
            .andExpect(status().isBadRequest());

        List<Tipcalconre> tipcalconreList = tipcalconreRepository.findAll();
        assertThat(tipcalconreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipcalconreRepository.findAll().size();
        // set the field null
        tipcalconre.setnFlgactivo(null);

        // Create the Tipcalconre, which fails.

        restTipcalconreMockMvc.perform(post("/api/tipcalconres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalconre)))
            .andExpect(status().isBadRequest());

        List<Tipcalconre> tipcalconreList = tipcalconreRepository.findAll();
        assertThat(tipcalconreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipcalconreRepository.findAll().size();
        // set the field null
        tipcalconre.setnSedereg(null);

        // Create the Tipcalconre, which fails.

        restTipcalconreMockMvc.perform(post("/api/tipcalconres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalconre)))
            .andExpect(status().isBadRequest());

        List<Tipcalconre> tipcalconreList = tipcalconreRepository.findAll();
        assertThat(tipcalconreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipcalconres() throws Exception {
        // Initialize the database
        tipcalconreRepository.saveAndFlush(tipcalconre);

        // Get all the tipcalconreList
        restTipcalconreMockMvc.perform(get("/api/tipcalconres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipcalconre.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNomtcal").value(hasItem(DEFAULT_V_NOMTCAL.toString())))
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
    public void getTipcalconre() throws Exception {
        // Initialize the database
        tipcalconreRepository.saveAndFlush(tipcalconre);

        // Get the tipcalconre
        restTipcalconreMockMvc.perform(get("/api/tipcalconres/{id}", tipcalconre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipcalconre.getId().intValue()))
            .andExpect(jsonPath("$.vNomtcal").value(DEFAULT_V_NOMTCAL.toString()))
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
    public void getNonExistingTipcalconre() throws Exception {
        // Get the tipcalconre
        restTipcalconreMockMvc.perform(get("/api/tipcalconres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipcalconre() throws Exception {
        // Initialize the database
        tipcalconreRepository.saveAndFlush(tipcalconre);
        tipcalconreSearchRepository.save(tipcalconre);
        int databaseSizeBeforeUpdate = tipcalconreRepository.findAll().size();

        // Update the tipcalconre
        Tipcalconre updatedTipcalconre = tipcalconreRepository.findOne(tipcalconre.getId());
        updatedTipcalconre
            .vNomtcal(UPDATED_V_NOMTCAL)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restTipcalconreMockMvc.perform(put("/api/tipcalconres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipcalconre)))
            .andExpect(status().isOk());

        // Validate the Tipcalconre in the database
        List<Tipcalconre> tipcalconreList = tipcalconreRepository.findAll();
        assertThat(tipcalconreList).hasSize(databaseSizeBeforeUpdate);
        Tipcalconre testTipcalconre = tipcalconreList.get(tipcalconreList.size() - 1);
        assertThat(testTipcalconre.getvNomtcal()).isEqualTo(UPDATED_V_NOMTCAL);
        assertThat(testTipcalconre.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testTipcalconre.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testTipcalconre.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testTipcalconre.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testTipcalconre.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testTipcalconre.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testTipcalconre.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Tipcalconre in Elasticsearch
        Tipcalconre tipcalconreEs = tipcalconreSearchRepository.findOne(testTipcalconre.getId());
        assertThat(tipcalconreEs).isEqualToComparingFieldByField(testTipcalconre);
    }

    @Test
    @Transactional
    public void updateNonExistingTipcalconre() throws Exception {
        int databaseSizeBeforeUpdate = tipcalconreRepository.findAll().size();

        // Create the Tipcalconre

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTipcalconreMockMvc.perform(put("/api/tipcalconres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipcalconre)))
            .andExpect(status().isCreated());

        // Validate the Tipcalconre in the database
        List<Tipcalconre> tipcalconreList = tipcalconreRepository.findAll();
        assertThat(tipcalconreList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTipcalconre() throws Exception {
        // Initialize the database
        tipcalconreRepository.saveAndFlush(tipcalconre);
        tipcalconreSearchRepository.save(tipcalconre);
        int databaseSizeBeforeDelete = tipcalconreRepository.findAll().size();

        // Get the tipcalconre
        restTipcalconreMockMvc.perform(delete("/api/tipcalconres/{id}", tipcalconre.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tipcalconreExistsInEs = tipcalconreSearchRepository.exists(tipcalconre.getId());
        assertThat(tipcalconreExistsInEs).isFalse();

        // Validate the database is empty
        List<Tipcalconre> tipcalconreList = tipcalconreRepository.findAll();
        assertThat(tipcalconreList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTipcalconre() throws Exception {
        // Initialize the database
        tipcalconreRepository.saveAndFlush(tipcalconre);
        tipcalconreSearchRepository.save(tipcalconre);

        // Search the tipcalconre
        restTipcalconreMockMvc.perform(get("/api/_search/tipcalconres?query=id:" + tipcalconre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipcalconre.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNomtcal").value(hasItem(DEFAULT_V_NOMTCAL.toString())))
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
        TestUtil.equalsVerifier(Tipcalconre.class);
        Tipcalconre tipcalconre1 = new Tipcalconre();
        tipcalconre1.setId(1L);
        Tipcalconre tipcalconre2 = new Tipcalconre();
        tipcalconre2.setId(tipcalconre1.getId());
        assertThat(tipcalconre1).isEqualTo(tipcalconre2);
        tipcalconre2.setId(2L);
        assertThat(tipcalconre1).isNotEqualTo(tipcalconre2);
        tipcalconre1.setId(null);
        assertThat(tipcalconre1).isNotEqualTo(tipcalconre2);
    }
}
