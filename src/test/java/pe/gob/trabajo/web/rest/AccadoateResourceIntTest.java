package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Accadoate;
import pe.gob.trabajo.repository.AccadoateRepository;
import pe.gob.trabajo.repository.search.AccadoateSearchRepository;
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
 * Test class for the AccadoateResource REST controller.
 *
 * @see AccadoateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class AccadoateResourceIntTest {

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
    private AccadoateRepository accadoateRepository;

    @Autowired
    private AccadoateSearchRepository accadoateSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccadoateMockMvc;

    private Accadoate accadoate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AccadoateResource accadoateResource = new AccadoateResource(accadoateRepository, accadoateSearchRepository);
        this.restAccadoateMockMvc = MockMvcBuilders.standaloneSetup(accadoateResource)
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
    public static Accadoate createEntity(EntityManager em) {
        Accadoate accadoate = new Accadoate()
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return accadoate;
    }

    @Before
    public void initTest() {
        accadoateSearchRepository.deleteAll();
        accadoate = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccadoate() throws Exception {
        int databaseSizeBeforeCreate = accadoateRepository.findAll().size();

        // Create the Accadoate
        restAccadoateMockMvc.perform(post("/api/accadoates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accadoate)))
            .andExpect(status().isCreated());

        // Validate the Accadoate in the database
        List<Accadoate> accadoateList = accadoateRepository.findAll();
        assertThat(accadoateList).hasSize(databaseSizeBeforeCreate + 1);
        Accadoate testAccadoate = accadoateList.get(accadoateList.size() - 1);
        assertThat(testAccadoate.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testAccadoate.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testAccadoate.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testAccadoate.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testAccadoate.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testAccadoate.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testAccadoate.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Accadoate in Elasticsearch
        Accadoate accadoateEs = accadoateSearchRepository.findOne(testAccadoate.getId());
        assertThat(accadoateEs).isEqualToComparingFieldByField(testAccadoate);
    }

    @Test
    @Transactional
    public void createAccadoateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accadoateRepository.findAll().size();

        // Create the Accadoate with an existing ID
        accadoate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccadoateMockMvc.perform(post("/api/accadoates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accadoate)))
            .andExpect(status().isBadRequest());

        // Validate the Accadoate in the database
        List<Accadoate> accadoateList = accadoateRepository.findAll();
        assertThat(accadoateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = accadoateRepository.findAll().size();
        // set the field null
        accadoate.setnUsuareg(null);

        // Create the Accadoate, which fails.

        restAccadoateMockMvc.perform(post("/api/accadoates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accadoate)))
            .andExpect(status().isBadRequest());

        List<Accadoate> accadoateList = accadoateRepository.findAll();
        assertThat(accadoateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = accadoateRepository.findAll().size();
        // set the field null
        accadoate.settFecreg(null);

        // Create the Accadoate, which fails.

        restAccadoateMockMvc.perform(post("/api/accadoates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accadoate)))
            .andExpect(status().isBadRequest());

        List<Accadoate> accadoateList = accadoateRepository.findAll();
        assertThat(accadoateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = accadoateRepository.findAll().size();
        // set the field null
        accadoate.setnFlgactivo(null);

        // Create the Accadoate, which fails.

        restAccadoateMockMvc.perform(post("/api/accadoates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accadoate)))
            .andExpect(status().isBadRequest());

        List<Accadoate> accadoateList = accadoateRepository.findAll();
        assertThat(accadoateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = accadoateRepository.findAll().size();
        // set the field null
        accadoate.setnSedereg(null);

        // Create the Accadoate, which fails.

        restAccadoateMockMvc.perform(post("/api/accadoates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accadoate)))
            .andExpect(status().isBadRequest());

        List<Accadoate> accadoateList = accadoateRepository.findAll();
        assertThat(accadoateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccadoates() throws Exception {
        // Initialize the database
        accadoateRepository.saveAndFlush(accadoate);

        // Get all the accadoateList
        restAccadoateMockMvc.perform(get("/api/accadoates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accadoate.getId().intValue())))
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
    public void getAccadoate() throws Exception {
        // Initialize the database
        accadoateRepository.saveAndFlush(accadoate);

        // Get the accadoate
        restAccadoateMockMvc.perform(get("/api/accadoates/{id}", accadoate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accadoate.getId().intValue()))
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
    public void getNonExistingAccadoate() throws Exception {
        // Get the accadoate
        restAccadoateMockMvc.perform(get("/api/accadoates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccadoate() throws Exception {
        // Initialize the database
        accadoateRepository.saveAndFlush(accadoate);
        accadoateSearchRepository.save(accadoate);
        int databaseSizeBeforeUpdate = accadoateRepository.findAll().size();

        // Update the accadoate
        Accadoate updatedAccadoate = accadoateRepository.findOne(accadoate.getId());
        updatedAccadoate
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restAccadoateMockMvc.perform(put("/api/accadoates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccadoate)))
            .andExpect(status().isOk());

        // Validate the Accadoate in the database
        List<Accadoate> accadoateList = accadoateRepository.findAll();
        assertThat(accadoateList).hasSize(databaseSizeBeforeUpdate);
        Accadoate testAccadoate = accadoateList.get(accadoateList.size() - 1);
        assertThat(testAccadoate.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testAccadoate.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testAccadoate.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testAccadoate.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testAccadoate.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testAccadoate.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testAccadoate.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Accadoate in Elasticsearch
        Accadoate accadoateEs = accadoateSearchRepository.findOne(testAccadoate.getId());
        assertThat(accadoateEs).isEqualToComparingFieldByField(testAccadoate);
    }

    @Test
    @Transactional
    public void updateNonExistingAccadoate() throws Exception {
        int databaseSizeBeforeUpdate = accadoateRepository.findAll().size();

        // Create the Accadoate

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccadoateMockMvc.perform(put("/api/accadoates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accadoate)))
            .andExpect(status().isCreated());

        // Validate the Accadoate in the database
        List<Accadoate> accadoateList = accadoateRepository.findAll();
        assertThat(accadoateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccadoate() throws Exception {
        // Initialize the database
        accadoateRepository.saveAndFlush(accadoate);
        accadoateSearchRepository.save(accadoate);
        int databaseSizeBeforeDelete = accadoateRepository.findAll().size();

        // Get the accadoate
        restAccadoateMockMvc.perform(delete("/api/accadoates/{id}", accadoate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean accadoateExistsInEs = accadoateSearchRepository.exists(accadoate.getId());
        assertThat(accadoateExistsInEs).isFalse();

        // Validate the database is empty
        List<Accadoate> accadoateList = accadoateRepository.findAll();
        assertThat(accadoateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAccadoate() throws Exception {
        // Initialize the database
        accadoateRepository.saveAndFlush(accadoate);
        accadoateSearchRepository.save(accadoate);

        // Search the accadoate
        restAccadoateMockMvc.perform(get("/api/_search/accadoates?query=id:" + accadoate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accadoate.getId().intValue())))
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
        TestUtil.equalsVerifier(Accadoate.class);
        Accadoate accadoate1 = new Accadoate();
        accadoate1.setId(1L);
        Accadoate accadoate2 = new Accadoate();
        accadoate2.setId(accadoate1.getId());
        assertThat(accadoate1).isEqualTo(accadoate2);
        accadoate2.setId(2L);
        assertThat(accadoate1).isNotEqualTo(accadoate2);
        accadoate1.setId(null);
        assertThat(accadoate1).isNotEqualTo(accadoate2);
    }
}
