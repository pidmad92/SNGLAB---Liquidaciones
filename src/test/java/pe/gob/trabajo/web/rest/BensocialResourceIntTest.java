package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Bensocial;
import pe.gob.trabajo.repository.BensocialRepository;
import pe.gob.trabajo.repository.search.BensocialSearchRepository;
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
 * Test class for the BensocialResource REST controller.
 *
 * @see BensocialResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class BensocialResourceIntTest {

    private static final String DEFAULT_V_BENSOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_V_BENSOCIAL = "BBBBBBBBBB";

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
    private BensocialRepository bensocialRepository;

    @Autowired
    private BensocialSearchRepository bensocialSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBensocialMockMvc;

    private Bensocial bensocial;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BensocialResource bensocialResource = new BensocialResource(bensocialRepository, bensocialSearchRepository);
        this.restBensocialMockMvc = MockMvcBuilders.standaloneSetup(bensocialResource)
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
    public static Bensocial createEntity(EntityManager em) {
        Bensocial bensocial = new Bensocial()
            .vBensocial(DEFAULT_V_BENSOCIAL)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return bensocial;
    }

    @Before
    public void initTest() {
        bensocialSearchRepository.deleteAll();
        bensocial = createEntity(em);
    }

    @Test
    @Transactional
    public void createBensocial() throws Exception {
        int databaseSizeBeforeCreate = bensocialRepository.findAll().size();

        // Create the Bensocial
        restBensocialMockMvc.perform(post("/api/bensocials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bensocial)))
            .andExpect(status().isCreated());

        // Validate the Bensocial in the database
        List<Bensocial> bensocialList = bensocialRepository.findAll();
        assertThat(bensocialList).hasSize(databaseSizeBeforeCreate + 1);
        Bensocial testBensocial = bensocialList.get(bensocialList.size() - 1);
        assertThat(testBensocial.getvBensocial()).isEqualTo(DEFAULT_V_BENSOCIAL);
        assertThat(testBensocial.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testBensocial.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testBensocial.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testBensocial.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testBensocial.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testBensocial.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testBensocial.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Bensocial in Elasticsearch
        Bensocial bensocialEs = bensocialSearchRepository.findOne(testBensocial.getId());
        assertThat(bensocialEs).isEqualToComparingFieldByField(testBensocial);
    }

    @Test
    @Transactional
    public void createBensocialWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bensocialRepository.findAll().size();

        // Create the Bensocial with an existing ID
        bensocial.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBensocialMockMvc.perform(post("/api/bensocials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bensocial)))
            .andExpect(status().isBadRequest());

        // Validate the Bensocial in the database
        List<Bensocial> bensocialList = bensocialRepository.findAll();
        assertThat(bensocialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvBensocialIsRequired() throws Exception {
        int databaseSizeBeforeTest = bensocialRepository.findAll().size();
        // set the field null
        bensocial.setvBensocial(null);

        // Create the Bensocial, which fails.

        restBensocialMockMvc.perform(post("/api/bensocials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bensocial)))
            .andExpect(status().isBadRequest());

        List<Bensocial> bensocialList = bensocialRepository.findAll();
        assertThat(bensocialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = bensocialRepository.findAll().size();
        // set the field null
        bensocial.setnUsuareg(null);

        // Create the Bensocial, which fails.

        restBensocialMockMvc.perform(post("/api/bensocials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bensocial)))
            .andExpect(status().isBadRequest());

        List<Bensocial> bensocialList = bensocialRepository.findAll();
        assertThat(bensocialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = bensocialRepository.findAll().size();
        // set the field null
        bensocial.settFecreg(null);

        // Create the Bensocial, which fails.

        restBensocialMockMvc.perform(post("/api/bensocials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bensocial)))
            .andExpect(status().isBadRequest());

        List<Bensocial> bensocialList = bensocialRepository.findAll();
        assertThat(bensocialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = bensocialRepository.findAll().size();
        // set the field null
        bensocial.setnFlgactivo(null);

        // Create the Bensocial, which fails.

        restBensocialMockMvc.perform(post("/api/bensocials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bensocial)))
            .andExpect(status().isBadRequest());

        List<Bensocial> bensocialList = bensocialRepository.findAll();
        assertThat(bensocialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = bensocialRepository.findAll().size();
        // set the field null
        bensocial.setnSedereg(null);

        // Create the Bensocial, which fails.

        restBensocialMockMvc.perform(post("/api/bensocials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bensocial)))
            .andExpect(status().isBadRequest());

        List<Bensocial> bensocialList = bensocialRepository.findAll();
        assertThat(bensocialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBensocials() throws Exception {
        // Initialize the database
        bensocialRepository.saveAndFlush(bensocial);

        // Get all the bensocialList
        restBensocialMockMvc.perform(get("/api/bensocials?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bensocial.getId().intValue())))
            .andExpect(jsonPath("$.[*].vBensocial").value(hasItem(DEFAULT_V_BENSOCIAL.toString())))
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
    public void getBensocial() throws Exception {
        // Initialize the database
        bensocialRepository.saveAndFlush(bensocial);

        // Get the bensocial
        restBensocialMockMvc.perform(get("/api/bensocials/{id}", bensocial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bensocial.getId().intValue()))
            .andExpect(jsonPath("$.vBensocial").value(DEFAULT_V_BENSOCIAL.toString()))
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
    public void getNonExistingBensocial() throws Exception {
        // Get the bensocial
        restBensocialMockMvc.perform(get("/api/bensocials/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBensocial() throws Exception {
        // Initialize the database
        bensocialRepository.saveAndFlush(bensocial);
        bensocialSearchRepository.save(bensocial);
        int databaseSizeBeforeUpdate = bensocialRepository.findAll().size();

        // Update the bensocial
        Bensocial updatedBensocial = bensocialRepository.findOne(bensocial.getId());
        updatedBensocial
            .vBensocial(UPDATED_V_BENSOCIAL)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restBensocialMockMvc.perform(put("/api/bensocials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBensocial)))
            .andExpect(status().isOk());

        // Validate the Bensocial in the database
        List<Bensocial> bensocialList = bensocialRepository.findAll();
        assertThat(bensocialList).hasSize(databaseSizeBeforeUpdate);
        Bensocial testBensocial = bensocialList.get(bensocialList.size() - 1);
        assertThat(testBensocial.getvBensocial()).isEqualTo(UPDATED_V_BENSOCIAL);
        assertThat(testBensocial.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testBensocial.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testBensocial.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testBensocial.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testBensocial.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testBensocial.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testBensocial.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Bensocial in Elasticsearch
        Bensocial bensocialEs = bensocialSearchRepository.findOne(testBensocial.getId());
        assertThat(bensocialEs).isEqualToComparingFieldByField(testBensocial);
    }

    @Test
    @Transactional
    public void updateNonExistingBensocial() throws Exception {
        int databaseSizeBeforeUpdate = bensocialRepository.findAll().size();

        // Create the Bensocial

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBensocialMockMvc.perform(put("/api/bensocials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bensocial)))
            .andExpect(status().isCreated());

        // Validate the Bensocial in the database
        List<Bensocial> bensocialList = bensocialRepository.findAll();
        assertThat(bensocialList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBensocial() throws Exception {
        // Initialize the database
        bensocialRepository.saveAndFlush(bensocial);
        bensocialSearchRepository.save(bensocial);
        int databaseSizeBeforeDelete = bensocialRepository.findAll().size();

        // Get the bensocial
        restBensocialMockMvc.perform(delete("/api/bensocials/{id}", bensocial.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bensocialExistsInEs = bensocialSearchRepository.exists(bensocial.getId());
        assertThat(bensocialExistsInEs).isFalse();

        // Validate the database is empty
        List<Bensocial> bensocialList = bensocialRepository.findAll();
        assertThat(bensocialList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBensocial() throws Exception {
        // Initialize the database
        bensocialRepository.saveAndFlush(bensocial);
        bensocialSearchRepository.save(bensocial);

        // Search the bensocial
        restBensocialMockMvc.perform(get("/api/_search/bensocials?query=id:" + bensocial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bensocial.getId().intValue())))
            .andExpect(jsonPath("$.[*].vBensocial").value(hasItem(DEFAULT_V_BENSOCIAL.toString())))
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
        TestUtil.equalsVerifier(Bensocial.class);
        Bensocial bensocial1 = new Bensocial();
        bensocial1.setId(1L);
        Bensocial bensocial2 = new Bensocial();
        bensocial2.setId(bensocial1.getId());
        assertThat(bensocial1).isEqualTo(bensocial2);
        bensocial2.setId(2L);
        assertThat(bensocial1).isNotEqualTo(bensocial2);
        bensocial1.setId(null);
        assertThat(bensocial1).isNotEqualTo(bensocial2);
    }
}
