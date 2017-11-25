package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Pasegl;
import pe.gob.trabajo.repository.PaseglRepository;
import pe.gob.trabajo.repository.search.PaseglSearchRepository;
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
 * Test class for the PaseglResource REST controller.
 *
 * @see PaseglResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class PaseglResourceIntTest {

    private static final String DEFAULT_V_OBSPASE = "AAAAAAAAAA";
    private static final String UPDATED_V_OBSPASE = "BBBBBBBBBB";

    private static final String DEFAULT_V_ESTADO = "A";
    private static final String UPDATED_V_ESTADO = "B";

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
    private PaseglRepository paseglRepository;

    @Autowired
    private PaseglSearchRepository paseglSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaseglMockMvc;

    private Pasegl pasegl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaseglResource paseglResource = new PaseglResource(paseglRepository, paseglSearchRepository);
        this.restPaseglMockMvc = MockMvcBuilders.standaloneSetup(paseglResource)
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
    public static Pasegl createEntity(EntityManager em) {
        Pasegl pasegl = new Pasegl()
            .vObspase(DEFAULT_V_OBSPASE)
            .vEstado(DEFAULT_V_ESTADO)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return pasegl;
    }

    @Before
    public void initTest() {
        paseglSearchRepository.deleteAll();
        pasegl = createEntity(em);
    }

    @Test
    @Transactional
    public void createPasegl() throws Exception {
        int databaseSizeBeforeCreate = paseglRepository.findAll().size();

        // Create the Pasegl
        restPaseglMockMvc.perform(post("/api/pasegls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pasegl)))
            .andExpect(status().isCreated());

        // Validate the Pasegl in the database
        List<Pasegl> paseglList = paseglRepository.findAll();
        assertThat(paseglList).hasSize(databaseSizeBeforeCreate + 1);
        Pasegl testPasegl = paseglList.get(paseglList.size() - 1);
        assertThat(testPasegl.getvObspase()).isEqualTo(DEFAULT_V_OBSPASE);
        assertThat(testPasegl.getvEstado()).isEqualTo(DEFAULT_V_ESTADO);
        assertThat(testPasegl.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testPasegl.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testPasegl.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testPasegl.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testPasegl.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testPasegl.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testPasegl.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Pasegl in Elasticsearch
        Pasegl paseglEs = paseglSearchRepository.findOne(testPasegl.getId());
        assertThat(paseglEs).isEqualToComparingFieldByField(testPasegl);
    }

    @Test
    @Transactional
    public void createPaseglWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paseglRepository.findAll().size();

        // Create the Pasegl with an existing ID
        pasegl.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaseglMockMvc.perform(post("/api/pasegls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pasegl)))
            .andExpect(status().isBadRequest());

        // Validate the Pasegl in the database
        List<Pasegl> paseglList = paseglRepository.findAll();
        assertThat(paseglList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = paseglRepository.findAll().size();
        // set the field null
        pasegl.setvEstado(null);

        // Create the Pasegl, which fails.

        restPaseglMockMvc.perform(post("/api/pasegls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pasegl)))
            .andExpect(status().isBadRequest());

        List<Pasegl> paseglList = paseglRepository.findAll();
        assertThat(paseglList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = paseglRepository.findAll().size();
        // set the field null
        pasegl.setnUsuareg(null);

        // Create the Pasegl, which fails.

        restPaseglMockMvc.perform(post("/api/pasegls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pasegl)))
            .andExpect(status().isBadRequest());

        List<Pasegl> paseglList = paseglRepository.findAll();
        assertThat(paseglList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = paseglRepository.findAll().size();
        // set the field null
        pasegl.settFecreg(null);

        // Create the Pasegl, which fails.

        restPaseglMockMvc.perform(post("/api/pasegls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pasegl)))
            .andExpect(status().isBadRequest());

        List<Pasegl> paseglList = paseglRepository.findAll();
        assertThat(paseglList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = paseglRepository.findAll().size();
        // set the field null
        pasegl.setnFlgactivo(null);

        // Create the Pasegl, which fails.

        restPaseglMockMvc.perform(post("/api/pasegls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pasegl)))
            .andExpect(status().isBadRequest());

        List<Pasegl> paseglList = paseglRepository.findAll();
        assertThat(paseglList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = paseglRepository.findAll().size();
        // set the field null
        pasegl.setnSedereg(null);

        // Create the Pasegl, which fails.

        restPaseglMockMvc.perform(post("/api/pasegls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pasegl)))
            .andExpect(status().isBadRequest());

        List<Pasegl> paseglList = paseglRepository.findAll();
        assertThat(paseglList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPasegls() throws Exception {
        // Initialize the database
        paseglRepository.saveAndFlush(pasegl);

        // Get all the paseglList
        restPaseglMockMvc.perform(get("/api/pasegls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pasegl.getId().intValue())))
            .andExpect(jsonPath("$.[*].vObspase").value(hasItem(DEFAULT_V_OBSPASE.toString())))
            .andExpect(jsonPath("$.[*].vEstado").value(hasItem(DEFAULT_V_ESTADO.toString())))
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
    public void getPasegl() throws Exception {
        // Initialize the database
        paseglRepository.saveAndFlush(pasegl);

        // Get the pasegl
        restPaseglMockMvc.perform(get("/api/pasegls/{id}", pasegl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pasegl.getId().intValue()))
            .andExpect(jsonPath("$.vObspase").value(DEFAULT_V_OBSPASE.toString()))
            .andExpect(jsonPath("$.vEstado").value(DEFAULT_V_ESTADO.toString()))
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
    public void getNonExistingPasegl() throws Exception {
        // Get the pasegl
        restPaseglMockMvc.perform(get("/api/pasegls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePasegl() throws Exception {
        // Initialize the database
        paseglRepository.saveAndFlush(pasegl);
        paseglSearchRepository.save(pasegl);
        int databaseSizeBeforeUpdate = paseglRepository.findAll().size();

        // Update the pasegl
        Pasegl updatedPasegl = paseglRepository.findOne(pasegl.getId());
        updatedPasegl
            .vObspase(UPDATED_V_OBSPASE)
            .vEstado(UPDATED_V_ESTADO)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restPaseglMockMvc.perform(put("/api/pasegls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPasegl)))
            .andExpect(status().isOk());

        // Validate the Pasegl in the database
        List<Pasegl> paseglList = paseglRepository.findAll();
        assertThat(paseglList).hasSize(databaseSizeBeforeUpdate);
        Pasegl testPasegl = paseglList.get(paseglList.size() - 1);
        assertThat(testPasegl.getvObspase()).isEqualTo(UPDATED_V_OBSPASE);
        assertThat(testPasegl.getvEstado()).isEqualTo(UPDATED_V_ESTADO);
        assertThat(testPasegl.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testPasegl.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testPasegl.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testPasegl.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testPasegl.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testPasegl.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testPasegl.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Pasegl in Elasticsearch
        Pasegl paseglEs = paseglSearchRepository.findOne(testPasegl.getId());
        assertThat(paseglEs).isEqualToComparingFieldByField(testPasegl);
    }

    @Test
    @Transactional
    public void updateNonExistingPasegl() throws Exception {
        int databaseSizeBeforeUpdate = paseglRepository.findAll().size();

        // Create the Pasegl

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPaseglMockMvc.perform(put("/api/pasegls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pasegl)))
            .andExpect(status().isCreated());

        // Validate the Pasegl in the database
        List<Pasegl> paseglList = paseglRepository.findAll();
        assertThat(paseglList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePasegl() throws Exception {
        // Initialize the database
        paseglRepository.saveAndFlush(pasegl);
        paseglSearchRepository.save(pasegl);
        int databaseSizeBeforeDelete = paseglRepository.findAll().size();

        // Get the pasegl
        restPaseglMockMvc.perform(delete("/api/pasegls/{id}", pasegl.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean paseglExistsInEs = paseglSearchRepository.exists(pasegl.getId());
        assertThat(paseglExistsInEs).isFalse();

        // Validate the database is empty
        List<Pasegl> paseglList = paseglRepository.findAll();
        assertThat(paseglList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPasegl() throws Exception {
        // Initialize the database
        paseglRepository.saveAndFlush(pasegl);
        paseglSearchRepository.save(pasegl);

        // Search the pasegl
        restPaseglMockMvc.perform(get("/api/_search/pasegls?query=id:" + pasegl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pasegl.getId().intValue())))
            .andExpect(jsonPath("$.[*].vObspase").value(hasItem(DEFAULT_V_OBSPASE.toString())))
            .andExpect(jsonPath("$.[*].vEstado").value(hasItem(DEFAULT_V_ESTADO.toString())))
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
        TestUtil.equalsVerifier(Pasegl.class);
        Pasegl pasegl1 = new Pasegl();
        pasegl1.setId(1L);
        Pasegl pasegl2 = new Pasegl();
        pasegl2.setId(pasegl1.getId());
        assertThat(pasegl1).isEqualTo(pasegl2);
        pasegl2.setId(2L);
        assertThat(pasegl1).isNotEqualTo(pasegl2);
        pasegl1.setId(null);
        assertThat(pasegl1).isNotEqualTo(pasegl2);
    }
}
