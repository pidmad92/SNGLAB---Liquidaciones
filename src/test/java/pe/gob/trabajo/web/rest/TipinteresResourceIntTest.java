package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Tipinteres;
import pe.gob.trabajo.repository.TipinteresRepository;
import pe.gob.trabajo.repository.search.TipinteresSearchRepository;
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
 * Test class for the TipinteresResource REST controller.
 *
 * @see TipinteresResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class TipinteresResourceIntTest {

    private static final String DEFAULT_V_NOMTIPINT = "AAAAAAAAAA";
    private static final String UPDATED_V_NOMTIPINT = "BBBBBBBBBB";

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
    private TipinteresRepository tipinteresRepository;

    @Autowired
    private TipinteresSearchRepository tipinteresSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTipinteresMockMvc;

    private Tipinteres tipinteres;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipinteresResource tipinteresResource = new TipinteresResource(tipinteresRepository, tipinteresSearchRepository);
        this.restTipinteresMockMvc = MockMvcBuilders.standaloneSetup(tipinteresResource)
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
    public static Tipinteres createEntity(EntityManager em) {
        Tipinteres tipinteres = new Tipinteres()
            .vNomtipint(DEFAULT_V_NOMTIPINT)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return tipinteres;
    }

    @Before
    public void initTest() {
        tipinteresSearchRepository.deleteAll();
        tipinteres = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipinteres() throws Exception {
        int databaseSizeBeforeCreate = tipinteresRepository.findAll().size();

        // Create the Tipinteres
        restTipinteresMockMvc.perform(post("/api/tipinteres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipinteres)))
            .andExpect(status().isCreated());

        // Validate the Tipinteres in the database
        List<Tipinteres> tipinteresList = tipinteresRepository.findAll();
        assertThat(tipinteresList).hasSize(databaseSizeBeforeCreate + 1);
        Tipinteres testTipinteres = tipinteresList.get(tipinteresList.size() - 1);
        assertThat(testTipinteres.getvNomtipint()).isEqualTo(DEFAULT_V_NOMTIPINT);
        assertThat(testTipinteres.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testTipinteres.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testTipinteres.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testTipinteres.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testTipinteres.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testTipinteres.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testTipinteres.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Tipinteres in Elasticsearch
        Tipinteres tipinteresEs = tipinteresSearchRepository.findOne(testTipinteres.getId());
        assertThat(tipinteresEs).isEqualToComparingFieldByField(testTipinteres);
    }

    @Test
    @Transactional
    public void createTipinteresWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipinteresRepository.findAll().size();

        // Create the Tipinteres with an existing ID
        tipinteres.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipinteresMockMvc.perform(post("/api/tipinteres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipinteres)))
            .andExpect(status().isBadRequest());

        // Validate the Tipinteres in the database
        List<Tipinteres> tipinteresList = tipinteresRepository.findAll();
        assertThat(tipinteresList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvNomtipintIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipinteresRepository.findAll().size();
        // set the field null
        tipinteres.setvNomtipint(null);

        // Create the Tipinteres, which fails.

        restTipinteresMockMvc.perform(post("/api/tipinteres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipinteres)))
            .andExpect(status().isBadRequest());

        List<Tipinteres> tipinteresList = tipinteresRepository.findAll();
        assertThat(tipinteresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipinteresRepository.findAll().size();
        // set the field null
        tipinteres.setnUsuareg(null);

        // Create the Tipinteres, which fails.

        restTipinteresMockMvc.perform(post("/api/tipinteres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipinteres)))
            .andExpect(status().isBadRequest());

        List<Tipinteres> tipinteresList = tipinteresRepository.findAll();
        assertThat(tipinteresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipinteresRepository.findAll().size();
        // set the field null
        tipinteres.settFecreg(null);

        // Create the Tipinteres, which fails.

        restTipinteresMockMvc.perform(post("/api/tipinteres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipinteres)))
            .andExpect(status().isBadRequest());

        List<Tipinteres> tipinteresList = tipinteresRepository.findAll();
        assertThat(tipinteresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipinteresRepository.findAll().size();
        // set the field null
        tipinteres.setnFlgactivo(null);

        // Create the Tipinteres, which fails.

        restTipinteresMockMvc.perform(post("/api/tipinteres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipinteres)))
            .andExpect(status().isBadRequest());

        List<Tipinteres> tipinteresList = tipinteresRepository.findAll();
        assertThat(tipinteresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipinteresRepository.findAll().size();
        // set the field null
        tipinteres.setnSedereg(null);

        // Create the Tipinteres, which fails.

        restTipinteresMockMvc.perform(post("/api/tipinteres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipinteres)))
            .andExpect(status().isBadRequest());

        List<Tipinteres> tipinteresList = tipinteresRepository.findAll();
        assertThat(tipinteresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipinteres() throws Exception {
        // Initialize the database
        tipinteresRepository.saveAndFlush(tipinteres);

        // Get all the tipinteresList
        restTipinteresMockMvc.perform(get("/api/tipinteres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipinteres.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNomtipint").value(hasItem(DEFAULT_V_NOMTIPINT.toString())))
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
    public void getTipinteres() throws Exception {
        // Initialize the database
        tipinteresRepository.saveAndFlush(tipinteres);

        // Get the tipinteres
        restTipinteresMockMvc.perform(get("/api/tipinteres/{id}", tipinteres.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipinteres.getId().intValue()))
            .andExpect(jsonPath("$.vNomtipint").value(DEFAULT_V_NOMTIPINT.toString()))
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
    public void getNonExistingTipinteres() throws Exception {
        // Get the tipinteres
        restTipinteresMockMvc.perform(get("/api/tipinteres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipinteres() throws Exception {
        // Initialize the database
        tipinteresRepository.saveAndFlush(tipinteres);
        tipinteresSearchRepository.save(tipinteres);
        int databaseSizeBeforeUpdate = tipinteresRepository.findAll().size();

        // Update the tipinteres
        Tipinteres updatedTipinteres = tipinteresRepository.findOne(tipinteres.getId());
        updatedTipinteres
            .vNomtipint(UPDATED_V_NOMTIPINT)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restTipinteresMockMvc.perform(put("/api/tipinteres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipinteres)))
            .andExpect(status().isOk());

        // Validate the Tipinteres in the database
        List<Tipinteres> tipinteresList = tipinteresRepository.findAll();
        assertThat(tipinteresList).hasSize(databaseSizeBeforeUpdate);
        Tipinteres testTipinteres = tipinteresList.get(tipinteresList.size() - 1);
        assertThat(testTipinteres.getvNomtipint()).isEqualTo(UPDATED_V_NOMTIPINT);
        assertThat(testTipinteres.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testTipinteres.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testTipinteres.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testTipinteres.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testTipinteres.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testTipinteres.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testTipinteres.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Tipinteres in Elasticsearch
        Tipinteres tipinteresEs = tipinteresSearchRepository.findOne(testTipinteres.getId());
        assertThat(tipinteresEs).isEqualToComparingFieldByField(testTipinteres);
    }

    @Test
    @Transactional
    public void updateNonExistingTipinteres() throws Exception {
        int databaseSizeBeforeUpdate = tipinteresRepository.findAll().size();

        // Create the Tipinteres

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTipinteresMockMvc.perform(put("/api/tipinteres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipinteres)))
            .andExpect(status().isCreated());

        // Validate the Tipinteres in the database
        List<Tipinteres> tipinteresList = tipinteresRepository.findAll();
        assertThat(tipinteresList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTipinteres() throws Exception {
        // Initialize the database
        tipinteresRepository.saveAndFlush(tipinteres);
        tipinteresSearchRepository.save(tipinteres);
        int databaseSizeBeforeDelete = tipinteresRepository.findAll().size();

        // Get the tipinteres
        restTipinteresMockMvc.perform(delete("/api/tipinteres/{id}", tipinteres.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tipinteresExistsInEs = tipinteresSearchRepository.exists(tipinteres.getId());
        assertThat(tipinteresExistsInEs).isFalse();

        // Validate the database is empty
        List<Tipinteres> tipinteresList = tipinteresRepository.findAll();
        assertThat(tipinteresList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTipinteres() throws Exception {
        // Initialize the database
        tipinteresRepository.saveAndFlush(tipinteres);
        tipinteresSearchRepository.save(tipinteres);

        // Search the tipinteres
        restTipinteresMockMvc.perform(get("/api/_search/tipinteres?query=id:" + tipinteres.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipinteres.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNomtipint").value(hasItem(DEFAULT_V_NOMTIPINT.toString())))
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
        TestUtil.equalsVerifier(Tipinteres.class);
        Tipinteres tipinteres1 = new Tipinteres();
        tipinteres1.setId(1L);
        Tipinteres tipinteres2 = new Tipinteres();
        tipinteres2.setId(tipinteres1.getId());
        assertThat(tipinteres1).isEqualTo(tipinteres2);
        tipinteres2.setId(2L);
        assertThat(tipinteres1).isNotEqualTo(tipinteres2);
        tipinteres1.setId(null);
        assertThat(tipinteres1).isNotEqualTo(tipinteres2);
    }
}
