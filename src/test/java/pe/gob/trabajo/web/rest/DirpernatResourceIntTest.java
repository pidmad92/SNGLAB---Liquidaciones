package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Dirpernat;
import pe.gob.trabajo.repository.DirpernatRepository;
import pe.gob.trabajo.repository.search.DirpernatSearchRepository;
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
 * Test class for the DirpernatResource REST controller.
 *
 * @see DirpernatResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class DirpernatResourceIntTest {

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
    private DirpernatRepository dirpernatRepository;

    @Autowired
    private DirpernatSearchRepository dirpernatSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDirpernatMockMvc;

    private Dirpernat dirpernat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DirpernatResource dirpernatResource = new DirpernatResource(dirpernatRepository, dirpernatSearchRepository);
        this.restDirpernatMockMvc = MockMvcBuilders.standaloneSetup(dirpernatResource)
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
    public static Dirpernat createEntity(EntityManager em) {
        Dirpernat dirpernat = new Dirpernat()
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
        return dirpernat;
    }

    @Before
    public void initTest() {
        dirpernatSearchRepository.deleteAll();
        dirpernat = createEntity(em);
    }

    @Test
    @Transactional
    public void createDirpernat() throws Exception {
        int databaseSizeBeforeCreate = dirpernatRepository.findAll().size();

        // Create the Dirpernat
        restDirpernatMockMvc.perform(post("/api/dirpernats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirpernat)))
            .andExpect(status().isCreated());

        // Validate the Dirpernat in the database
        List<Dirpernat> dirpernatList = dirpernatRepository.findAll();
        assertThat(dirpernatList).hasSize(databaseSizeBeforeCreate + 1);
        Dirpernat testDirpernat = dirpernatList.get(dirpernatList.size() - 1);
        assertThat(testDirpernat.getnCoddepto()).isEqualTo(DEFAULT_N_CODDEPTO);
        assertThat(testDirpernat.getnCodprov()).isEqualTo(DEFAULT_N_CODPROV);
        assertThat(testDirpernat.getnCoddist()).isEqualTo(DEFAULT_N_CODDIST);
        assertThat(testDirpernat.getvDircomple()).isEqualTo(DEFAULT_V_DIRCOMPLE);
        assertThat(testDirpernat.getvReferen()).isEqualTo(DEFAULT_V_REFEREN);
        assertThat(testDirpernat.isnFlgnotifi()).isEqualTo(DEFAULT_N_FLGNOTIFI);
        assertThat(testDirpernat.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testDirpernat.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testDirpernat.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testDirpernat.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testDirpernat.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testDirpernat.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testDirpernat.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Dirpernat in Elasticsearch
        Dirpernat dirpernatEs = dirpernatSearchRepository.findOne(testDirpernat.getId());
        assertThat(dirpernatEs).isEqualToComparingFieldByField(testDirpernat);
    }

    @Test
    @Transactional
    public void createDirpernatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dirpernatRepository.findAll().size();

        // Create the Dirpernat with an existing ID
        dirpernat.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirpernatMockMvc.perform(post("/api/dirpernats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirpernat)))
            .andExpect(status().isBadRequest());

        // Validate the Dirpernat in the database
        List<Dirpernat> dirpernatList = dirpernatRepository.findAll();
        assertThat(dirpernatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDircompleIsRequired() throws Exception {
        int databaseSizeBeforeTest = dirpernatRepository.findAll().size();
        // set the field null
        dirpernat.setvDircomple(null);

        // Create the Dirpernat, which fails.

        restDirpernatMockMvc.perform(post("/api/dirpernats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirpernat)))
            .andExpect(status().isBadRequest());

        List<Dirpernat> dirpernatList = dirpernatRepository.findAll();
        assertThat(dirpernatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgnotifiIsRequired() throws Exception {
        int databaseSizeBeforeTest = dirpernatRepository.findAll().size();
        // set the field null
        dirpernat.setnFlgnotifi(null);

        // Create the Dirpernat, which fails.

        restDirpernatMockMvc.perform(post("/api/dirpernats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirpernat)))
            .andExpect(status().isBadRequest());

        List<Dirpernat> dirpernatList = dirpernatRepository.findAll();
        assertThat(dirpernatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = dirpernatRepository.findAll().size();
        // set the field null
        dirpernat.setnUsuareg(null);

        // Create the Dirpernat, which fails.

        restDirpernatMockMvc.perform(post("/api/dirpernats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirpernat)))
            .andExpect(status().isBadRequest());

        List<Dirpernat> dirpernatList = dirpernatRepository.findAll();
        assertThat(dirpernatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = dirpernatRepository.findAll().size();
        // set the field null
        dirpernat.settFecreg(null);

        // Create the Dirpernat, which fails.

        restDirpernatMockMvc.perform(post("/api/dirpernats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirpernat)))
            .andExpect(status().isBadRequest());

        List<Dirpernat> dirpernatList = dirpernatRepository.findAll();
        assertThat(dirpernatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = dirpernatRepository.findAll().size();
        // set the field null
        dirpernat.setnFlgactivo(null);

        // Create the Dirpernat, which fails.

        restDirpernatMockMvc.perform(post("/api/dirpernats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirpernat)))
            .andExpect(status().isBadRequest());

        List<Dirpernat> dirpernatList = dirpernatRepository.findAll();
        assertThat(dirpernatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = dirpernatRepository.findAll().size();
        // set the field null
        dirpernat.setnSedereg(null);

        // Create the Dirpernat, which fails.

        restDirpernatMockMvc.perform(post("/api/dirpernats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirpernat)))
            .andExpect(status().isBadRequest());

        List<Dirpernat> dirpernatList = dirpernatRepository.findAll();
        assertThat(dirpernatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDirpernats() throws Exception {
        // Initialize the database
        dirpernatRepository.saveAndFlush(dirpernat);

        // Get all the dirpernatList
        restDirpernatMockMvc.perform(get("/api/dirpernats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dirpernat.getId().intValue())))
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
    public void getDirpernat() throws Exception {
        // Initialize the database
        dirpernatRepository.saveAndFlush(dirpernat);

        // Get the dirpernat
        restDirpernatMockMvc.perform(get("/api/dirpernats/{id}", dirpernat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dirpernat.getId().intValue()))
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
    public void getNonExistingDirpernat() throws Exception {
        // Get the dirpernat
        restDirpernatMockMvc.perform(get("/api/dirpernats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDirpernat() throws Exception {
        // Initialize the database
        dirpernatRepository.saveAndFlush(dirpernat);
        dirpernatSearchRepository.save(dirpernat);
        int databaseSizeBeforeUpdate = dirpernatRepository.findAll().size();

        // Update the dirpernat
        Dirpernat updatedDirpernat = dirpernatRepository.findOne(dirpernat.getId());
        updatedDirpernat
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

        restDirpernatMockMvc.perform(put("/api/dirpernats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDirpernat)))
            .andExpect(status().isOk());

        // Validate the Dirpernat in the database
        List<Dirpernat> dirpernatList = dirpernatRepository.findAll();
        assertThat(dirpernatList).hasSize(databaseSizeBeforeUpdate);
        Dirpernat testDirpernat = dirpernatList.get(dirpernatList.size() - 1);
        assertThat(testDirpernat.getnCoddepto()).isEqualTo(UPDATED_N_CODDEPTO);
        assertThat(testDirpernat.getnCodprov()).isEqualTo(UPDATED_N_CODPROV);
        assertThat(testDirpernat.getnCoddist()).isEqualTo(UPDATED_N_CODDIST);
        assertThat(testDirpernat.getvDircomple()).isEqualTo(UPDATED_V_DIRCOMPLE);
        assertThat(testDirpernat.getvReferen()).isEqualTo(UPDATED_V_REFEREN);
        assertThat(testDirpernat.isnFlgnotifi()).isEqualTo(UPDATED_N_FLGNOTIFI);
        assertThat(testDirpernat.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testDirpernat.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testDirpernat.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testDirpernat.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testDirpernat.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testDirpernat.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testDirpernat.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Dirpernat in Elasticsearch
        Dirpernat dirpernatEs = dirpernatSearchRepository.findOne(testDirpernat.getId());
        assertThat(dirpernatEs).isEqualToComparingFieldByField(testDirpernat);
    }

    @Test
    @Transactional
    public void updateNonExistingDirpernat() throws Exception {
        int databaseSizeBeforeUpdate = dirpernatRepository.findAll().size();

        // Create the Dirpernat

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDirpernatMockMvc.perform(put("/api/dirpernats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dirpernat)))
            .andExpect(status().isCreated());

        // Validate the Dirpernat in the database
        List<Dirpernat> dirpernatList = dirpernatRepository.findAll();
        assertThat(dirpernatList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDirpernat() throws Exception {
        // Initialize the database
        dirpernatRepository.saveAndFlush(dirpernat);
        dirpernatSearchRepository.save(dirpernat);
        int databaseSizeBeforeDelete = dirpernatRepository.findAll().size();

        // Get the dirpernat
        restDirpernatMockMvc.perform(delete("/api/dirpernats/{id}", dirpernat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean dirpernatExistsInEs = dirpernatSearchRepository.exists(dirpernat.getId());
        assertThat(dirpernatExistsInEs).isFalse();

        // Validate the database is empty
        List<Dirpernat> dirpernatList = dirpernatRepository.findAll();
        assertThat(dirpernatList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDirpernat() throws Exception {
        // Initialize the database
        dirpernatRepository.saveAndFlush(dirpernat);
        dirpernatSearchRepository.save(dirpernat);

        // Search the dirpernat
        restDirpernatMockMvc.perform(get("/api/_search/dirpernats?query=id:" + dirpernat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dirpernat.getId().intValue())))
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
        TestUtil.equalsVerifier(Dirpernat.class);
        Dirpernat dirpernat1 = new Dirpernat();
        dirpernat1.setId(1L);
        Dirpernat dirpernat2 = new Dirpernat();
        dirpernat2.setId(dirpernat1.getId());
        assertThat(dirpernat1).isEqualTo(dirpernat2);
        dirpernat2.setId(2L);
        assertThat(dirpernat1).isNotEqualTo(dirpernat2);
        dirpernat1.setId(null);
        assertThat(dirpernat1).isNotEqualTo(dirpernat2);
    }
}
