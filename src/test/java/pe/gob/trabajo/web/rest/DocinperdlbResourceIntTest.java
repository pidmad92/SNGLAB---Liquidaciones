package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Docinperdlb;
import pe.gob.trabajo.repository.DocinperdlbRepository;
import pe.gob.trabajo.repository.search.DocinperdlbSearchRepository;
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
 * Test class for the DocinperdlbResource REST controller.
 *
 * @see DocinperdlbResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class DocinperdlbResourceIntTest {

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
    private DocinperdlbRepository docinperdlbRepository;

    @Autowired
    private DocinperdlbSearchRepository docinperdlbSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDocinperdlbMockMvc;

    private Docinperdlb docinperdlb;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocinperdlbResource docinperdlbResource = new DocinperdlbResource(docinperdlbRepository, docinperdlbSearchRepository);
        this.restDocinperdlbMockMvc = MockMvcBuilders.standaloneSetup(docinperdlbResource)
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
    public static Docinperdlb createEntity(EntityManager em) {
        Docinperdlb docinperdlb = new Docinperdlb()
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return docinperdlb;
    }

    @Before
    public void initTest() {
        docinperdlbSearchRepository.deleteAll();
        docinperdlb = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocinperdlb() throws Exception {
        int databaseSizeBeforeCreate = docinperdlbRepository.findAll().size();

        // Create the Docinperdlb
        restDocinperdlbMockMvc.perform(post("/api/docinperdlbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docinperdlb)))
            .andExpect(status().isCreated());

        // Validate the Docinperdlb in the database
        List<Docinperdlb> docinperdlbList = docinperdlbRepository.findAll();
        assertThat(docinperdlbList).hasSize(databaseSizeBeforeCreate + 1);
        Docinperdlb testDocinperdlb = docinperdlbList.get(docinperdlbList.size() - 1);
        assertThat(testDocinperdlb.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testDocinperdlb.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testDocinperdlb.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testDocinperdlb.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testDocinperdlb.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testDocinperdlb.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testDocinperdlb.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Docinperdlb in Elasticsearch
        Docinperdlb docinperdlbEs = docinperdlbSearchRepository.findOne(testDocinperdlb.getId());
        assertThat(docinperdlbEs).isEqualToComparingFieldByField(testDocinperdlb);
    }

    @Test
    @Transactional
    public void createDocinperdlbWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = docinperdlbRepository.findAll().size();

        // Create the Docinperdlb with an existing ID
        docinperdlb.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocinperdlbMockMvc.perform(post("/api/docinperdlbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docinperdlb)))
            .andExpect(status().isBadRequest());

        // Validate the Docinperdlb in the database
        List<Docinperdlb> docinperdlbList = docinperdlbRepository.findAll();
        assertThat(docinperdlbList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = docinperdlbRepository.findAll().size();
        // set the field null
        docinperdlb.setnUsuareg(null);

        // Create the Docinperdlb, which fails.

        restDocinperdlbMockMvc.perform(post("/api/docinperdlbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docinperdlb)))
            .andExpect(status().isBadRequest());

        List<Docinperdlb> docinperdlbList = docinperdlbRepository.findAll();
        assertThat(docinperdlbList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = docinperdlbRepository.findAll().size();
        // set the field null
        docinperdlb.settFecreg(null);

        // Create the Docinperdlb, which fails.

        restDocinperdlbMockMvc.perform(post("/api/docinperdlbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docinperdlb)))
            .andExpect(status().isBadRequest());

        List<Docinperdlb> docinperdlbList = docinperdlbRepository.findAll();
        assertThat(docinperdlbList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = docinperdlbRepository.findAll().size();
        // set the field null
        docinperdlb.setnFlgactivo(null);

        // Create the Docinperdlb, which fails.

        restDocinperdlbMockMvc.perform(post("/api/docinperdlbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docinperdlb)))
            .andExpect(status().isBadRequest());

        List<Docinperdlb> docinperdlbList = docinperdlbRepository.findAll();
        assertThat(docinperdlbList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = docinperdlbRepository.findAll().size();
        // set the field null
        docinperdlb.setnSedereg(null);

        // Create the Docinperdlb, which fails.

        restDocinperdlbMockMvc.perform(post("/api/docinperdlbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docinperdlb)))
            .andExpect(status().isBadRequest());

        List<Docinperdlb> docinperdlbList = docinperdlbRepository.findAll();
        assertThat(docinperdlbList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocinperdlbs() throws Exception {
        // Initialize the database
        docinperdlbRepository.saveAndFlush(docinperdlb);

        // Get all the docinperdlbList
        restDocinperdlbMockMvc.perform(get("/api/docinperdlbs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docinperdlb.getId().intValue())))
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
    public void getDocinperdlb() throws Exception {
        // Initialize the database
        docinperdlbRepository.saveAndFlush(docinperdlb);

        // Get the docinperdlb
        restDocinperdlbMockMvc.perform(get("/api/docinperdlbs/{id}", docinperdlb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(docinperdlb.getId().intValue()))
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
    public void getNonExistingDocinperdlb() throws Exception {
        // Get the docinperdlb
        restDocinperdlbMockMvc.perform(get("/api/docinperdlbs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocinperdlb() throws Exception {
        // Initialize the database
        docinperdlbRepository.saveAndFlush(docinperdlb);
        docinperdlbSearchRepository.save(docinperdlb);
        int databaseSizeBeforeUpdate = docinperdlbRepository.findAll().size();

        // Update the docinperdlb
        Docinperdlb updatedDocinperdlb = docinperdlbRepository.findOne(docinperdlb.getId());
        updatedDocinperdlb
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restDocinperdlbMockMvc.perform(put("/api/docinperdlbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocinperdlb)))
            .andExpect(status().isOk());

        // Validate the Docinperdlb in the database
        List<Docinperdlb> docinperdlbList = docinperdlbRepository.findAll();
        assertThat(docinperdlbList).hasSize(databaseSizeBeforeUpdate);
        Docinperdlb testDocinperdlb = docinperdlbList.get(docinperdlbList.size() - 1);
        assertThat(testDocinperdlb.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testDocinperdlb.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testDocinperdlb.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testDocinperdlb.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testDocinperdlb.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testDocinperdlb.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testDocinperdlb.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Docinperdlb in Elasticsearch
        Docinperdlb docinperdlbEs = docinperdlbSearchRepository.findOne(testDocinperdlb.getId());
        assertThat(docinperdlbEs).isEqualToComparingFieldByField(testDocinperdlb);
    }

    @Test
    @Transactional
    public void updateNonExistingDocinperdlb() throws Exception {
        int databaseSizeBeforeUpdate = docinperdlbRepository.findAll().size();

        // Create the Docinperdlb

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDocinperdlbMockMvc.perform(put("/api/docinperdlbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docinperdlb)))
            .andExpect(status().isCreated());

        // Validate the Docinperdlb in the database
        List<Docinperdlb> docinperdlbList = docinperdlbRepository.findAll();
        assertThat(docinperdlbList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDocinperdlb() throws Exception {
        // Initialize the database
        docinperdlbRepository.saveAndFlush(docinperdlb);
        docinperdlbSearchRepository.save(docinperdlb);
        int databaseSizeBeforeDelete = docinperdlbRepository.findAll().size();

        // Get the docinperdlb
        restDocinperdlbMockMvc.perform(delete("/api/docinperdlbs/{id}", docinperdlb.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean docinperdlbExistsInEs = docinperdlbSearchRepository.exists(docinperdlb.getId());
        assertThat(docinperdlbExistsInEs).isFalse();

        // Validate the database is empty
        List<Docinperdlb> docinperdlbList = docinperdlbRepository.findAll();
        assertThat(docinperdlbList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDocinperdlb() throws Exception {
        // Initialize the database
        docinperdlbRepository.saveAndFlush(docinperdlb);
        docinperdlbSearchRepository.save(docinperdlb);

        // Search the docinperdlb
        restDocinperdlbMockMvc.perform(get("/api/_search/docinperdlbs?query=id:" + docinperdlb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docinperdlb.getId().intValue())))
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
        TestUtil.equalsVerifier(Docinperdlb.class);
        Docinperdlb docinperdlb1 = new Docinperdlb();
        docinperdlb1.setId(1L);
        Docinperdlb docinperdlb2 = new Docinperdlb();
        docinperdlb2.setId(docinperdlb1.getId());
        assertThat(docinperdlb1).isEqualTo(docinperdlb2);
        docinperdlb2.setId(2L);
        assertThat(docinperdlb1).isNotEqualTo(docinperdlb2);
        docinperdlb1.setId(null);
        assertThat(docinperdlb1).isNotEqualTo(docinperdlb2);
    }
}
