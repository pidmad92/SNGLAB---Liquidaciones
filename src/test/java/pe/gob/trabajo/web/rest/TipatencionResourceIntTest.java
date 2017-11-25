package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Tipatencion;
import pe.gob.trabajo.repository.TipatencionRepository;
import pe.gob.trabajo.repository.search.TipatencionSearchRepository;
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
 * Test class for the TipatencionResource REST controller.
 *
 * @see TipatencionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class TipatencionResourceIntTest {

    private static final String DEFAULT_V_DESTIPATE = "AAAAAAAAAA";
    private static final String UPDATED_V_DESTIPATE = "BBBBBBBBBB";

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
    private TipatencionRepository tipatencionRepository;

    @Autowired
    private TipatencionSearchRepository tipatencionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTipatencionMockMvc;

    private Tipatencion tipatencion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipatencionResource tipatencionResource = new TipatencionResource(tipatencionRepository, tipatencionSearchRepository);
        this.restTipatencionMockMvc = MockMvcBuilders.standaloneSetup(tipatencionResource)
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
    public static Tipatencion createEntity(EntityManager em) {
        Tipatencion tipatencion = new Tipatencion()
            .vDestipate(DEFAULT_V_DESTIPATE)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return tipatencion;
    }

    @Before
    public void initTest() {
        tipatencionSearchRepository.deleteAll();
        tipatencion = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipatencion() throws Exception {
        int databaseSizeBeforeCreate = tipatencionRepository.findAll().size();

        // Create the Tipatencion
        restTipatencionMockMvc.perform(post("/api/tipatencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipatencion)))
            .andExpect(status().isCreated());

        // Validate the Tipatencion in the database
        List<Tipatencion> tipatencionList = tipatencionRepository.findAll();
        assertThat(tipatencionList).hasSize(databaseSizeBeforeCreate + 1);
        Tipatencion testTipatencion = tipatencionList.get(tipatencionList.size() - 1);
        assertThat(testTipatencion.getvDestipate()).isEqualTo(DEFAULT_V_DESTIPATE);
        assertThat(testTipatencion.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testTipatencion.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testTipatencion.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testTipatencion.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testTipatencion.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testTipatencion.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testTipatencion.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Tipatencion in Elasticsearch
        Tipatencion tipatencionEs = tipatencionSearchRepository.findOne(testTipatencion.getId());
        assertThat(tipatencionEs).isEqualToComparingFieldByField(testTipatencion);
    }

    @Test
    @Transactional
    public void createTipatencionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipatencionRepository.findAll().size();

        // Create the Tipatencion with an existing ID
        tipatencion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipatencionMockMvc.perform(post("/api/tipatencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipatencion)))
            .andExpect(status().isBadRequest());

        // Validate the Tipatencion in the database
        List<Tipatencion> tipatencionList = tipatencionRepository.findAll();
        assertThat(tipatencionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDestipateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipatencionRepository.findAll().size();
        // set the field null
        tipatencion.setvDestipate(null);

        // Create the Tipatencion, which fails.

        restTipatencionMockMvc.perform(post("/api/tipatencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipatencion)))
            .andExpect(status().isBadRequest());

        List<Tipatencion> tipatencionList = tipatencionRepository.findAll();
        assertThat(tipatencionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipatencionRepository.findAll().size();
        // set the field null
        tipatencion.setnUsuareg(null);

        // Create the Tipatencion, which fails.

        restTipatencionMockMvc.perform(post("/api/tipatencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipatencion)))
            .andExpect(status().isBadRequest());

        List<Tipatencion> tipatencionList = tipatencionRepository.findAll();
        assertThat(tipatencionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipatencionRepository.findAll().size();
        // set the field null
        tipatencion.settFecreg(null);

        // Create the Tipatencion, which fails.

        restTipatencionMockMvc.perform(post("/api/tipatencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipatencion)))
            .andExpect(status().isBadRequest());

        List<Tipatencion> tipatencionList = tipatencionRepository.findAll();
        assertThat(tipatencionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipatencionRepository.findAll().size();
        // set the field null
        tipatencion.setnFlgactivo(null);

        // Create the Tipatencion, which fails.

        restTipatencionMockMvc.perform(post("/api/tipatencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipatencion)))
            .andExpect(status().isBadRequest());

        List<Tipatencion> tipatencionList = tipatencionRepository.findAll();
        assertThat(tipatencionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipatencionRepository.findAll().size();
        // set the field null
        tipatencion.setnSedereg(null);

        // Create the Tipatencion, which fails.

        restTipatencionMockMvc.perform(post("/api/tipatencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipatencion)))
            .andExpect(status().isBadRequest());

        List<Tipatencion> tipatencionList = tipatencionRepository.findAll();
        assertThat(tipatencionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipatencions() throws Exception {
        // Initialize the database
        tipatencionRepository.saveAndFlush(tipatencion);

        // Get all the tipatencionList
        restTipatencionMockMvc.perform(get("/api/tipatencions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipatencion.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDestipate").value(hasItem(DEFAULT_V_DESTIPATE.toString())))
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
    public void getTipatencion() throws Exception {
        // Initialize the database
        tipatencionRepository.saveAndFlush(tipatencion);

        // Get the tipatencion
        restTipatencionMockMvc.perform(get("/api/tipatencions/{id}", tipatencion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipatencion.getId().intValue()))
            .andExpect(jsonPath("$.vDestipate").value(DEFAULT_V_DESTIPATE.toString()))
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
    public void getNonExistingTipatencion() throws Exception {
        // Get the tipatencion
        restTipatencionMockMvc.perform(get("/api/tipatencions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipatencion() throws Exception {
        // Initialize the database
        tipatencionRepository.saveAndFlush(tipatencion);
        tipatencionSearchRepository.save(tipatencion);
        int databaseSizeBeforeUpdate = tipatencionRepository.findAll().size();

        // Update the tipatencion
        Tipatencion updatedTipatencion = tipatencionRepository.findOne(tipatencion.getId());
        updatedTipatencion
            .vDestipate(UPDATED_V_DESTIPATE)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restTipatencionMockMvc.perform(put("/api/tipatencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipatencion)))
            .andExpect(status().isOk());

        // Validate the Tipatencion in the database
        List<Tipatencion> tipatencionList = tipatencionRepository.findAll();
        assertThat(tipatencionList).hasSize(databaseSizeBeforeUpdate);
        Tipatencion testTipatencion = tipatencionList.get(tipatencionList.size() - 1);
        assertThat(testTipatencion.getvDestipate()).isEqualTo(UPDATED_V_DESTIPATE);
        assertThat(testTipatencion.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testTipatencion.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testTipatencion.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testTipatencion.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testTipatencion.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testTipatencion.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testTipatencion.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Tipatencion in Elasticsearch
        Tipatencion tipatencionEs = tipatencionSearchRepository.findOne(testTipatencion.getId());
        assertThat(tipatencionEs).isEqualToComparingFieldByField(testTipatencion);
    }

    @Test
    @Transactional
    public void updateNonExistingTipatencion() throws Exception {
        int databaseSizeBeforeUpdate = tipatencionRepository.findAll().size();

        // Create the Tipatencion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTipatencionMockMvc.perform(put("/api/tipatencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipatencion)))
            .andExpect(status().isCreated());

        // Validate the Tipatencion in the database
        List<Tipatencion> tipatencionList = tipatencionRepository.findAll();
        assertThat(tipatencionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTipatencion() throws Exception {
        // Initialize the database
        tipatencionRepository.saveAndFlush(tipatencion);
        tipatencionSearchRepository.save(tipatencion);
        int databaseSizeBeforeDelete = tipatencionRepository.findAll().size();

        // Get the tipatencion
        restTipatencionMockMvc.perform(delete("/api/tipatencions/{id}", tipatencion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tipatencionExistsInEs = tipatencionSearchRepository.exists(tipatencion.getId());
        assertThat(tipatencionExistsInEs).isFalse();

        // Validate the database is empty
        List<Tipatencion> tipatencionList = tipatencionRepository.findAll();
        assertThat(tipatencionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTipatencion() throws Exception {
        // Initialize the database
        tipatencionRepository.saveAndFlush(tipatencion);
        tipatencionSearchRepository.save(tipatencion);

        // Search the tipatencion
        restTipatencionMockMvc.perform(get("/api/_search/tipatencions?query=id:" + tipatencion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipatencion.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDestipate").value(hasItem(DEFAULT_V_DESTIPATE.toString())))
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
        TestUtil.equalsVerifier(Tipatencion.class);
        Tipatencion tipatencion1 = new Tipatencion();
        tipatencion1.setId(1L);
        Tipatencion tipatencion2 = new Tipatencion();
        tipatencion2.setId(tipatencion1.getId());
        assertThat(tipatencion1).isEqualTo(tipatencion2);
        tipatencion2.setId(2L);
        assertThat(tipatencion1).isNotEqualTo(tipatencion2);
        tipatencion1.setId(null);
        assertThat(tipatencion1).isNotEqualTo(tipatencion2);
    }
}
