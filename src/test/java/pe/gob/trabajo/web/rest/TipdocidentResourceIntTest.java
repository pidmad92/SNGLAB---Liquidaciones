package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Tipdocident;
import pe.gob.trabajo.repository.TipdocidentRepository;
import pe.gob.trabajo.repository.search.TipdocidentSearchRepository;
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
 * Test class for the TipdocidentResource REST controller.
 *
 * @see TipdocidentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class TipdocidentResourceIntTest {

    private static final String DEFAULT_V_DESDOCIDE = "AAAAAAAAAA";
    private static final String UPDATED_V_DESDOCIDE = "BBBBBBBBBB";

    private static final Integer DEFAULT_N_NUMDIGI = 1;
    private static final Integer UPDATED_N_NUMDIGI = 2;

    private static final String DEFAULT_V_DESCORTA = "AAAAAAAAAA";
    private static final String UPDATED_V_DESCORTA = "BBBBBBBBBB";

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
    private TipdocidentRepository tipdocidentRepository;

    @Autowired
    private TipdocidentSearchRepository tipdocidentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTipdocidentMockMvc;

    private Tipdocident tipdocident;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipdocidentResource tipdocidentResource = new TipdocidentResource(tipdocidentRepository, tipdocidentSearchRepository);
        this.restTipdocidentMockMvc = MockMvcBuilders.standaloneSetup(tipdocidentResource)
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
    public static Tipdocident createEntity(EntityManager em) {
        Tipdocident tipdocident = new Tipdocident()
            .vDesdocide(DEFAULT_V_DESDOCIDE)
            .nNumdigi(DEFAULT_N_NUMDIGI)
            .vDescorta(DEFAULT_V_DESCORTA)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return tipdocident;
    }

    @Before
    public void initTest() {
        tipdocidentSearchRepository.deleteAll();
        tipdocident = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipdocident() throws Exception {
        int databaseSizeBeforeCreate = tipdocidentRepository.findAll().size();

        // Create the Tipdocident
        restTipdocidentMockMvc.perform(post("/api/tipdocidents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdocident)))
            .andExpect(status().isCreated());

        // Validate the Tipdocident in the database
        List<Tipdocident> tipdocidentList = tipdocidentRepository.findAll();
        assertThat(tipdocidentList).hasSize(databaseSizeBeforeCreate + 1);
        Tipdocident testTipdocident = tipdocidentList.get(tipdocidentList.size() - 1);
        assertThat(testTipdocident.getvDesdocide()).isEqualTo(DEFAULT_V_DESDOCIDE);
        assertThat(testTipdocident.getnNumdigi()).isEqualTo(DEFAULT_N_NUMDIGI);
        assertThat(testTipdocident.getvDescorta()).isEqualTo(DEFAULT_V_DESCORTA);
        assertThat(testTipdocident.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testTipdocident.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testTipdocident.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testTipdocident.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testTipdocident.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testTipdocident.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testTipdocident.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Tipdocident in Elasticsearch
        Tipdocident tipdocidentEs = tipdocidentSearchRepository.findOne(testTipdocident.getId());
        assertThat(tipdocidentEs).isEqualToComparingFieldByField(testTipdocident);
    }

    @Test
    @Transactional
    public void createTipdocidentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipdocidentRepository.findAll().size();

        // Create the Tipdocident with an existing ID
        tipdocident.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipdocidentMockMvc.perform(post("/api/tipdocidents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdocident)))
            .andExpect(status().isBadRequest());

        // Validate the Tipdocident in the database
        List<Tipdocident> tipdocidentList = tipdocidentRepository.findAll();
        assertThat(tipdocidentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDesdocideIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipdocidentRepository.findAll().size();
        // set the field null
        tipdocident.setvDesdocide(null);

        // Create the Tipdocident, which fails.

        restTipdocidentMockMvc.perform(post("/api/tipdocidents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdocident)))
            .andExpect(status().isBadRequest());

        List<Tipdocident> tipdocidentList = tipdocidentRepository.findAll();
        assertThat(tipdocidentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknNumdigiIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipdocidentRepository.findAll().size();
        // set the field null
        tipdocident.setnNumdigi(null);

        // Create the Tipdocident, which fails.

        restTipdocidentMockMvc.perform(post("/api/tipdocidents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdocident)))
            .andExpect(status().isBadRequest());

        List<Tipdocident> tipdocidentList = tipdocidentRepository.findAll();
        assertThat(tipdocidentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkvDescortaIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipdocidentRepository.findAll().size();
        // set the field null
        tipdocident.setvDescorta(null);

        // Create the Tipdocident, which fails.

        restTipdocidentMockMvc.perform(post("/api/tipdocidents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdocident)))
            .andExpect(status().isBadRequest());

        List<Tipdocident> tipdocidentList = tipdocidentRepository.findAll();
        assertThat(tipdocidentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipdocidentRepository.findAll().size();
        // set the field null
        tipdocident.setnUsuareg(null);

        // Create the Tipdocident, which fails.

        restTipdocidentMockMvc.perform(post("/api/tipdocidents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdocident)))
            .andExpect(status().isBadRequest());

        List<Tipdocident> tipdocidentList = tipdocidentRepository.findAll();
        assertThat(tipdocidentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipdocidentRepository.findAll().size();
        // set the field null
        tipdocident.settFecreg(null);

        // Create the Tipdocident, which fails.

        restTipdocidentMockMvc.perform(post("/api/tipdocidents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdocident)))
            .andExpect(status().isBadRequest());

        List<Tipdocident> tipdocidentList = tipdocidentRepository.findAll();
        assertThat(tipdocidentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipdocidentRepository.findAll().size();
        // set the field null
        tipdocident.setnFlgactivo(null);

        // Create the Tipdocident, which fails.

        restTipdocidentMockMvc.perform(post("/api/tipdocidents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdocident)))
            .andExpect(status().isBadRequest());

        List<Tipdocident> tipdocidentList = tipdocidentRepository.findAll();
        assertThat(tipdocidentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipdocidentRepository.findAll().size();
        // set the field null
        tipdocident.setnSedereg(null);

        // Create the Tipdocident, which fails.

        restTipdocidentMockMvc.perform(post("/api/tipdocidents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdocident)))
            .andExpect(status().isBadRequest());

        List<Tipdocident> tipdocidentList = tipdocidentRepository.findAll();
        assertThat(tipdocidentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipdocidents() throws Exception {
        // Initialize the database
        tipdocidentRepository.saveAndFlush(tipdocident);

        // Get all the tipdocidentList
        restTipdocidentMockMvc.perform(get("/api/tipdocidents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipdocident.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesdocide").value(hasItem(DEFAULT_V_DESDOCIDE.toString())))
            .andExpect(jsonPath("$.[*].nNumdigi").value(hasItem(DEFAULT_N_NUMDIGI)))
            .andExpect(jsonPath("$.[*].vDescorta").value(hasItem(DEFAULT_V_DESCORTA.toString())))
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
    public void getTipdocident() throws Exception {
        // Initialize the database
        tipdocidentRepository.saveAndFlush(tipdocident);

        // Get the tipdocident
        restTipdocidentMockMvc.perform(get("/api/tipdocidents/{id}", tipdocident.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipdocident.getId().intValue()))
            .andExpect(jsonPath("$.vDesdocide").value(DEFAULT_V_DESDOCIDE.toString()))
            .andExpect(jsonPath("$.nNumdigi").value(DEFAULT_N_NUMDIGI))
            .andExpect(jsonPath("$.vDescorta").value(DEFAULT_V_DESCORTA.toString()))
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
    public void getNonExistingTipdocident() throws Exception {
        // Get the tipdocident
        restTipdocidentMockMvc.perform(get("/api/tipdocidents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipdocident() throws Exception {
        // Initialize the database
        tipdocidentRepository.saveAndFlush(tipdocident);
        tipdocidentSearchRepository.save(tipdocident);
        int databaseSizeBeforeUpdate = tipdocidentRepository.findAll().size();

        // Update the tipdocident
        Tipdocident updatedTipdocident = tipdocidentRepository.findOne(tipdocident.getId());
        updatedTipdocident
            .vDesdocide(UPDATED_V_DESDOCIDE)
            .nNumdigi(UPDATED_N_NUMDIGI)
            .vDescorta(UPDATED_V_DESCORTA)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restTipdocidentMockMvc.perform(put("/api/tipdocidents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipdocident)))
            .andExpect(status().isOk());

        // Validate the Tipdocident in the database
        List<Tipdocident> tipdocidentList = tipdocidentRepository.findAll();
        assertThat(tipdocidentList).hasSize(databaseSizeBeforeUpdate);
        Tipdocident testTipdocident = tipdocidentList.get(tipdocidentList.size() - 1);
        assertThat(testTipdocident.getvDesdocide()).isEqualTo(UPDATED_V_DESDOCIDE);
        assertThat(testTipdocident.getnNumdigi()).isEqualTo(UPDATED_N_NUMDIGI);
        assertThat(testTipdocident.getvDescorta()).isEqualTo(UPDATED_V_DESCORTA);
        assertThat(testTipdocident.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testTipdocident.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testTipdocident.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testTipdocident.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testTipdocident.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testTipdocident.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testTipdocident.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Tipdocident in Elasticsearch
        Tipdocident tipdocidentEs = tipdocidentSearchRepository.findOne(testTipdocident.getId());
        assertThat(tipdocidentEs).isEqualToComparingFieldByField(testTipdocident);
    }

    @Test
    @Transactional
    public void updateNonExistingTipdocident() throws Exception {
        int databaseSizeBeforeUpdate = tipdocidentRepository.findAll().size();

        // Create the Tipdocident

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTipdocidentMockMvc.perform(put("/api/tipdocidents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdocident)))
            .andExpect(status().isCreated());

        // Validate the Tipdocident in the database
        List<Tipdocident> tipdocidentList = tipdocidentRepository.findAll();
        assertThat(tipdocidentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTipdocident() throws Exception {
        // Initialize the database
        tipdocidentRepository.saveAndFlush(tipdocident);
        tipdocidentSearchRepository.save(tipdocident);
        int databaseSizeBeforeDelete = tipdocidentRepository.findAll().size();

        // Get the tipdocident
        restTipdocidentMockMvc.perform(delete("/api/tipdocidents/{id}", tipdocident.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tipdocidentExistsInEs = tipdocidentSearchRepository.exists(tipdocident.getId());
        assertThat(tipdocidentExistsInEs).isFalse();

        // Validate the database is empty
        List<Tipdocident> tipdocidentList = tipdocidentRepository.findAll();
        assertThat(tipdocidentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTipdocident() throws Exception {
        // Initialize the database
        tipdocidentRepository.saveAndFlush(tipdocident);
        tipdocidentSearchRepository.save(tipdocident);

        // Search the tipdocident
        restTipdocidentMockMvc.perform(get("/api/_search/tipdocidents?query=id:" + tipdocident.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipdocident.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesdocide").value(hasItem(DEFAULT_V_DESDOCIDE.toString())))
            .andExpect(jsonPath("$.[*].nNumdigi").value(hasItem(DEFAULT_N_NUMDIGI)))
            .andExpect(jsonPath("$.[*].vDescorta").value(hasItem(DEFAULT_V_DESCORTA.toString())))
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
        TestUtil.equalsVerifier(Tipdocident.class);
        Tipdocident tipdocident1 = new Tipdocident();
        tipdocident1.setId(1L);
        Tipdocident tipdocident2 = new Tipdocident();
        tipdocident2.setId(tipdocident1.getId());
        assertThat(tipdocident1).isEqualTo(tipdocident2);
        tipdocident2.setId(2L);
        assertThat(tipdocident1).isNotEqualTo(tipdocident2);
        tipdocident1.setId(null);
        assertThat(tipdocident1).isNotEqualTo(tipdocident2);
    }
}
