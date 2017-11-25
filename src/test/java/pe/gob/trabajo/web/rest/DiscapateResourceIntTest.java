package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Discapate;
import pe.gob.trabajo.repository.DiscapateRepository;
import pe.gob.trabajo.repository.search.DiscapateSearchRepository;
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
 * Test class for the DiscapateResource REST controller.
 *
 * @see DiscapateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class DiscapateResourceIntTest {

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
    private DiscapateRepository discapateRepository;

    @Autowired
    private DiscapateSearchRepository discapateSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDiscapateMockMvc;

    private Discapate discapate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DiscapateResource discapateResource = new DiscapateResource(discapateRepository, discapateSearchRepository);
        this.restDiscapateMockMvc = MockMvcBuilders.standaloneSetup(discapateResource)
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
    public static Discapate createEntity(EntityManager em) {
        Discapate discapate = new Discapate()
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return discapate;
    }

    @Before
    public void initTest() {
        discapateSearchRepository.deleteAll();
        discapate = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiscapate() throws Exception {
        int databaseSizeBeforeCreate = discapateRepository.findAll().size();

        // Create the Discapate
        restDiscapateMockMvc.perform(post("/api/discapates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discapate)))
            .andExpect(status().isCreated());

        // Validate the Discapate in the database
        List<Discapate> discapateList = discapateRepository.findAll();
        assertThat(discapateList).hasSize(databaseSizeBeforeCreate + 1);
        Discapate testDiscapate = discapateList.get(discapateList.size() - 1);
        assertThat(testDiscapate.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testDiscapate.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testDiscapate.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testDiscapate.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testDiscapate.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testDiscapate.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testDiscapate.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Discapate in Elasticsearch
        Discapate discapateEs = discapateSearchRepository.findOne(testDiscapate.getId());
        assertThat(discapateEs).isEqualToComparingFieldByField(testDiscapate);
    }

    @Test
    @Transactional
    public void createDiscapateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = discapateRepository.findAll().size();

        // Create the Discapate with an existing ID
        discapate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscapateMockMvc.perform(post("/api/discapates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discapate)))
            .andExpect(status().isBadRequest());

        // Validate the Discapate in the database
        List<Discapate> discapateList = discapateRepository.findAll();
        assertThat(discapateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = discapateRepository.findAll().size();
        // set the field null
        discapate.setnUsuareg(null);

        // Create the Discapate, which fails.

        restDiscapateMockMvc.perform(post("/api/discapates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discapate)))
            .andExpect(status().isBadRequest());

        List<Discapate> discapateList = discapateRepository.findAll();
        assertThat(discapateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = discapateRepository.findAll().size();
        // set the field null
        discapate.settFecreg(null);

        // Create the Discapate, which fails.

        restDiscapateMockMvc.perform(post("/api/discapates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discapate)))
            .andExpect(status().isBadRequest());

        List<Discapate> discapateList = discapateRepository.findAll();
        assertThat(discapateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = discapateRepository.findAll().size();
        // set the field null
        discapate.setnFlgactivo(null);

        // Create the Discapate, which fails.

        restDiscapateMockMvc.perform(post("/api/discapates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discapate)))
            .andExpect(status().isBadRequest());

        List<Discapate> discapateList = discapateRepository.findAll();
        assertThat(discapateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = discapateRepository.findAll().size();
        // set the field null
        discapate.setnSedereg(null);

        // Create the Discapate, which fails.

        restDiscapateMockMvc.perform(post("/api/discapates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discapate)))
            .andExpect(status().isBadRequest());

        List<Discapate> discapateList = discapateRepository.findAll();
        assertThat(discapateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiscapates() throws Exception {
        // Initialize the database
        discapateRepository.saveAndFlush(discapate);

        // Get all the discapateList
        restDiscapateMockMvc.perform(get("/api/discapates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discapate.getId().intValue())))
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
    public void getDiscapate() throws Exception {
        // Initialize the database
        discapateRepository.saveAndFlush(discapate);

        // Get the discapate
        restDiscapateMockMvc.perform(get("/api/discapates/{id}", discapate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(discapate.getId().intValue()))
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
    public void getNonExistingDiscapate() throws Exception {
        // Get the discapate
        restDiscapateMockMvc.perform(get("/api/discapates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscapate() throws Exception {
        // Initialize the database
        discapateRepository.saveAndFlush(discapate);
        discapateSearchRepository.save(discapate);
        int databaseSizeBeforeUpdate = discapateRepository.findAll().size();

        // Update the discapate
        Discapate updatedDiscapate = discapateRepository.findOne(discapate.getId());
        updatedDiscapate
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restDiscapateMockMvc.perform(put("/api/discapates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDiscapate)))
            .andExpect(status().isOk());

        // Validate the Discapate in the database
        List<Discapate> discapateList = discapateRepository.findAll();
        assertThat(discapateList).hasSize(databaseSizeBeforeUpdate);
        Discapate testDiscapate = discapateList.get(discapateList.size() - 1);
        assertThat(testDiscapate.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testDiscapate.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testDiscapate.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testDiscapate.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testDiscapate.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testDiscapate.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testDiscapate.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Discapate in Elasticsearch
        Discapate discapateEs = discapateSearchRepository.findOne(testDiscapate.getId());
        assertThat(discapateEs).isEqualToComparingFieldByField(testDiscapate);
    }

    @Test
    @Transactional
    public void updateNonExistingDiscapate() throws Exception {
        int databaseSizeBeforeUpdate = discapateRepository.findAll().size();

        // Create the Discapate

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDiscapateMockMvc.perform(put("/api/discapates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discapate)))
            .andExpect(status().isCreated());

        // Validate the Discapate in the database
        List<Discapate> discapateList = discapateRepository.findAll();
        assertThat(discapateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDiscapate() throws Exception {
        // Initialize the database
        discapateRepository.saveAndFlush(discapate);
        discapateSearchRepository.save(discapate);
        int databaseSizeBeforeDelete = discapateRepository.findAll().size();

        // Get the discapate
        restDiscapateMockMvc.perform(delete("/api/discapates/{id}", discapate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean discapateExistsInEs = discapateSearchRepository.exists(discapate.getId());
        assertThat(discapateExistsInEs).isFalse();

        // Validate the database is empty
        List<Discapate> discapateList = discapateRepository.findAll();
        assertThat(discapateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDiscapate() throws Exception {
        // Initialize the database
        discapateRepository.saveAndFlush(discapate);
        discapateSearchRepository.save(discapate);

        // Search the discapate
        restDiscapateMockMvc.perform(get("/api/_search/discapates?query=id:" + discapate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discapate.getId().intValue())))
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
        TestUtil.equalsVerifier(Discapate.class);
        Discapate discapate1 = new Discapate();
        discapate1.setId(1L);
        Discapate discapate2 = new Discapate();
        discapate2.setId(discapate1.getId());
        assertThat(discapate1).isEqualTo(discapate2);
        discapate2.setId(2L);
        assertThat(discapate1).isNotEqualTo(discapate2);
        discapate1.setId(null);
        assertThat(discapate1).isNotEqualTo(discapate2);
    }
}
