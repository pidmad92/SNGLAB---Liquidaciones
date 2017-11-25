package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Direcalter;
import pe.gob.trabajo.repository.DirecalterRepository;
import pe.gob.trabajo.repository.search.DirecalterSearchRepository;
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
 * Test class for the DirecalterResource REST controller.
 *
 * @see DirecalterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class DirecalterResourceIntTest {

    private static final String DEFAULT_V_RAZSOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_V_RAZSOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_V_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_V_DIRECCION = "BBBBBBBBBB";

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
    private DirecalterRepository direcalterRepository;

    @Autowired
    private DirecalterSearchRepository direcalterSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDirecalterMockMvc;

    private Direcalter direcalter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DirecalterResource direcalterResource = new DirecalterResource(direcalterRepository, direcalterSearchRepository);
        this.restDirecalterMockMvc = MockMvcBuilders.standaloneSetup(direcalterResource)
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
    public static Direcalter createEntity(EntityManager em) {
        Direcalter direcalter = new Direcalter()
            .vRazsocial(DEFAULT_V_RAZSOCIAL)
            .vDireccion(DEFAULT_V_DIRECCION)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return direcalter;
    }

    @Before
    public void initTest() {
        direcalterSearchRepository.deleteAll();
        direcalter = createEntity(em);
    }

    @Test
    @Transactional
    public void createDirecalter() throws Exception {
        int databaseSizeBeforeCreate = direcalterRepository.findAll().size();

        // Create the Direcalter
        restDirecalterMockMvc.perform(post("/api/direcalters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direcalter)))
            .andExpect(status().isCreated());

        // Validate the Direcalter in the database
        List<Direcalter> direcalterList = direcalterRepository.findAll();
        assertThat(direcalterList).hasSize(databaseSizeBeforeCreate + 1);
        Direcalter testDirecalter = direcalterList.get(direcalterList.size() - 1);
        assertThat(testDirecalter.getvRazsocial()).isEqualTo(DEFAULT_V_RAZSOCIAL);
        assertThat(testDirecalter.getvDireccion()).isEqualTo(DEFAULT_V_DIRECCION);
        assertThat(testDirecalter.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testDirecalter.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testDirecalter.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testDirecalter.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testDirecalter.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testDirecalter.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testDirecalter.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Direcalter in Elasticsearch
        Direcalter direcalterEs = direcalterSearchRepository.findOne(testDirecalter.getId());
        assertThat(direcalterEs).isEqualToComparingFieldByField(testDirecalter);
    }

    @Test
    @Transactional
    public void createDirecalterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = direcalterRepository.findAll().size();

        // Create the Direcalter with an existing ID
        direcalter.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirecalterMockMvc.perform(post("/api/direcalters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direcalter)))
            .andExpect(status().isBadRequest());

        // Validate the Direcalter in the database
        List<Direcalter> direcalterList = direcalterRepository.findAll();
        assertThat(direcalterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvRazsocialIsRequired() throws Exception {
        int databaseSizeBeforeTest = direcalterRepository.findAll().size();
        // set the field null
        direcalter.setvRazsocial(null);

        // Create the Direcalter, which fails.

        restDirecalterMockMvc.perform(post("/api/direcalters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direcalter)))
            .andExpect(status().isBadRequest());

        List<Direcalter> direcalterList = direcalterRepository.findAll();
        assertThat(direcalterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkvDireccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = direcalterRepository.findAll().size();
        // set the field null
        direcalter.setvDireccion(null);

        // Create the Direcalter, which fails.

        restDirecalterMockMvc.perform(post("/api/direcalters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direcalter)))
            .andExpect(status().isBadRequest());

        List<Direcalter> direcalterList = direcalterRepository.findAll();
        assertThat(direcalterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = direcalterRepository.findAll().size();
        // set the field null
        direcalter.setnUsuareg(null);

        // Create the Direcalter, which fails.

        restDirecalterMockMvc.perform(post("/api/direcalters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direcalter)))
            .andExpect(status().isBadRequest());

        List<Direcalter> direcalterList = direcalterRepository.findAll();
        assertThat(direcalterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = direcalterRepository.findAll().size();
        // set the field null
        direcalter.settFecreg(null);

        // Create the Direcalter, which fails.

        restDirecalterMockMvc.perform(post("/api/direcalters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direcalter)))
            .andExpect(status().isBadRequest());

        List<Direcalter> direcalterList = direcalterRepository.findAll();
        assertThat(direcalterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = direcalterRepository.findAll().size();
        // set the field null
        direcalter.setnFlgactivo(null);

        // Create the Direcalter, which fails.

        restDirecalterMockMvc.perform(post("/api/direcalters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direcalter)))
            .andExpect(status().isBadRequest());

        List<Direcalter> direcalterList = direcalterRepository.findAll();
        assertThat(direcalterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = direcalterRepository.findAll().size();
        // set the field null
        direcalter.setnSedereg(null);

        // Create the Direcalter, which fails.

        restDirecalterMockMvc.perform(post("/api/direcalters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direcalter)))
            .andExpect(status().isBadRequest());

        List<Direcalter> direcalterList = direcalterRepository.findAll();
        assertThat(direcalterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDirecalters() throws Exception {
        // Initialize the database
        direcalterRepository.saveAndFlush(direcalter);

        // Get all the direcalterList
        restDirecalterMockMvc.perform(get("/api/direcalters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direcalter.getId().intValue())))
            .andExpect(jsonPath("$.[*].vRazsocial").value(hasItem(DEFAULT_V_RAZSOCIAL.toString())))
            .andExpect(jsonPath("$.[*].vDireccion").value(hasItem(DEFAULT_V_DIRECCION.toString())))
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
    public void getDirecalter() throws Exception {
        // Initialize the database
        direcalterRepository.saveAndFlush(direcalter);

        // Get the direcalter
        restDirecalterMockMvc.perform(get("/api/direcalters/{id}", direcalter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(direcalter.getId().intValue()))
            .andExpect(jsonPath("$.vRazsocial").value(DEFAULT_V_RAZSOCIAL.toString()))
            .andExpect(jsonPath("$.vDireccion").value(DEFAULT_V_DIRECCION.toString()))
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
    public void getNonExistingDirecalter() throws Exception {
        // Get the direcalter
        restDirecalterMockMvc.perform(get("/api/direcalters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDirecalter() throws Exception {
        // Initialize the database
        direcalterRepository.saveAndFlush(direcalter);
        direcalterSearchRepository.save(direcalter);
        int databaseSizeBeforeUpdate = direcalterRepository.findAll().size();

        // Update the direcalter
        Direcalter updatedDirecalter = direcalterRepository.findOne(direcalter.getId());
        updatedDirecalter
            .vRazsocial(UPDATED_V_RAZSOCIAL)
            .vDireccion(UPDATED_V_DIRECCION)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restDirecalterMockMvc.perform(put("/api/direcalters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDirecalter)))
            .andExpect(status().isOk());

        // Validate the Direcalter in the database
        List<Direcalter> direcalterList = direcalterRepository.findAll();
        assertThat(direcalterList).hasSize(databaseSizeBeforeUpdate);
        Direcalter testDirecalter = direcalterList.get(direcalterList.size() - 1);
        assertThat(testDirecalter.getvRazsocial()).isEqualTo(UPDATED_V_RAZSOCIAL);
        assertThat(testDirecalter.getvDireccion()).isEqualTo(UPDATED_V_DIRECCION);
        assertThat(testDirecalter.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testDirecalter.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testDirecalter.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testDirecalter.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testDirecalter.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testDirecalter.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testDirecalter.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Direcalter in Elasticsearch
        Direcalter direcalterEs = direcalterSearchRepository.findOne(testDirecalter.getId());
        assertThat(direcalterEs).isEqualToComparingFieldByField(testDirecalter);
    }

    @Test
    @Transactional
    public void updateNonExistingDirecalter() throws Exception {
        int databaseSizeBeforeUpdate = direcalterRepository.findAll().size();

        // Create the Direcalter

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDirecalterMockMvc.perform(put("/api/direcalters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direcalter)))
            .andExpect(status().isCreated());

        // Validate the Direcalter in the database
        List<Direcalter> direcalterList = direcalterRepository.findAll();
        assertThat(direcalterList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDirecalter() throws Exception {
        // Initialize the database
        direcalterRepository.saveAndFlush(direcalter);
        direcalterSearchRepository.save(direcalter);
        int databaseSizeBeforeDelete = direcalterRepository.findAll().size();

        // Get the direcalter
        restDirecalterMockMvc.perform(delete("/api/direcalters/{id}", direcalter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean direcalterExistsInEs = direcalterSearchRepository.exists(direcalter.getId());
        assertThat(direcalterExistsInEs).isFalse();

        // Validate the database is empty
        List<Direcalter> direcalterList = direcalterRepository.findAll();
        assertThat(direcalterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDirecalter() throws Exception {
        // Initialize the database
        direcalterRepository.saveAndFlush(direcalter);
        direcalterSearchRepository.save(direcalter);

        // Search the direcalter
        restDirecalterMockMvc.perform(get("/api/_search/direcalters?query=id:" + direcalter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direcalter.getId().intValue())))
            .andExpect(jsonPath("$.[*].vRazsocial").value(hasItem(DEFAULT_V_RAZSOCIAL.toString())))
            .andExpect(jsonPath("$.[*].vDireccion").value(hasItem(DEFAULT_V_DIRECCION.toString())))
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
        TestUtil.equalsVerifier(Direcalter.class);
        Direcalter direcalter1 = new Direcalter();
        direcalter1.setId(1L);
        Direcalter direcalter2 = new Direcalter();
        direcalter2.setId(direcalter1.getId());
        assertThat(direcalter1).isEqualTo(direcalter2);
        direcalter2.setId(2L);
        assertThat(direcalter1).isNotEqualTo(direcalter2);
        direcalter1.setId(null);
        assertThat(direcalter1).isNotEqualTo(direcalter2);
    }
}
