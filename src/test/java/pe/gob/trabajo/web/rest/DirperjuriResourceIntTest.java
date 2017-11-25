package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Dirperjuri;
import pe.gob.trabajo.repository.DirperjuriRepository;
import pe.gob.trabajo.repository.search.DirperjuriSearchRepository;
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
 * Test class for the DirperjuriResource REST controller.
 *
 * @see DirperjuriResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class DirperjuriResourceIntTest {

    private static final Integer DEFAULT_N_CODDEPTO = 1;
    private static final Integer UPDATED_N_CODDEPTO = 2;

    private static final Integer DEFAULT_N_CODPROV = 1;
    private static final Integer UPDATED_N_CODPROV = 2;

    private static final Integer DEFAULT_N_CODDIST = 1;
    private static final Integer UPDATED_N_CODDIST = 2;

    private static final String DEFAULT_V_DIRCOMPLE = "AAAAAAAAAA";
    private static final String UPDATED_V_DIRCOMPLE = "BBBBBBBBBB";

    private static final String DEFAULT_V_REFEREN = "AAAAAAAAAA";
    private static final String UPDATED_V_REFEREN = "BBBBBBBBBB";

    private static final Boolean DEFAULT_N_FLGNOTIFI = false;
    private static final Boolean UPDATED_N_FLGNOTIFI = true;

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
    private DirperjuriRepository dirperjuriRepository;

    @Autowired
    private DirperjuriSearchRepository dirperjuriSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDirperjuriMockMvc;

    private Dirperjuri dirperjuri;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DirperjuriResource dirperjuriResource = new DirperjuriResource(dirperjuriRepository, dirperjuriSearchRepository);
        this.restDirperjuriMockMvc = MockMvcBuilders.standaloneSetup(dirperjuriResource)
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
    public static Dirperjuri createEntity(EntityManager em) {
        Dirperjuri dirperjuri = new Dirperjuri()
            .nCoddepto(DEFAULT_N_CODDEPTO)
            .nCodprov(DEFAULT_N_CODPROV)
            .nCoddist(DEFAULT_N_CODDIST)
            .vDircomple(DEFAULT_V_DIRCOMPLE)
            .vReferen(DEFAULT_V_REFEREN)
            .nFlgnotifi(DEFAULT_N_FLGNOTIFI)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return dirperjuri;
    }

    @Before
    public void initTest() {
        dirperjuriSearchRepository.deleteAll();
        dirperjuri = createEntity(em);
    }

    @Test
    @Transactional
    public void createDirperjuri() throws Exception {
        int databaseSizeBeforeCreate = dirperjuriRepository.findAll().size();

        // Create the Dirperjuri
        restDirperjuriMockMvc.perform(post("/api/dirperjuris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirperjuri)))
            .andExpect(status().isCreated());

        // Validate the Dirperjuri in the database
        List<Dirperjuri> dirperjuriList = dirperjuriRepository.findAll();
        assertThat(dirperjuriList).hasSize(databaseSizeBeforeCreate + 1);
        Dirperjuri testDirperjuri = dirperjuriList.get(dirperjuriList.size() - 1);
        assertThat(testDirperjuri.getnCoddepto()).isEqualTo(DEFAULT_N_CODDEPTO);
        assertThat(testDirperjuri.getnCodprov()).isEqualTo(DEFAULT_N_CODPROV);
        assertThat(testDirperjuri.getnCoddist()).isEqualTo(DEFAULT_N_CODDIST);
        assertThat(testDirperjuri.getvDircomple()).isEqualTo(DEFAULT_V_DIRCOMPLE);
        assertThat(testDirperjuri.getvReferen()).isEqualTo(DEFAULT_V_REFEREN);
        assertThat(testDirperjuri.isnFlgnotifi()).isEqualTo(DEFAULT_N_FLGNOTIFI);
        assertThat(testDirperjuri.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testDirperjuri.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testDirperjuri.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testDirperjuri.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testDirperjuri.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testDirperjuri.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testDirperjuri.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Dirperjuri in Elasticsearch
        Dirperjuri dirperjuriEs = dirperjuriSearchRepository.findOne(testDirperjuri.getId());
        assertThat(dirperjuriEs).isEqualToComparingFieldByField(testDirperjuri);
    }

    @Test
    @Transactional
    public void createDirperjuriWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dirperjuriRepository.findAll().size();

        // Create the Dirperjuri with an existing ID
        dirperjuri.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirperjuriMockMvc.perform(post("/api/dirperjuris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirperjuri)))
            .andExpect(status().isBadRequest());

        // Validate the Dirperjuri in the database
        List<Dirperjuri> dirperjuriList = dirperjuriRepository.findAll();
        assertThat(dirperjuriList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDircompleIsRequired() throws Exception {
        int databaseSizeBeforeTest = dirperjuriRepository.findAll().size();
        // set the field null
        dirperjuri.setvDircomple(null);

        // Create the Dirperjuri, which fails.

        restDirperjuriMockMvc.perform(post("/api/dirperjuris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirperjuri)))
            .andExpect(status().isBadRequest());

        List<Dirperjuri> dirperjuriList = dirperjuriRepository.findAll();
        assertThat(dirperjuriList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgnotifiIsRequired() throws Exception {
        int databaseSizeBeforeTest = dirperjuriRepository.findAll().size();
        // set the field null
        dirperjuri.setnFlgnotifi(null);

        // Create the Dirperjuri, which fails.

        restDirperjuriMockMvc.perform(post("/api/dirperjuris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirperjuri)))
            .andExpect(status().isBadRequest());

        List<Dirperjuri> dirperjuriList = dirperjuriRepository.findAll();
        assertThat(dirperjuriList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = dirperjuriRepository.findAll().size();
        // set the field null
        dirperjuri.setnUsuareg(null);

        // Create the Dirperjuri, which fails.

        restDirperjuriMockMvc.perform(post("/api/dirperjuris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirperjuri)))
            .andExpect(status().isBadRequest());

        List<Dirperjuri> dirperjuriList = dirperjuriRepository.findAll();
        assertThat(dirperjuriList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = dirperjuriRepository.findAll().size();
        // set the field null
        dirperjuri.settFecreg(null);

        // Create the Dirperjuri, which fails.

        restDirperjuriMockMvc.perform(post("/api/dirperjuris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirperjuri)))
            .andExpect(status().isBadRequest());

        List<Dirperjuri> dirperjuriList = dirperjuriRepository.findAll();
        assertThat(dirperjuriList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = dirperjuriRepository.findAll().size();
        // set the field null
        dirperjuri.setnFlgactivo(null);

        // Create the Dirperjuri, which fails.

        restDirperjuriMockMvc.perform(post("/api/dirperjuris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirperjuri)))
            .andExpect(status().isBadRequest());

        List<Dirperjuri> dirperjuriList = dirperjuriRepository.findAll();
        assertThat(dirperjuriList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = dirperjuriRepository.findAll().size();
        // set the field null
        dirperjuri.setnSedereg(null);

        // Create the Dirperjuri, which fails.

        restDirperjuriMockMvc.perform(post("/api/dirperjuris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirperjuri)))
            .andExpect(status().isBadRequest());

        List<Dirperjuri> dirperjuriList = dirperjuriRepository.findAll();
        assertThat(dirperjuriList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDirperjuris() throws Exception {
        // Initialize the database
        dirperjuriRepository.saveAndFlush(dirperjuri);

        // Get all the dirperjuriList
        restDirperjuriMockMvc.perform(get("/api/dirperjuris?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dirperjuri.getId().intValue())))
            .andExpect(jsonPath("$.[*].nCoddepto").value(hasItem(DEFAULT_N_CODDEPTO)))
            .andExpect(jsonPath("$.[*].nCodprov").value(hasItem(DEFAULT_N_CODPROV)))
            .andExpect(jsonPath("$.[*].nCoddist").value(hasItem(DEFAULT_N_CODDIST)))
            .andExpect(jsonPath("$.[*].vDircomple").value(hasItem(DEFAULT_V_DIRCOMPLE.toString())))
            .andExpect(jsonPath("$.[*].vReferen").value(hasItem(DEFAULT_V_REFEREN.toString())))
            .andExpect(jsonPath("$.[*].nFlgnotifi").value(hasItem(DEFAULT_N_FLGNOTIFI.booleanValue())))
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
    public void getDirperjuri() throws Exception {
        // Initialize the database
        dirperjuriRepository.saveAndFlush(dirperjuri);

        // Get the dirperjuri
        restDirperjuriMockMvc.perform(get("/api/dirperjuris/{id}", dirperjuri.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dirperjuri.getId().intValue()))
            .andExpect(jsonPath("$.nCoddepto").value(DEFAULT_N_CODDEPTO))
            .andExpect(jsonPath("$.nCodprov").value(DEFAULT_N_CODPROV))
            .andExpect(jsonPath("$.nCoddist").value(DEFAULT_N_CODDIST))
            .andExpect(jsonPath("$.vDircomple").value(DEFAULT_V_DIRCOMPLE.toString()))
            .andExpect(jsonPath("$.vReferen").value(DEFAULT_V_REFEREN.toString()))
            .andExpect(jsonPath("$.nFlgnotifi").value(DEFAULT_N_FLGNOTIFI.booleanValue()))
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
    public void getNonExistingDirperjuri() throws Exception {
        // Get the dirperjuri
        restDirperjuriMockMvc.perform(get("/api/dirperjuris/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDirperjuri() throws Exception {
        // Initialize the database
        dirperjuriRepository.saveAndFlush(dirperjuri);
        dirperjuriSearchRepository.save(dirperjuri);
        int databaseSizeBeforeUpdate = dirperjuriRepository.findAll().size();

        // Update the dirperjuri
        Dirperjuri updatedDirperjuri = dirperjuriRepository.findOne(dirperjuri.getId());
        updatedDirperjuri
            .nCoddepto(UPDATED_N_CODDEPTO)
            .nCodprov(UPDATED_N_CODPROV)
            .nCoddist(UPDATED_N_CODDIST)
            .vDircomple(UPDATED_V_DIRCOMPLE)
            .vReferen(UPDATED_V_REFEREN)
            .nFlgnotifi(UPDATED_N_FLGNOTIFI)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restDirperjuriMockMvc.perform(put("/api/dirperjuris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDirperjuri)))
            .andExpect(status().isOk());

        // Validate the Dirperjuri in the database
        List<Dirperjuri> dirperjuriList = dirperjuriRepository.findAll();
        assertThat(dirperjuriList).hasSize(databaseSizeBeforeUpdate);
        Dirperjuri testDirperjuri = dirperjuriList.get(dirperjuriList.size() - 1);
        assertThat(testDirperjuri.getnCoddepto()).isEqualTo(UPDATED_N_CODDEPTO);
        assertThat(testDirperjuri.getnCodprov()).isEqualTo(UPDATED_N_CODPROV);
        assertThat(testDirperjuri.getnCoddist()).isEqualTo(UPDATED_N_CODDIST);
        assertThat(testDirperjuri.getvDircomple()).isEqualTo(UPDATED_V_DIRCOMPLE);
        assertThat(testDirperjuri.getvReferen()).isEqualTo(UPDATED_V_REFEREN);
        assertThat(testDirperjuri.isnFlgnotifi()).isEqualTo(UPDATED_N_FLGNOTIFI);
        assertThat(testDirperjuri.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testDirperjuri.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testDirperjuri.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testDirperjuri.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testDirperjuri.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testDirperjuri.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testDirperjuri.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Dirperjuri in Elasticsearch
        Dirperjuri dirperjuriEs = dirperjuriSearchRepository.findOne(testDirperjuri.getId());
        assertThat(dirperjuriEs).isEqualToComparingFieldByField(testDirperjuri);
    }

    @Test
    @Transactional
    public void updateNonExistingDirperjuri() throws Exception {
        int databaseSizeBeforeUpdate = dirperjuriRepository.findAll().size();

        // Create the Dirperjuri

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDirperjuriMockMvc.perform(put("/api/dirperjuris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirperjuri)))
            .andExpect(status().isCreated());

        // Validate the Dirperjuri in the database
        List<Dirperjuri> dirperjuriList = dirperjuriRepository.findAll();
        assertThat(dirperjuriList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDirperjuri() throws Exception {
        // Initialize the database
        dirperjuriRepository.saveAndFlush(dirperjuri);
        dirperjuriSearchRepository.save(dirperjuri);
        int databaseSizeBeforeDelete = dirperjuriRepository.findAll().size();

        // Get the dirperjuri
        restDirperjuriMockMvc.perform(delete("/api/dirperjuris/{id}", dirperjuri.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean dirperjuriExistsInEs = dirperjuriSearchRepository.exists(dirperjuri.getId());
        assertThat(dirperjuriExistsInEs).isFalse();

        // Validate the database is empty
        List<Dirperjuri> dirperjuriList = dirperjuriRepository.findAll();
        assertThat(dirperjuriList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDirperjuri() throws Exception {
        // Initialize the database
        dirperjuriRepository.saveAndFlush(dirperjuri);
        dirperjuriSearchRepository.save(dirperjuri);

        // Search the dirperjuri
        restDirperjuriMockMvc.perform(get("/api/_search/dirperjuris?query=id:" + dirperjuri.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dirperjuri.getId().intValue())))
            .andExpect(jsonPath("$.[*].nCoddepto").value(hasItem(DEFAULT_N_CODDEPTO)))
            .andExpect(jsonPath("$.[*].nCodprov").value(hasItem(DEFAULT_N_CODPROV)))
            .andExpect(jsonPath("$.[*].nCoddist").value(hasItem(DEFAULT_N_CODDIST)))
            .andExpect(jsonPath("$.[*].vDircomple").value(hasItem(DEFAULT_V_DIRCOMPLE.toString())))
            .andExpect(jsonPath("$.[*].vReferen").value(hasItem(DEFAULT_V_REFEREN.toString())))
            .andExpect(jsonPath("$.[*].nFlgnotifi").value(hasItem(DEFAULT_N_FLGNOTIFI.booleanValue())))
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
        TestUtil.equalsVerifier(Dirperjuri.class);
        Dirperjuri dirperjuri1 = new Dirperjuri();
        dirperjuri1.setId(1L);
        Dirperjuri dirperjuri2 = new Dirperjuri();
        dirperjuri2.setId(dirperjuri1.getId());
        assertThat(dirperjuri1).isEqualTo(dirperjuri2);
        dirperjuri2.setId(2L);
        assertThat(dirperjuri1).isNotEqualTo(dirperjuri2);
        dirperjuri1.setId(null);
        assertThat(dirperjuri1).isNotEqualTo(dirperjuri2);
    }
}
