package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Tipmotaten;
import pe.gob.trabajo.repository.TipmotatenRepository;
import pe.gob.trabajo.repository.search.TipmotatenSearchRepository;
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
 * Test class for the TipmotatenResource REST controller.
 *
 * @see TipmotatenResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class TipmotatenResourceIntTest {

    private static final String DEFAULT_V_DESTIPMOT = "AAAAAAAAAA";
    private static final String UPDATED_V_DESTIPMOT = "BBBBBBBBBB";

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
    private TipmotatenRepository tipmotatenRepository;

    @Autowired
    private TipmotatenSearchRepository tipmotatenSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTipmotatenMockMvc;

    private Tipmotaten tipmotaten;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipmotatenResource tipmotatenResource = new TipmotatenResource(tipmotatenRepository, tipmotatenSearchRepository);
        this.restTipmotatenMockMvc = MockMvcBuilders.standaloneSetup(tipmotatenResource)
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
    public static Tipmotaten createEntity(EntityManager em) {
        Tipmotaten tipmotaten = new Tipmotaten()
            .vDestipmot(DEFAULT_V_DESTIPMOT)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return tipmotaten;
    }

    @Before
    public void initTest() {
        tipmotatenSearchRepository.deleteAll();
        tipmotaten = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipmotaten() throws Exception {
        int databaseSizeBeforeCreate = tipmotatenRepository.findAll().size();

        // Create the Tipmotaten
        restTipmotatenMockMvc.perform(post("/api/tipmotatens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipmotaten)))
            .andExpect(status().isCreated());

        // Validate the Tipmotaten in the database
        List<Tipmotaten> tipmotatenList = tipmotatenRepository.findAll();
        assertThat(tipmotatenList).hasSize(databaseSizeBeforeCreate + 1);
        Tipmotaten testTipmotaten = tipmotatenList.get(tipmotatenList.size() - 1);
        assertThat(testTipmotaten.getvDestipmot()).isEqualTo(DEFAULT_V_DESTIPMOT);
        assertThat(testTipmotaten.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testTipmotaten.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testTipmotaten.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testTipmotaten.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testTipmotaten.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testTipmotaten.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testTipmotaten.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Tipmotaten in Elasticsearch
        Tipmotaten tipmotatenEs = tipmotatenSearchRepository.findOne(testTipmotaten.getId());
        assertThat(tipmotatenEs).isEqualToComparingFieldByField(testTipmotaten);
    }

    @Test
    @Transactional
    public void createTipmotatenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipmotatenRepository.findAll().size();

        // Create the Tipmotaten with an existing ID
        tipmotaten.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipmotatenMockMvc.perform(post("/api/tipmotatens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipmotaten)))
            .andExpect(status().isBadRequest());

        // Validate the Tipmotaten in the database
        List<Tipmotaten> tipmotatenList = tipmotatenRepository.findAll();
        assertThat(tipmotatenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDestipmotIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipmotatenRepository.findAll().size();
        // set the field null
        tipmotaten.setvDestipmot(null);

        // Create the Tipmotaten, which fails.

        restTipmotatenMockMvc.perform(post("/api/tipmotatens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipmotaten)))
            .andExpect(status().isBadRequest());

        List<Tipmotaten> tipmotatenList = tipmotatenRepository.findAll();
        assertThat(tipmotatenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipmotatenRepository.findAll().size();
        // set the field null
        tipmotaten.setnUsuareg(null);

        // Create the Tipmotaten, which fails.

        restTipmotatenMockMvc.perform(post("/api/tipmotatens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipmotaten)))
            .andExpect(status().isBadRequest());

        List<Tipmotaten> tipmotatenList = tipmotatenRepository.findAll();
        assertThat(tipmotatenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipmotatenRepository.findAll().size();
        // set the field null
        tipmotaten.settFecreg(null);

        // Create the Tipmotaten, which fails.

        restTipmotatenMockMvc.perform(post("/api/tipmotatens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipmotaten)))
            .andExpect(status().isBadRequest());

        List<Tipmotaten> tipmotatenList = tipmotatenRepository.findAll();
        assertThat(tipmotatenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipmotatenRepository.findAll().size();
        // set the field null
        tipmotaten.setnFlgactivo(null);

        // Create the Tipmotaten, which fails.

        restTipmotatenMockMvc.perform(post("/api/tipmotatens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipmotaten)))
            .andExpect(status().isBadRequest());

        List<Tipmotaten> tipmotatenList = tipmotatenRepository.findAll();
        assertThat(tipmotatenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipmotatenRepository.findAll().size();
        // set the field null
        tipmotaten.setnSedereg(null);

        // Create the Tipmotaten, which fails.

        restTipmotatenMockMvc.perform(post("/api/tipmotatens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipmotaten)))
            .andExpect(status().isBadRequest());

        List<Tipmotaten> tipmotatenList = tipmotatenRepository.findAll();
        assertThat(tipmotatenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipmotatens() throws Exception {
        // Initialize the database
        tipmotatenRepository.saveAndFlush(tipmotaten);

        // Get all the tipmotatenList
        restTipmotatenMockMvc.perform(get("/api/tipmotatens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipmotaten.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDestipmot").value(hasItem(DEFAULT_V_DESTIPMOT.toString())))
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
    public void getTipmotaten() throws Exception {
        // Initialize the database
        tipmotatenRepository.saveAndFlush(tipmotaten);

        // Get the tipmotaten
        restTipmotatenMockMvc.perform(get("/api/tipmotatens/{id}", tipmotaten.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipmotaten.getId().intValue()))
            .andExpect(jsonPath("$.vDestipmot").value(DEFAULT_V_DESTIPMOT.toString()))
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
    public void getNonExistingTipmotaten() throws Exception {
        // Get the tipmotaten
        restTipmotatenMockMvc.perform(get("/api/tipmotatens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipmotaten() throws Exception {
        // Initialize the database
        tipmotatenRepository.saveAndFlush(tipmotaten);
        tipmotatenSearchRepository.save(tipmotaten);
        int databaseSizeBeforeUpdate = tipmotatenRepository.findAll().size();

        // Update the tipmotaten
        Tipmotaten updatedTipmotaten = tipmotatenRepository.findOne(tipmotaten.getId());
        updatedTipmotaten
            .vDestipmot(UPDATED_V_DESTIPMOT)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restTipmotatenMockMvc.perform(put("/api/tipmotatens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipmotaten)))
            .andExpect(status().isOk());

        // Validate the Tipmotaten in the database
        List<Tipmotaten> tipmotatenList = tipmotatenRepository.findAll();
        assertThat(tipmotatenList).hasSize(databaseSizeBeforeUpdate);
        Tipmotaten testTipmotaten = tipmotatenList.get(tipmotatenList.size() - 1);
        assertThat(testTipmotaten.getvDestipmot()).isEqualTo(UPDATED_V_DESTIPMOT);
        assertThat(testTipmotaten.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testTipmotaten.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testTipmotaten.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testTipmotaten.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testTipmotaten.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testTipmotaten.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testTipmotaten.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Tipmotaten in Elasticsearch
        Tipmotaten tipmotatenEs = tipmotatenSearchRepository.findOne(testTipmotaten.getId());
        assertThat(tipmotatenEs).isEqualToComparingFieldByField(testTipmotaten);
    }

    @Test
    @Transactional
    public void updateNonExistingTipmotaten() throws Exception {
        int databaseSizeBeforeUpdate = tipmotatenRepository.findAll().size();

        // Create the Tipmotaten

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTipmotatenMockMvc.perform(put("/api/tipmotatens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipmotaten)))
            .andExpect(status().isCreated());

        // Validate the Tipmotaten in the database
        List<Tipmotaten> tipmotatenList = tipmotatenRepository.findAll();
        assertThat(tipmotatenList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTipmotaten() throws Exception {
        // Initialize the database
        tipmotatenRepository.saveAndFlush(tipmotaten);
        tipmotatenSearchRepository.save(tipmotaten);
        int databaseSizeBeforeDelete = tipmotatenRepository.findAll().size();

        // Get the tipmotaten
        restTipmotatenMockMvc.perform(delete("/api/tipmotatens/{id}", tipmotaten.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tipmotatenExistsInEs = tipmotatenSearchRepository.exists(tipmotaten.getId());
        assertThat(tipmotatenExistsInEs).isFalse();

        // Validate the database is empty
        List<Tipmotaten> tipmotatenList = tipmotatenRepository.findAll();
        assertThat(tipmotatenList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTipmotaten() throws Exception {
        // Initialize the database
        tipmotatenRepository.saveAndFlush(tipmotaten);
        tipmotatenSearchRepository.save(tipmotaten);

        // Search the tipmotaten
        restTipmotatenMockMvc.perform(get("/api/_search/tipmotatens?query=id:" + tipmotaten.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipmotaten.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDestipmot").value(hasItem(DEFAULT_V_DESTIPMOT.toString())))
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
        TestUtil.equalsVerifier(Tipmotaten.class);
        Tipmotaten tipmotaten1 = new Tipmotaten();
        tipmotaten1.setId(1L);
        Tipmotaten tipmotaten2 = new Tipmotaten();
        tipmotaten2.setId(tipmotaten1.getId());
        assertThat(tipmotaten1).isEqualTo(tipmotaten2);
        tipmotaten2.setId(2L);
        assertThat(tipmotaten1).isNotEqualTo(tipmotaten2);
        tipmotaten1.setId(null);
        assertThat(tipmotaten1).isNotEqualTo(tipmotaten2);
    }
}
