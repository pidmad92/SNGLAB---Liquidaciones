package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Tipvinculo;
import pe.gob.trabajo.repository.TipvinculoRepository;
import pe.gob.trabajo.repository.search.TipvinculoSearchRepository;
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
 * Test class for the TipvinculoResource REST controller.
 *
 * @see TipvinculoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class TipvinculoResourceIntTest {

    private static final String DEFAULT_V_DESTIPVIN = "AAAAAAAAAA";
    private static final String UPDATED_V_DESTIPVIN = "BBBBBBBBBB";

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
    private TipvinculoRepository tipvinculoRepository;

    @Autowired
    private TipvinculoSearchRepository tipvinculoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTipvinculoMockMvc;

    private Tipvinculo tipvinculo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipvinculoResource tipvinculoResource = new TipvinculoResource(tipvinculoRepository, tipvinculoSearchRepository);
        this.restTipvinculoMockMvc = MockMvcBuilders.standaloneSetup(tipvinculoResource)
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
    public static Tipvinculo createEntity(EntityManager em) {
        Tipvinculo tipvinculo = new Tipvinculo()
            .vDestipvin(DEFAULT_V_DESTIPVIN)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return tipvinculo;
    }

    @Before
    public void initTest() {
        tipvinculoSearchRepository.deleteAll();
        tipvinculo = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipvinculo() throws Exception {
        int databaseSizeBeforeCreate = tipvinculoRepository.findAll().size();

        // Create the Tipvinculo
        restTipvinculoMockMvc.perform(post("/api/tipvinculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipvinculo)))
            .andExpect(status().isCreated());

        // Validate the Tipvinculo in the database
        List<Tipvinculo> tipvinculoList = tipvinculoRepository.findAll();
        assertThat(tipvinculoList).hasSize(databaseSizeBeforeCreate + 1);
        Tipvinculo testTipvinculo = tipvinculoList.get(tipvinculoList.size() - 1);
        assertThat(testTipvinculo.getvDestipvin()).isEqualTo(DEFAULT_V_DESTIPVIN);
        assertThat(testTipvinculo.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testTipvinculo.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testTipvinculo.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testTipvinculo.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testTipvinculo.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testTipvinculo.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testTipvinculo.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Tipvinculo in Elasticsearch
        Tipvinculo tipvinculoEs = tipvinculoSearchRepository.findOne(testTipvinculo.getId());
        assertThat(tipvinculoEs).isEqualToComparingFieldByField(testTipvinculo);
    }

    @Test
    @Transactional
    public void createTipvinculoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipvinculoRepository.findAll().size();

        // Create the Tipvinculo with an existing ID
        tipvinculo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipvinculoMockMvc.perform(post("/api/tipvinculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipvinculo)))
            .andExpect(status().isBadRequest());

        // Validate the Tipvinculo in the database
        List<Tipvinculo> tipvinculoList = tipvinculoRepository.findAll();
        assertThat(tipvinculoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDestipvinIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipvinculoRepository.findAll().size();
        // set the field null
        tipvinculo.setvDestipvin(null);

        // Create the Tipvinculo, which fails.

        restTipvinculoMockMvc.perform(post("/api/tipvinculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipvinculo)))
            .andExpect(status().isBadRequest());

        List<Tipvinculo> tipvinculoList = tipvinculoRepository.findAll();
        assertThat(tipvinculoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipvinculoRepository.findAll().size();
        // set the field null
        tipvinculo.setnUsuareg(null);

        // Create the Tipvinculo, which fails.

        restTipvinculoMockMvc.perform(post("/api/tipvinculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipvinculo)))
            .andExpect(status().isBadRequest());

        List<Tipvinculo> tipvinculoList = tipvinculoRepository.findAll();
        assertThat(tipvinculoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipvinculoRepository.findAll().size();
        // set the field null
        tipvinculo.settFecreg(null);

        // Create the Tipvinculo, which fails.

        restTipvinculoMockMvc.perform(post("/api/tipvinculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipvinculo)))
            .andExpect(status().isBadRequest());

        List<Tipvinculo> tipvinculoList = tipvinculoRepository.findAll();
        assertThat(tipvinculoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipvinculoRepository.findAll().size();
        // set the field null
        tipvinculo.setnFlgactivo(null);

        // Create the Tipvinculo, which fails.

        restTipvinculoMockMvc.perform(post("/api/tipvinculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipvinculo)))
            .andExpect(status().isBadRequest());

        List<Tipvinculo> tipvinculoList = tipvinculoRepository.findAll();
        assertThat(tipvinculoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipvinculoRepository.findAll().size();
        // set the field null
        tipvinculo.setnSedereg(null);

        // Create the Tipvinculo, which fails.

        restTipvinculoMockMvc.perform(post("/api/tipvinculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipvinculo)))
            .andExpect(status().isBadRequest());

        List<Tipvinculo> tipvinculoList = tipvinculoRepository.findAll();
        assertThat(tipvinculoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipvinculos() throws Exception {
        // Initialize the database
        tipvinculoRepository.saveAndFlush(tipvinculo);

        // Get all the tipvinculoList
        restTipvinculoMockMvc.perform(get("/api/tipvinculos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipvinculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDestipvin").value(hasItem(DEFAULT_V_DESTIPVIN.toString())))
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
    public void getTipvinculo() throws Exception {
        // Initialize the database
        tipvinculoRepository.saveAndFlush(tipvinculo);

        // Get the tipvinculo
        restTipvinculoMockMvc.perform(get("/api/tipvinculos/{id}", tipvinculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipvinculo.getId().intValue()))
            .andExpect(jsonPath("$.vDestipvin").value(DEFAULT_V_DESTIPVIN.toString()))
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
    public void getNonExistingTipvinculo() throws Exception {
        // Get the tipvinculo
        restTipvinculoMockMvc.perform(get("/api/tipvinculos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipvinculo() throws Exception {
        // Initialize the database
        tipvinculoRepository.saveAndFlush(tipvinculo);
        tipvinculoSearchRepository.save(tipvinculo);
        int databaseSizeBeforeUpdate = tipvinculoRepository.findAll().size();

        // Update the tipvinculo
        Tipvinculo updatedTipvinculo = tipvinculoRepository.findOne(tipvinculo.getId());
        updatedTipvinculo
            .vDestipvin(UPDATED_V_DESTIPVIN)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restTipvinculoMockMvc.perform(put("/api/tipvinculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipvinculo)))
            .andExpect(status().isOk());

        // Validate the Tipvinculo in the database
        List<Tipvinculo> tipvinculoList = tipvinculoRepository.findAll();
        assertThat(tipvinculoList).hasSize(databaseSizeBeforeUpdate);
        Tipvinculo testTipvinculo = tipvinculoList.get(tipvinculoList.size() - 1);
        assertThat(testTipvinculo.getvDestipvin()).isEqualTo(UPDATED_V_DESTIPVIN);
        assertThat(testTipvinculo.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testTipvinculo.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testTipvinculo.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testTipvinculo.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testTipvinculo.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testTipvinculo.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testTipvinculo.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Tipvinculo in Elasticsearch
        Tipvinculo tipvinculoEs = tipvinculoSearchRepository.findOne(testTipvinculo.getId());
        assertThat(tipvinculoEs).isEqualToComparingFieldByField(testTipvinculo);
    }

    @Test
    @Transactional
    public void updateNonExistingTipvinculo() throws Exception {
        int databaseSizeBeforeUpdate = tipvinculoRepository.findAll().size();

        // Create the Tipvinculo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTipvinculoMockMvc.perform(put("/api/tipvinculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipvinculo)))
            .andExpect(status().isCreated());

        // Validate the Tipvinculo in the database
        List<Tipvinculo> tipvinculoList = tipvinculoRepository.findAll();
        assertThat(tipvinculoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTipvinculo() throws Exception {
        // Initialize the database
        tipvinculoRepository.saveAndFlush(tipvinculo);
        tipvinculoSearchRepository.save(tipvinculo);
        int databaseSizeBeforeDelete = tipvinculoRepository.findAll().size();

        // Get the tipvinculo
        restTipvinculoMockMvc.perform(delete("/api/tipvinculos/{id}", tipvinculo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tipvinculoExistsInEs = tipvinculoSearchRepository.exists(tipvinculo.getId());
        assertThat(tipvinculoExistsInEs).isFalse();

        // Validate the database is empty
        List<Tipvinculo> tipvinculoList = tipvinculoRepository.findAll();
        assertThat(tipvinculoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTipvinculo() throws Exception {
        // Initialize the database
        tipvinculoRepository.saveAndFlush(tipvinculo);
        tipvinculoSearchRepository.save(tipvinculo);

        // Search the tipvinculo
        restTipvinculoMockMvc.perform(get("/api/_search/tipvinculos?query=id:" + tipvinculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipvinculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDestipvin").value(hasItem(DEFAULT_V_DESTIPVIN.toString())))
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
        TestUtil.equalsVerifier(Tipvinculo.class);
        Tipvinculo tipvinculo1 = new Tipvinculo();
        tipvinculo1.setId(1L);
        Tipvinculo tipvinculo2 = new Tipvinculo();
        tipvinculo2.setId(tipvinculo1.getId());
        assertThat(tipvinculo1).isEqualTo(tipvinculo2);
        tipvinculo2.setId(2L);
        assertThat(tipvinculo1).isNotEqualTo(tipvinculo2);
        tipvinculo1.setId(null);
        assertThat(tipvinculo1).isNotEqualTo(tipvinculo2);
    }
}
