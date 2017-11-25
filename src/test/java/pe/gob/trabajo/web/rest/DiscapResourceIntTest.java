package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Discap;
import pe.gob.trabajo.repository.DiscapRepository;
import pe.gob.trabajo.repository.search.DiscapSearchRepository;
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
 * Test class for the DiscapResource REST controller.
 *
 * @see DiscapResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class DiscapResourceIntTest {

    private static final String DEFAULT_V_DESDISCAP = "AAAAAAAAAA";
    private static final String UPDATED_V_DESDISCAP = "BBBBBBBBBB";

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
    private DiscapRepository discapRepository;

    @Autowired
    private DiscapSearchRepository discapSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDiscapMockMvc;

    private Discap discap;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DiscapResource discapResource = new DiscapResource(discapRepository, discapSearchRepository);
        this.restDiscapMockMvc = MockMvcBuilders.standaloneSetup(discapResource)
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
    public static Discap createEntity(EntityManager em) {
        Discap discap = new Discap()
            .vDesdiscap(DEFAULT_V_DESDISCAP)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return discap;
    }

    @Before
    public void initTest() {
        discapSearchRepository.deleteAll();
        discap = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiscap() throws Exception {
        int databaseSizeBeforeCreate = discapRepository.findAll().size();

        // Create the Discap
        restDiscapMockMvc.perform(post("/api/discaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discap)))
            .andExpect(status().isCreated());

        // Validate the Discap in the database
        List<Discap> discapList = discapRepository.findAll();
        assertThat(discapList).hasSize(databaseSizeBeforeCreate + 1);
        Discap testDiscap = discapList.get(discapList.size() - 1);
        assertThat(testDiscap.getvDesdiscap()).isEqualTo(DEFAULT_V_DESDISCAP);
        assertThat(testDiscap.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testDiscap.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testDiscap.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testDiscap.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testDiscap.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testDiscap.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testDiscap.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Discap in Elasticsearch
        Discap discapEs = discapSearchRepository.findOne(testDiscap.getId());
        assertThat(discapEs).isEqualToComparingFieldByField(testDiscap);
    }

    @Test
    @Transactional
    public void createDiscapWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = discapRepository.findAll().size();

        // Create the Discap with an existing ID
        discap.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscapMockMvc.perform(post("/api/discaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discap)))
            .andExpect(status().isBadRequest());

        // Validate the Discap in the database
        List<Discap> discapList = discapRepository.findAll();
        assertThat(discapList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDesdiscapIsRequired() throws Exception {
        int databaseSizeBeforeTest = discapRepository.findAll().size();
        // set the field null
        discap.setvDesdiscap(null);

        // Create the Discap, which fails.

        restDiscapMockMvc.perform(post("/api/discaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discap)))
            .andExpect(status().isBadRequest());

        List<Discap> discapList = discapRepository.findAll();
        assertThat(discapList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = discapRepository.findAll().size();
        // set the field null
        discap.setnUsuareg(null);

        // Create the Discap, which fails.

        restDiscapMockMvc.perform(post("/api/discaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discap)))
            .andExpect(status().isBadRequest());

        List<Discap> discapList = discapRepository.findAll();
        assertThat(discapList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = discapRepository.findAll().size();
        // set the field null
        discap.settFecreg(null);

        // Create the Discap, which fails.

        restDiscapMockMvc.perform(post("/api/discaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discap)))
            .andExpect(status().isBadRequest());

        List<Discap> discapList = discapRepository.findAll();
        assertThat(discapList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = discapRepository.findAll().size();
        // set the field null
        discap.setnFlgactivo(null);

        // Create the Discap, which fails.

        restDiscapMockMvc.perform(post("/api/discaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discap)))
            .andExpect(status().isBadRequest());

        List<Discap> discapList = discapRepository.findAll();
        assertThat(discapList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = discapRepository.findAll().size();
        // set the field null
        discap.setnSedereg(null);

        // Create the Discap, which fails.

        restDiscapMockMvc.perform(post("/api/discaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discap)))
            .andExpect(status().isBadRequest());

        List<Discap> discapList = discapRepository.findAll();
        assertThat(discapList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiscaps() throws Exception {
        // Initialize the database
        discapRepository.saveAndFlush(discap);

        // Get all the discapList
        restDiscapMockMvc.perform(get("/api/discaps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discap.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesdiscap").value(hasItem(DEFAULT_V_DESDISCAP.toString())))
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
    public void getDiscap() throws Exception {
        // Initialize the database
        discapRepository.saveAndFlush(discap);

        // Get the discap
        restDiscapMockMvc.perform(get("/api/discaps/{id}", discap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(discap.getId().intValue()))
            .andExpect(jsonPath("$.vDesdiscap").value(DEFAULT_V_DESDISCAP.toString()))
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
    public void getNonExistingDiscap() throws Exception {
        // Get the discap
        restDiscapMockMvc.perform(get("/api/discaps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscap() throws Exception {
        // Initialize the database
        discapRepository.saveAndFlush(discap);
        discapSearchRepository.save(discap);
        int databaseSizeBeforeUpdate = discapRepository.findAll().size();

        // Update the discap
        Discap updatedDiscap = discapRepository.findOne(discap.getId());
        updatedDiscap
            .vDesdiscap(UPDATED_V_DESDISCAP)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restDiscapMockMvc.perform(put("/api/discaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDiscap)))
            .andExpect(status().isOk());

        // Validate the Discap in the database
        List<Discap> discapList = discapRepository.findAll();
        assertThat(discapList).hasSize(databaseSizeBeforeUpdate);
        Discap testDiscap = discapList.get(discapList.size() - 1);
        assertThat(testDiscap.getvDesdiscap()).isEqualTo(UPDATED_V_DESDISCAP);
        assertThat(testDiscap.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testDiscap.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testDiscap.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testDiscap.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testDiscap.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testDiscap.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testDiscap.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Discap in Elasticsearch
        Discap discapEs = discapSearchRepository.findOne(testDiscap.getId());
        assertThat(discapEs).isEqualToComparingFieldByField(testDiscap);
    }

    @Test
    @Transactional
    public void updateNonExistingDiscap() throws Exception {
        int databaseSizeBeforeUpdate = discapRepository.findAll().size();

        // Create the Discap

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDiscapMockMvc.perform(put("/api/discaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discap)))
            .andExpect(status().isCreated());

        // Validate the Discap in the database
        List<Discap> discapList = discapRepository.findAll();
        assertThat(discapList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDiscap() throws Exception {
        // Initialize the database
        discapRepository.saveAndFlush(discap);
        discapSearchRepository.save(discap);
        int databaseSizeBeforeDelete = discapRepository.findAll().size();

        // Get the discap
        restDiscapMockMvc.perform(delete("/api/discaps/{id}", discap.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean discapExistsInEs = discapSearchRepository.exists(discap.getId());
        assertThat(discapExistsInEs).isFalse();

        // Validate the database is empty
        List<Discap> discapList = discapRepository.findAll();
        assertThat(discapList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDiscap() throws Exception {
        // Initialize the database
        discapRepository.saveAndFlush(discap);
        discapSearchRepository.save(discap);

        // Search the discap
        restDiscapMockMvc.perform(get("/api/_search/discaps?query=id:" + discap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discap.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesdiscap").value(hasItem(DEFAULT_V_DESDISCAP.toString())))
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
        TestUtil.equalsVerifier(Discap.class);
        Discap discap1 = new Discap();
        discap1.setId(1L);
        Discap discap2 = new Discap();
        discap2.setId(discap1.getId());
        assertThat(discap1).isEqualTo(discap2);
        discap2.setId(2L);
        assertThat(discap1).isNotEqualTo(discap2);
        discap1.setId(null);
        assertThat(discap1).isNotEqualTo(discap2);
    }
}
