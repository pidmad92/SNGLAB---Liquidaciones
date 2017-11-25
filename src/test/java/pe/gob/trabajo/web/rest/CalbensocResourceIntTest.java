package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Calbensoc;
import pe.gob.trabajo.repository.CalbensocRepository;
import pe.gob.trabajo.repository.search.CalbensocSearchRepository;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CalbensocResource REST controller.
 *
 * @see CalbensocResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class CalbensocResourceIntTest {

    private static final BigDecimal DEFAULT_N_CALBENS = new BigDecimal(1);
    private static final BigDecimal UPDATED_N_CALBENS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_N_CALBENS_2 = new BigDecimal(1);
    private static final BigDecimal UPDATED_N_CALBENS_2 = new BigDecimal(2);

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
    private CalbensocRepository calbensocRepository;

    @Autowired
    private CalbensocSearchRepository calbensocSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCalbensocMockMvc;

    private Calbensoc calbensoc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CalbensocResource calbensocResource = new CalbensocResource(calbensocRepository, calbensocSearchRepository);
        this.restCalbensocMockMvc = MockMvcBuilders.standaloneSetup(calbensocResource)
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
    public static Calbensoc createEntity(EntityManager em) {
        Calbensoc calbensoc = new Calbensoc()
            .nCalbens(DEFAULT_N_CALBENS)
            .nCalbens2(DEFAULT_N_CALBENS_2)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return calbensoc;
    }

    @Before
    public void initTest() {
        calbensocSearchRepository.deleteAll();
        calbensoc = createEntity(em);
    }

    @Test
    @Transactional
    public void createCalbensoc() throws Exception {
        int databaseSizeBeforeCreate = calbensocRepository.findAll().size();

        // Create the Calbensoc
        restCalbensocMockMvc.perform(post("/api/calbensocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calbensoc)))
            .andExpect(status().isCreated());

        // Validate the Calbensoc in the database
        List<Calbensoc> calbensocList = calbensocRepository.findAll();
        assertThat(calbensocList).hasSize(databaseSizeBeforeCreate + 1);
        Calbensoc testCalbensoc = calbensocList.get(calbensocList.size() - 1);
        assertThat(testCalbensoc.getnCalbens()).isEqualTo(DEFAULT_N_CALBENS);
        assertThat(testCalbensoc.getnCalbens2()).isEqualTo(DEFAULT_N_CALBENS_2);
        assertThat(testCalbensoc.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testCalbensoc.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testCalbensoc.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testCalbensoc.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testCalbensoc.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testCalbensoc.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testCalbensoc.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Calbensoc in Elasticsearch
        Calbensoc calbensocEs = calbensocSearchRepository.findOne(testCalbensoc.getId());
        assertThat(calbensocEs).isEqualToComparingFieldByField(testCalbensoc);
    }

    @Test
    @Transactional
    public void createCalbensocWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = calbensocRepository.findAll().size();

        // Create the Calbensoc with an existing ID
        calbensoc.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalbensocMockMvc.perform(post("/api/calbensocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calbensoc)))
            .andExpect(status().isBadRequest());

        // Validate the Calbensoc in the database
        List<Calbensoc> calbensocList = calbensocRepository.findAll();
        assertThat(calbensocList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = calbensocRepository.findAll().size();
        // set the field null
        calbensoc.setnUsuareg(null);

        // Create the Calbensoc, which fails.

        restCalbensocMockMvc.perform(post("/api/calbensocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calbensoc)))
            .andExpect(status().isBadRequest());

        List<Calbensoc> calbensocList = calbensocRepository.findAll();
        assertThat(calbensocList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = calbensocRepository.findAll().size();
        // set the field null
        calbensoc.settFecreg(null);

        // Create the Calbensoc, which fails.

        restCalbensocMockMvc.perform(post("/api/calbensocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calbensoc)))
            .andExpect(status().isBadRequest());

        List<Calbensoc> calbensocList = calbensocRepository.findAll();
        assertThat(calbensocList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = calbensocRepository.findAll().size();
        // set the field null
        calbensoc.setnFlgactivo(null);

        // Create the Calbensoc, which fails.

        restCalbensocMockMvc.perform(post("/api/calbensocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calbensoc)))
            .andExpect(status().isBadRequest());

        List<Calbensoc> calbensocList = calbensocRepository.findAll();
        assertThat(calbensocList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = calbensocRepository.findAll().size();
        // set the field null
        calbensoc.setnSedereg(null);

        // Create the Calbensoc, which fails.

        restCalbensocMockMvc.perform(post("/api/calbensocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calbensoc)))
            .andExpect(status().isBadRequest());

        List<Calbensoc> calbensocList = calbensocRepository.findAll();
        assertThat(calbensocList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCalbensocs() throws Exception {
        // Initialize the database
        calbensocRepository.saveAndFlush(calbensoc);

        // Get all the calbensocList
        restCalbensocMockMvc.perform(get("/api/calbensocs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calbensoc.getId().intValue())))
            .andExpect(jsonPath("$.[*].nCalbens").value(hasItem(DEFAULT_N_CALBENS.intValue())))
            .andExpect(jsonPath("$.[*].nCalbens2").value(hasItem(DEFAULT_N_CALBENS_2.intValue())))
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
    public void getCalbensoc() throws Exception {
        // Initialize the database
        calbensocRepository.saveAndFlush(calbensoc);

        // Get the calbensoc
        restCalbensocMockMvc.perform(get("/api/calbensocs/{id}", calbensoc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(calbensoc.getId().intValue()))
            .andExpect(jsonPath("$.nCalbens").value(DEFAULT_N_CALBENS.intValue()))
            .andExpect(jsonPath("$.nCalbens2").value(DEFAULT_N_CALBENS_2.intValue()))
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
    public void getNonExistingCalbensoc() throws Exception {
        // Get the calbensoc
        restCalbensocMockMvc.perform(get("/api/calbensocs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCalbensoc() throws Exception {
        // Initialize the database
        calbensocRepository.saveAndFlush(calbensoc);
        calbensocSearchRepository.save(calbensoc);
        int databaseSizeBeforeUpdate = calbensocRepository.findAll().size();

        // Update the calbensoc
        Calbensoc updatedCalbensoc = calbensocRepository.findOne(calbensoc.getId());
        updatedCalbensoc
            .nCalbens(UPDATED_N_CALBENS)
            .nCalbens2(UPDATED_N_CALBENS_2)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restCalbensocMockMvc.perform(put("/api/calbensocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCalbensoc)))
            .andExpect(status().isOk());

        // Validate the Calbensoc in the database
        List<Calbensoc> calbensocList = calbensocRepository.findAll();
        assertThat(calbensocList).hasSize(databaseSizeBeforeUpdate);
        Calbensoc testCalbensoc = calbensocList.get(calbensocList.size() - 1);
        assertThat(testCalbensoc.getnCalbens()).isEqualTo(UPDATED_N_CALBENS);
        assertThat(testCalbensoc.getnCalbens2()).isEqualTo(UPDATED_N_CALBENS_2);
        assertThat(testCalbensoc.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testCalbensoc.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testCalbensoc.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testCalbensoc.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testCalbensoc.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testCalbensoc.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testCalbensoc.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Calbensoc in Elasticsearch
        Calbensoc calbensocEs = calbensocSearchRepository.findOne(testCalbensoc.getId());
        assertThat(calbensocEs).isEqualToComparingFieldByField(testCalbensoc);
    }

    @Test
    @Transactional
    public void updateNonExistingCalbensoc() throws Exception {
        int databaseSizeBeforeUpdate = calbensocRepository.findAll().size();

        // Create the Calbensoc

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCalbensocMockMvc.perform(put("/api/calbensocs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calbensoc)))
            .andExpect(status().isCreated());

        // Validate the Calbensoc in the database
        List<Calbensoc> calbensocList = calbensocRepository.findAll();
        assertThat(calbensocList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCalbensoc() throws Exception {
        // Initialize the database
        calbensocRepository.saveAndFlush(calbensoc);
        calbensocSearchRepository.save(calbensoc);
        int databaseSizeBeforeDelete = calbensocRepository.findAll().size();

        // Get the calbensoc
        restCalbensocMockMvc.perform(delete("/api/calbensocs/{id}", calbensoc.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean calbensocExistsInEs = calbensocSearchRepository.exists(calbensoc.getId());
        assertThat(calbensocExistsInEs).isFalse();

        // Validate the database is empty
        List<Calbensoc> calbensocList = calbensocRepository.findAll();
        assertThat(calbensocList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCalbensoc() throws Exception {
        // Initialize the database
        calbensocRepository.saveAndFlush(calbensoc);
        calbensocSearchRepository.save(calbensoc);

        // Search the calbensoc
        restCalbensocMockMvc.perform(get("/api/_search/calbensocs?query=id:" + calbensoc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calbensoc.getId().intValue())))
            .andExpect(jsonPath("$.[*].nCalbens").value(hasItem(DEFAULT_N_CALBENS.intValue())))
            .andExpect(jsonPath("$.[*].nCalbens2").value(hasItem(DEFAULT_N_CALBENS_2.intValue())))
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
        TestUtil.equalsVerifier(Calbensoc.class);
        Calbensoc calbensoc1 = new Calbensoc();
        calbensoc1.setId(1L);
        Calbensoc calbensoc2 = new Calbensoc();
        calbensoc2.setId(calbensoc1.getId());
        assertThat(calbensoc1).isEqualTo(calbensoc2);
        calbensoc2.setId(2L);
        assertThat(calbensoc1).isNotEqualTo(calbensoc2);
        calbensoc1.setId(null);
        assertThat(calbensoc1).isNotEqualTo(calbensoc2);
    }
}
