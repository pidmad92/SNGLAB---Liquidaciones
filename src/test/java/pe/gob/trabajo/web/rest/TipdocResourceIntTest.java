package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Tipdoc;
import pe.gob.trabajo.repository.TipdocRepository;
import pe.gob.trabajo.repository.search.TipdocSearchRepository;
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
 * Test class for the TipdocResource REST controller.
 *
 * @see TipdocResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class TipdocResourceIntTest {

    private static final String DEFAULT_V_DESTIPDOC = "AAAAAAAAAA";
    private static final String UPDATED_V_DESTIPDOC = "BBBBBBBBBB";

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
    private TipdocRepository tipdocRepository;

    @Autowired
    private TipdocSearchRepository tipdocSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTipdocMockMvc;

    private Tipdoc tipdoc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipdocResource tipdocResource = new TipdocResource(tipdocRepository, tipdocSearchRepository);
        this.restTipdocMockMvc = MockMvcBuilders.standaloneSetup(tipdocResource)
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
    public static Tipdoc createEntity(EntityManager em) {
        Tipdoc tipdoc = new Tipdoc()
            .vDestipdoc(DEFAULT_V_DESTIPDOC)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return tipdoc;
    }

    @Before
    public void initTest() {
        tipdocSearchRepository.deleteAll();
        tipdoc = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipdoc() throws Exception {
        int databaseSizeBeforeCreate = tipdocRepository.findAll().size();

        // Create the Tipdoc
        restTipdocMockMvc.perform(post("/api/tipdocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdoc)))
            .andExpect(status().isCreated());

        // Validate the Tipdoc in the database
        List<Tipdoc> tipdocList = tipdocRepository.findAll();
        assertThat(tipdocList).hasSize(databaseSizeBeforeCreate + 1);
        Tipdoc testTipdoc = tipdocList.get(tipdocList.size() - 1);
        assertThat(testTipdoc.getvDestipdoc()).isEqualTo(DEFAULT_V_DESTIPDOC);
        assertThat(testTipdoc.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testTipdoc.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testTipdoc.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testTipdoc.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testTipdoc.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testTipdoc.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testTipdoc.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Tipdoc in Elasticsearch
        Tipdoc tipdocEs = tipdocSearchRepository.findOne(testTipdoc.getId());
        assertThat(tipdocEs).isEqualToComparingFieldByField(testTipdoc);
    }

    @Test
    @Transactional
    public void createTipdocWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipdocRepository.findAll().size();

        // Create the Tipdoc with an existing ID
        tipdoc.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipdocMockMvc.perform(post("/api/tipdocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdoc)))
            .andExpect(status().isBadRequest());

        // Validate the Tipdoc in the database
        List<Tipdoc> tipdocList = tipdocRepository.findAll();
        assertThat(tipdocList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDestipdocIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipdocRepository.findAll().size();
        // set the field null
        tipdoc.setvDestipdoc(null);

        // Create the Tipdoc, which fails.

        restTipdocMockMvc.perform(post("/api/tipdocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdoc)))
            .andExpect(status().isBadRequest());

        List<Tipdoc> tipdocList = tipdocRepository.findAll();
        assertThat(tipdocList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipdocRepository.findAll().size();
        // set the field null
        tipdoc.setnUsuareg(null);

        // Create the Tipdoc, which fails.

        restTipdocMockMvc.perform(post("/api/tipdocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdoc)))
            .andExpect(status().isBadRequest());

        List<Tipdoc> tipdocList = tipdocRepository.findAll();
        assertThat(tipdocList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipdocRepository.findAll().size();
        // set the field null
        tipdoc.settFecreg(null);

        // Create the Tipdoc, which fails.

        restTipdocMockMvc.perform(post("/api/tipdocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdoc)))
            .andExpect(status().isBadRequest());

        List<Tipdoc> tipdocList = tipdocRepository.findAll();
        assertThat(tipdocList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipdocRepository.findAll().size();
        // set the field null
        tipdoc.setnFlgactivo(null);

        // Create the Tipdoc, which fails.

        restTipdocMockMvc.perform(post("/api/tipdocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdoc)))
            .andExpect(status().isBadRequest());

        List<Tipdoc> tipdocList = tipdocRepository.findAll();
        assertThat(tipdocList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipdocRepository.findAll().size();
        // set the field null
        tipdoc.setnSedereg(null);

        // Create the Tipdoc, which fails.

        restTipdocMockMvc.perform(post("/api/tipdocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdoc)))
            .andExpect(status().isBadRequest());

        List<Tipdoc> tipdocList = tipdocRepository.findAll();
        assertThat(tipdocList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipdocs() throws Exception {
        // Initialize the database
        tipdocRepository.saveAndFlush(tipdoc);

        // Get all the tipdocList
        restTipdocMockMvc.perform(get("/api/tipdocs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipdoc.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDestipdoc").value(hasItem(DEFAULT_V_DESTIPDOC.toString())))
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
    public void getTipdoc() throws Exception {
        // Initialize the database
        tipdocRepository.saveAndFlush(tipdoc);

        // Get the tipdoc
        restTipdocMockMvc.perform(get("/api/tipdocs/{id}", tipdoc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipdoc.getId().intValue()))
            .andExpect(jsonPath("$.vDestipdoc").value(DEFAULT_V_DESTIPDOC.toString()))
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
    public void getNonExistingTipdoc() throws Exception {
        // Get the tipdoc
        restTipdocMockMvc.perform(get("/api/tipdocs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipdoc() throws Exception {
        // Initialize the database
        tipdocRepository.saveAndFlush(tipdoc);
        tipdocSearchRepository.save(tipdoc);
        int databaseSizeBeforeUpdate = tipdocRepository.findAll().size();

        // Update the tipdoc
        Tipdoc updatedTipdoc = tipdocRepository.findOne(tipdoc.getId());
        updatedTipdoc
            .vDestipdoc(UPDATED_V_DESTIPDOC)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restTipdocMockMvc.perform(put("/api/tipdocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipdoc)))
            .andExpect(status().isOk());

        // Validate the Tipdoc in the database
        List<Tipdoc> tipdocList = tipdocRepository.findAll();
        assertThat(tipdocList).hasSize(databaseSizeBeforeUpdate);
        Tipdoc testTipdoc = tipdocList.get(tipdocList.size() - 1);
        assertThat(testTipdoc.getvDestipdoc()).isEqualTo(UPDATED_V_DESTIPDOC);
        assertThat(testTipdoc.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testTipdoc.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testTipdoc.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testTipdoc.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testTipdoc.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testTipdoc.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testTipdoc.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Tipdoc in Elasticsearch
        Tipdoc tipdocEs = tipdocSearchRepository.findOne(testTipdoc.getId());
        assertThat(tipdocEs).isEqualToComparingFieldByField(testTipdoc);
    }

    @Test
    @Transactional
    public void updateNonExistingTipdoc() throws Exception {
        int databaseSizeBeforeUpdate = tipdocRepository.findAll().size();

        // Create the Tipdoc

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTipdocMockMvc.perform(put("/api/tipdocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipdoc)))
            .andExpect(status().isCreated());

        // Validate the Tipdoc in the database
        List<Tipdoc> tipdocList = tipdocRepository.findAll();
        assertThat(tipdocList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTipdoc() throws Exception {
        // Initialize the database
        tipdocRepository.saveAndFlush(tipdoc);
        tipdocSearchRepository.save(tipdoc);
        int databaseSizeBeforeDelete = tipdocRepository.findAll().size();

        // Get the tipdoc
        restTipdocMockMvc.perform(delete("/api/tipdocs/{id}", tipdoc.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tipdocExistsInEs = tipdocSearchRepository.exists(tipdoc.getId());
        assertThat(tipdocExistsInEs).isFalse();

        // Validate the database is empty
        List<Tipdoc> tipdocList = tipdocRepository.findAll();
        assertThat(tipdocList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTipdoc() throws Exception {
        // Initialize the database
        tipdocRepository.saveAndFlush(tipdoc);
        tipdocSearchRepository.save(tipdoc);

        // Search the tipdoc
        restTipdocMockMvc.perform(get("/api/_search/tipdocs?query=id:" + tipdoc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipdoc.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDestipdoc").value(hasItem(DEFAULT_V_DESTIPDOC.toString())))
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
        TestUtil.equalsVerifier(Tipdoc.class);
        Tipdoc tipdoc1 = new Tipdoc();
        tipdoc1.setId(1L);
        Tipdoc tipdoc2 = new Tipdoc();
        tipdoc2.setId(tipdoc1.getId());
        assertThat(tipdoc1).isEqualTo(tipdoc2);
        tipdoc2.setId(2L);
        assertThat(tipdoc1).isNotEqualTo(tipdoc2);
        tipdoc1.setId(null);
        assertThat(tipdoc1).isNotEqualTo(tipdoc2);
    }
}
