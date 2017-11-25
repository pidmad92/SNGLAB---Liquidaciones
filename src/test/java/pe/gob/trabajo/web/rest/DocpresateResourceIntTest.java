package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Docpresate;
import pe.gob.trabajo.repository.DocpresateRepository;
import pe.gob.trabajo.repository.search.DocpresateSearchRepository;
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
 * Test class for the DocpresateResource REST controller.
 *
 * @see DocpresateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class DocpresateResourceIntTest {

    private static final String DEFAULT_V_OBSDOPATE = "AAAAAAAAAA";
    private static final String UPDATED_V_OBSDOPATE = "BBBBBBBBBB";

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
    private DocpresateRepository docpresateRepository;

    @Autowired
    private DocpresateSearchRepository docpresateSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDocpresateMockMvc;

    private Docpresate docpresate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocpresateResource docpresateResource = new DocpresateResource(docpresateRepository, docpresateSearchRepository);
        this.restDocpresateMockMvc = MockMvcBuilders.standaloneSetup(docpresateResource)
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
    public static Docpresate createEntity(EntityManager em) {
        Docpresate docpresate = new Docpresate()
            .vObsdopate(DEFAULT_V_OBSDOPATE)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return docpresate;
    }

    @Before
    public void initTest() {
        docpresateSearchRepository.deleteAll();
        docpresate = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocpresate() throws Exception {
        int databaseSizeBeforeCreate = docpresateRepository.findAll().size();

        // Create the Docpresate
        restDocpresateMockMvc.perform(post("/api/docpresates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docpresate)))
            .andExpect(status().isCreated());

        // Validate the Docpresate in the database
        List<Docpresate> docpresateList = docpresateRepository.findAll();
        assertThat(docpresateList).hasSize(databaseSizeBeforeCreate + 1);
        Docpresate testDocpresate = docpresateList.get(docpresateList.size() - 1);
        assertThat(testDocpresate.getvObsdopate()).isEqualTo(DEFAULT_V_OBSDOPATE);
        assertThat(testDocpresate.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testDocpresate.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testDocpresate.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testDocpresate.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testDocpresate.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testDocpresate.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testDocpresate.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Docpresate in Elasticsearch
        Docpresate docpresateEs = docpresateSearchRepository.findOne(testDocpresate.getId());
        assertThat(docpresateEs).isEqualToComparingFieldByField(testDocpresate);
    }

    @Test
    @Transactional
    public void createDocpresateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = docpresateRepository.findAll().size();

        // Create the Docpresate with an existing ID
        docpresate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocpresateMockMvc.perform(post("/api/docpresates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docpresate)))
            .andExpect(status().isBadRequest());

        // Validate the Docpresate in the database
        List<Docpresate> docpresateList = docpresateRepository.findAll();
        assertThat(docpresateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = docpresateRepository.findAll().size();
        // set the field null
        docpresate.setnUsuareg(null);

        // Create the Docpresate, which fails.

        restDocpresateMockMvc.perform(post("/api/docpresates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docpresate)))
            .andExpect(status().isBadRequest());

        List<Docpresate> docpresateList = docpresateRepository.findAll();
        assertThat(docpresateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = docpresateRepository.findAll().size();
        // set the field null
        docpresate.settFecreg(null);

        // Create the Docpresate, which fails.

        restDocpresateMockMvc.perform(post("/api/docpresates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docpresate)))
            .andExpect(status().isBadRequest());

        List<Docpresate> docpresateList = docpresateRepository.findAll();
        assertThat(docpresateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = docpresateRepository.findAll().size();
        // set the field null
        docpresate.setnFlgactivo(null);

        // Create the Docpresate, which fails.

        restDocpresateMockMvc.perform(post("/api/docpresates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docpresate)))
            .andExpect(status().isBadRequest());

        List<Docpresate> docpresateList = docpresateRepository.findAll();
        assertThat(docpresateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = docpresateRepository.findAll().size();
        // set the field null
        docpresate.setnSedereg(null);

        // Create the Docpresate, which fails.

        restDocpresateMockMvc.perform(post("/api/docpresates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docpresate)))
            .andExpect(status().isBadRequest());

        List<Docpresate> docpresateList = docpresateRepository.findAll();
        assertThat(docpresateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocpresates() throws Exception {
        // Initialize the database
        docpresateRepository.saveAndFlush(docpresate);

        // Get all the docpresateList
        restDocpresateMockMvc.perform(get("/api/docpresates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docpresate.getId().intValue())))
            .andExpect(jsonPath("$.[*].vObsdopate").value(hasItem(DEFAULT_V_OBSDOPATE.toString())))
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
    public void getDocpresate() throws Exception {
        // Initialize the database
        docpresateRepository.saveAndFlush(docpresate);

        // Get the docpresate
        restDocpresateMockMvc.perform(get("/api/docpresates/{id}", docpresate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(docpresate.getId().intValue()))
            .andExpect(jsonPath("$.vObsdopate").value(DEFAULT_V_OBSDOPATE.toString()))
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
    public void getNonExistingDocpresate() throws Exception {
        // Get the docpresate
        restDocpresateMockMvc.perform(get("/api/docpresates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocpresate() throws Exception {
        // Initialize the database
        docpresateRepository.saveAndFlush(docpresate);
        docpresateSearchRepository.save(docpresate);
        int databaseSizeBeforeUpdate = docpresateRepository.findAll().size();

        // Update the docpresate
        Docpresate updatedDocpresate = docpresateRepository.findOne(docpresate.getId());
        updatedDocpresate
            .vObsdopate(UPDATED_V_OBSDOPATE)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restDocpresateMockMvc.perform(put("/api/docpresates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocpresate)))
            .andExpect(status().isOk());

        // Validate the Docpresate in the database
        List<Docpresate> docpresateList = docpresateRepository.findAll();
        assertThat(docpresateList).hasSize(databaseSizeBeforeUpdate);
        Docpresate testDocpresate = docpresateList.get(docpresateList.size() - 1);
        assertThat(testDocpresate.getvObsdopate()).isEqualTo(UPDATED_V_OBSDOPATE);
        assertThat(testDocpresate.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testDocpresate.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testDocpresate.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testDocpresate.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testDocpresate.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testDocpresate.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testDocpresate.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Docpresate in Elasticsearch
        Docpresate docpresateEs = docpresateSearchRepository.findOne(testDocpresate.getId());
        assertThat(docpresateEs).isEqualToComparingFieldByField(testDocpresate);
    }

    @Test
    @Transactional
    public void updateNonExistingDocpresate() throws Exception {
        int databaseSizeBeforeUpdate = docpresateRepository.findAll().size();

        // Create the Docpresate

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDocpresateMockMvc.perform(put("/api/docpresates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docpresate)))
            .andExpect(status().isCreated());

        // Validate the Docpresate in the database
        List<Docpresate> docpresateList = docpresateRepository.findAll();
        assertThat(docpresateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDocpresate() throws Exception {
        // Initialize the database
        docpresateRepository.saveAndFlush(docpresate);
        docpresateSearchRepository.save(docpresate);
        int databaseSizeBeforeDelete = docpresateRepository.findAll().size();

        // Get the docpresate
        restDocpresateMockMvc.perform(delete("/api/docpresates/{id}", docpresate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean docpresateExistsInEs = docpresateSearchRepository.exists(docpresate.getId());
        assertThat(docpresateExistsInEs).isFalse();

        // Validate the database is empty
        List<Docpresate> docpresateList = docpresateRepository.findAll();
        assertThat(docpresateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDocpresate() throws Exception {
        // Initialize the database
        docpresateRepository.saveAndFlush(docpresate);
        docpresateSearchRepository.save(docpresate);

        // Search the docpresate
        restDocpresateMockMvc.perform(get("/api/_search/docpresates?query=id:" + docpresate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docpresate.getId().intValue())))
            .andExpect(jsonPath("$.[*].vObsdopate").value(hasItem(DEFAULT_V_OBSDOPATE.toString())))
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
        TestUtil.equalsVerifier(Docpresate.class);
        Docpresate docpresate1 = new Docpresate();
        docpresate1.setId(1L);
        Docpresate docpresate2 = new Docpresate();
        docpresate2.setId(docpresate1.getId());
        assertThat(docpresate1).isEqualTo(docpresate2);
        docpresate2.setId(2L);
        assertThat(docpresate1).isNotEqualTo(docpresate2);
        docpresate1.setId(null);
        assertThat(docpresate1).isNotEqualTo(docpresate2);
    }
}
