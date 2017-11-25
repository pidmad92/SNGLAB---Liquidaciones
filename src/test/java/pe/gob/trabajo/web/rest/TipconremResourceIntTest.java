package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Tipconrem;
import pe.gob.trabajo.repository.TipconremRepository;
import pe.gob.trabajo.repository.search.TipconremSearchRepository;
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
 * Test class for the TipconremResource REST controller.
 *
 * @see TipconremResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class TipconremResourceIntTest {

    private static final String DEFAULT_V_NOMTIPCR = "AAAAAAAAAA";
    private static final String UPDATED_V_NOMTIPCR = "BBBBBBBBBB";

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
    private TipconremRepository tipconremRepository;

    @Autowired
    private TipconremSearchRepository tipconremSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTipconremMockMvc;

    private Tipconrem tipconrem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipconremResource tipconremResource = new TipconremResource(tipconremRepository, tipconremSearchRepository);
        this.restTipconremMockMvc = MockMvcBuilders.standaloneSetup(tipconremResource)
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
    public static Tipconrem createEntity(EntityManager em) {
        Tipconrem tipconrem = new Tipconrem()
            .vNomtipcr(DEFAULT_V_NOMTIPCR)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return tipconrem;
    }

    @Before
    public void initTest() {
        tipconremSearchRepository.deleteAll();
        tipconrem = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipconrem() throws Exception {
        int databaseSizeBeforeCreate = tipconremRepository.findAll().size();

        // Create the Tipconrem
        restTipconremMockMvc.perform(post("/api/tipconrems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipconrem)))
            .andExpect(status().isCreated());

        // Validate the Tipconrem in the database
        List<Tipconrem> tipconremList = tipconremRepository.findAll();
        assertThat(tipconremList).hasSize(databaseSizeBeforeCreate + 1);
        Tipconrem testTipconrem = tipconremList.get(tipconremList.size() - 1);
        assertThat(testTipconrem.getvNomtipcr()).isEqualTo(DEFAULT_V_NOMTIPCR);
        assertThat(testTipconrem.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testTipconrem.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testTipconrem.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testTipconrem.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testTipconrem.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testTipconrem.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testTipconrem.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Tipconrem in Elasticsearch
        Tipconrem tipconremEs = tipconremSearchRepository.findOne(testTipconrem.getId());
        assertThat(tipconremEs).isEqualToComparingFieldByField(testTipconrem);
    }

    @Test
    @Transactional
    public void createTipconremWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipconremRepository.findAll().size();

        // Create the Tipconrem with an existing ID
        tipconrem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipconremMockMvc.perform(post("/api/tipconrems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipconrem)))
            .andExpect(status().isBadRequest());

        // Validate the Tipconrem in the database
        List<Tipconrem> tipconremList = tipconremRepository.findAll();
        assertThat(tipconremList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvNomtipcrIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipconremRepository.findAll().size();
        // set the field null
        tipconrem.setvNomtipcr(null);

        // Create the Tipconrem, which fails.

        restTipconremMockMvc.perform(post("/api/tipconrems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipconrem)))
            .andExpect(status().isBadRequest());

        List<Tipconrem> tipconremList = tipconremRepository.findAll();
        assertThat(tipconremList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipconremRepository.findAll().size();
        // set the field null
        tipconrem.setnUsuareg(null);

        // Create the Tipconrem, which fails.

        restTipconremMockMvc.perform(post("/api/tipconrems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipconrem)))
            .andExpect(status().isBadRequest());

        List<Tipconrem> tipconremList = tipconremRepository.findAll();
        assertThat(tipconremList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipconremRepository.findAll().size();
        // set the field null
        tipconrem.settFecreg(null);

        // Create the Tipconrem, which fails.

        restTipconremMockMvc.perform(post("/api/tipconrems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipconrem)))
            .andExpect(status().isBadRequest());

        List<Tipconrem> tipconremList = tipconremRepository.findAll();
        assertThat(tipconremList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipconremRepository.findAll().size();
        // set the field null
        tipconrem.setnFlgactivo(null);

        // Create the Tipconrem, which fails.

        restTipconremMockMvc.perform(post("/api/tipconrems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipconrem)))
            .andExpect(status().isBadRequest());

        List<Tipconrem> tipconremList = tipconremRepository.findAll();
        assertThat(tipconremList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipconremRepository.findAll().size();
        // set the field null
        tipconrem.setnSedereg(null);

        // Create the Tipconrem, which fails.

        restTipconremMockMvc.perform(post("/api/tipconrems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipconrem)))
            .andExpect(status().isBadRequest());

        List<Tipconrem> tipconremList = tipconremRepository.findAll();
        assertThat(tipconremList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipconrems() throws Exception {
        // Initialize the database
        tipconremRepository.saveAndFlush(tipconrem);

        // Get all the tipconremList
        restTipconremMockMvc.perform(get("/api/tipconrems?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipconrem.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNomtipcr").value(hasItem(DEFAULT_V_NOMTIPCR.toString())))
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
    public void getTipconrem() throws Exception {
        // Initialize the database
        tipconremRepository.saveAndFlush(tipconrem);

        // Get the tipconrem
        restTipconremMockMvc.perform(get("/api/tipconrems/{id}", tipconrem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipconrem.getId().intValue()))
            .andExpect(jsonPath("$.vNomtipcr").value(DEFAULT_V_NOMTIPCR.toString()))
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
    public void getNonExistingTipconrem() throws Exception {
        // Get the tipconrem
        restTipconremMockMvc.perform(get("/api/tipconrems/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipconrem() throws Exception {
        // Initialize the database
        tipconremRepository.saveAndFlush(tipconrem);
        tipconremSearchRepository.save(tipconrem);
        int databaseSizeBeforeUpdate = tipconremRepository.findAll().size();

        // Update the tipconrem
        Tipconrem updatedTipconrem = tipconremRepository.findOne(tipconrem.getId());
        updatedTipconrem
            .vNomtipcr(UPDATED_V_NOMTIPCR)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restTipconremMockMvc.perform(put("/api/tipconrems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipconrem)))
            .andExpect(status().isOk());

        // Validate the Tipconrem in the database
        List<Tipconrem> tipconremList = tipconremRepository.findAll();
        assertThat(tipconremList).hasSize(databaseSizeBeforeUpdate);
        Tipconrem testTipconrem = tipconremList.get(tipconremList.size() - 1);
        assertThat(testTipconrem.getvNomtipcr()).isEqualTo(UPDATED_V_NOMTIPCR);
        assertThat(testTipconrem.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testTipconrem.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testTipconrem.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testTipconrem.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testTipconrem.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testTipconrem.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testTipconrem.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Tipconrem in Elasticsearch
        Tipconrem tipconremEs = tipconremSearchRepository.findOne(testTipconrem.getId());
        assertThat(tipconremEs).isEqualToComparingFieldByField(testTipconrem);
    }

    @Test
    @Transactional
    public void updateNonExistingTipconrem() throws Exception {
        int databaseSizeBeforeUpdate = tipconremRepository.findAll().size();

        // Create the Tipconrem

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTipconremMockMvc.perform(put("/api/tipconrems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipconrem)))
            .andExpect(status().isCreated());

        // Validate the Tipconrem in the database
        List<Tipconrem> tipconremList = tipconremRepository.findAll();
        assertThat(tipconremList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTipconrem() throws Exception {
        // Initialize the database
        tipconremRepository.saveAndFlush(tipconrem);
        tipconremSearchRepository.save(tipconrem);
        int databaseSizeBeforeDelete = tipconremRepository.findAll().size();

        // Get the tipconrem
        restTipconremMockMvc.perform(delete("/api/tipconrems/{id}", tipconrem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tipconremExistsInEs = tipconremSearchRepository.exists(tipconrem.getId());
        assertThat(tipconremExistsInEs).isFalse();

        // Validate the database is empty
        List<Tipconrem> tipconremList = tipconremRepository.findAll();
        assertThat(tipconremList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTipconrem() throws Exception {
        // Initialize the database
        tipconremRepository.saveAndFlush(tipconrem);
        tipconremSearchRepository.save(tipconrem);

        // Search the tipconrem
        restTipconremMockMvc.perform(get("/api/_search/tipconrems?query=id:" + tipconrem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipconrem.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNomtipcr").value(hasItem(DEFAULT_V_NOMTIPCR.toString())))
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
        TestUtil.equalsVerifier(Tipconrem.class);
        Tipconrem tipconrem1 = new Tipconrem();
        tipconrem1.setId(1L);
        Tipconrem tipconrem2 = new Tipconrem();
        tipconrem2.setId(tipconrem1.getId());
        assertThat(tipconrem1).isEqualTo(tipconrem2);
        tipconrem2.setId(2L);
        assertThat(tipconrem1).isNotEqualTo(tipconrem2);
        tipconrem1.setId(null);
        assertThat(tipconrem1).isNotEqualTo(tipconrem2);
    }
}
