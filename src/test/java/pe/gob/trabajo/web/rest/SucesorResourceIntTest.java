package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Sucesor;
import pe.gob.trabajo.repository.SucesorRepository;
import pe.gob.trabajo.repository.search.SucesorSearchRepository;
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
 * Test class for the SucesorResource REST controller.
 *
 * @see SucesorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class SucesorResourceIntTest {

    private static final String DEFAULT_V_ESTADO = "A";
    private static final String UPDATED_V_ESTADO = "B";

    private static final String DEFAULT_V_CODPARTID = "AAAAAAAAAA";
    private static final String UPDATED_V_CODPARTID = "BBBBBBBBBB";

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
    private SucesorRepository sucesorRepository;

    @Autowired
    private SucesorSearchRepository sucesorSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSucesorMockMvc;

    private Sucesor sucesor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SucesorResource sucesorResource = new SucesorResource(sucesorRepository, sucesorSearchRepository);
        this.restSucesorMockMvc = MockMvcBuilders.standaloneSetup(sucesorResource)
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
    public static Sucesor createEntity(EntityManager em) {
        Sucesor sucesor = new Sucesor()
            .vEstado(DEFAULT_V_ESTADO)
            .vCodpartid(DEFAULT_V_CODPARTID)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return sucesor;
    }

    @Before
    public void initTest() {
        sucesorSearchRepository.deleteAll();
        sucesor = createEntity(em);
    }

    @Test
    @Transactional
    public void createSucesor() throws Exception {
        int databaseSizeBeforeCreate = sucesorRepository.findAll().size();

        // Create the Sucesor
        restSucesorMockMvc.perform(post("/api/sucesors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sucesor)))
            .andExpect(status().isCreated());

        // Validate the Sucesor in the database
        List<Sucesor> sucesorList = sucesorRepository.findAll();
        assertThat(sucesorList).hasSize(databaseSizeBeforeCreate + 1);
        Sucesor testSucesor = sucesorList.get(sucesorList.size() - 1);
        assertThat(testSucesor.getvEstado()).isEqualTo(DEFAULT_V_ESTADO);
        assertThat(testSucesor.getvCodpartid()).isEqualTo(DEFAULT_V_CODPARTID);
        assertThat(testSucesor.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testSucesor.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testSucesor.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testSucesor.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testSucesor.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testSucesor.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testSucesor.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Sucesor in Elasticsearch
        Sucesor sucesorEs = sucesorSearchRepository.findOne(testSucesor.getId());
        assertThat(sucesorEs).isEqualToComparingFieldByField(testSucesor);
    }

    @Test
    @Transactional
    public void createSucesorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sucesorRepository.findAll().size();

        // Create the Sucesor with an existing ID
        sucesor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSucesorMockMvc.perform(post("/api/sucesors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sucesor)))
            .andExpect(status().isBadRequest());

        // Validate the Sucesor in the database
        List<Sucesor> sucesorList = sucesorRepository.findAll();
        assertThat(sucesorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = sucesorRepository.findAll().size();
        // set the field null
        sucesor.setvEstado(null);

        // Create the Sucesor, which fails.

        restSucesorMockMvc.perform(post("/api/sucesors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sucesor)))
            .andExpect(status().isBadRequest());

        List<Sucesor> sucesorList = sucesorRepository.findAll();
        assertThat(sucesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkvCodpartidIsRequired() throws Exception {
        int databaseSizeBeforeTest = sucesorRepository.findAll().size();
        // set the field null
        sucesor.setvCodpartid(null);

        // Create the Sucesor, which fails.

        restSucesorMockMvc.perform(post("/api/sucesors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sucesor)))
            .andExpect(status().isBadRequest());

        List<Sucesor> sucesorList = sucesorRepository.findAll();
        assertThat(sucesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = sucesorRepository.findAll().size();
        // set the field null
        sucesor.setnUsuareg(null);

        // Create the Sucesor, which fails.

        restSucesorMockMvc.perform(post("/api/sucesors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sucesor)))
            .andExpect(status().isBadRequest());

        List<Sucesor> sucesorList = sucesorRepository.findAll();
        assertThat(sucesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = sucesorRepository.findAll().size();
        // set the field null
        sucesor.settFecreg(null);

        // Create the Sucesor, which fails.

        restSucesorMockMvc.perform(post("/api/sucesors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sucesor)))
            .andExpect(status().isBadRequest());

        List<Sucesor> sucesorList = sucesorRepository.findAll();
        assertThat(sucesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = sucesorRepository.findAll().size();
        // set the field null
        sucesor.setnFlgactivo(null);

        // Create the Sucesor, which fails.

        restSucesorMockMvc.perform(post("/api/sucesors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sucesor)))
            .andExpect(status().isBadRequest());

        List<Sucesor> sucesorList = sucesorRepository.findAll();
        assertThat(sucesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = sucesorRepository.findAll().size();
        // set the field null
        sucesor.setnSedereg(null);

        // Create the Sucesor, which fails.

        restSucesorMockMvc.perform(post("/api/sucesors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sucesor)))
            .andExpect(status().isBadRequest());

        List<Sucesor> sucesorList = sucesorRepository.findAll();
        assertThat(sucesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSucesors() throws Exception {
        // Initialize the database
        sucesorRepository.saveAndFlush(sucesor);

        // Get all the sucesorList
        restSucesorMockMvc.perform(get("/api/sucesors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sucesor.getId().intValue())))
            .andExpect(jsonPath("$.[*].vEstado").value(hasItem(DEFAULT_V_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].vCodpartid").value(hasItem(DEFAULT_V_CODPARTID.toString())))
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
    public void getSucesor() throws Exception {
        // Initialize the database
        sucesorRepository.saveAndFlush(sucesor);

        // Get the sucesor
        restSucesorMockMvc.perform(get("/api/sucesors/{id}", sucesor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sucesor.getId().intValue()))
            .andExpect(jsonPath("$.vEstado").value(DEFAULT_V_ESTADO.toString()))
            .andExpect(jsonPath("$.vCodpartid").value(DEFAULT_V_CODPARTID.toString()))
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
    public void getNonExistingSucesor() throws Exception {
        // Get the sucesor
        restSucesorMockMvc.perform(get("/api/sucesors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSucesor() throws Exception {
        // Initialize the database
        sucesorRepository.saveAndFlush(sucesor);
        sucesorSearchRepository.save(sucesor);
        int databaseSizeBeforeUpdate = sucesorRepository.findAll().size();

        // Update the sucesor
        Sucesor updatedSucesor = sucesorRepository.findOne(sucesor.getId());
        updatedSucesor
            .vEstado(UPDATED_V_ESTADO)
            .vCodpartid(UPDATED_V_CODPARTID)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restSucesorMockMvc.perform(put("/api/sucesors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSucesor)))
            .andExpect(status().isOk());

        // Validate the Sucesor in the database
        List<Sucesor> sucesorList = sucesorRepository.findAll();
        assertThat(sucesorList).hasSize(databaseSizeBeforeUpdate);
        Sucesor testSucesor = sucesorList.get(sucesorList.size() - 1);
        assertThat(testSucesor.getvEstado()).isEqualTo(UPDATED_V_ESTADO);
        assertThat(testSucesor.getvCodpartid()).isEqualTo(UPDATED_V_CODPARTID);
        assertThat(testSucesor.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testSucesor.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testSucesor.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testSucesor.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testSucesor.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testSucesor.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testSucesor.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Sucesor in Elasticsearch
        Sucesor sucesorEs = sucesorSearchRepository.findOne(testSucesor.getId());
        assertThat(sucesorEs).isEqualToComparingFieldByField(testSucesor);
    }

    @Test
    @Transactional
    public void updateNonExistingSucesor() throws Exception {
        int databaseSizeBeforeUpdate = sucesorRepository.findAll().size();

        // Create the Sucesor

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSucesorMockMvc.perform(put("/api/sucesors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sucesor)))
            .andExpect(status().isCreated());

        // Validate the Sucesor in the database
        List<Sucesor> sucesorList = sucesorRepository.findAll();
        assertThat(sucesorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSucesor() throws Exception {
        // Initialize the database
        sucesorRepository.saveAndFlush(sucesor);
        sucesorSearchRepository.save(sucesor);
        int databaseSizeBeforeDelete = sucesorRepository.findAll().size();

        // Get the sucesor
        restSucesorMockMvc.perform(delete("/api/sucesors/{id}", sucesor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean sucesorExistsInEs = sucesorSearchRepository.exists(sucesor.getId());
        assertThat(sucesorExistsInEs).isFalse();

        // Validate the database is empty
        List<Sucesor> sucesorList = sucesorRepository.findAll();
        assertThat(sucesorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSucesor() throws Exception {
        // Initialize the database
        sucesorRepository.saveAndFlush(sucesor);
        sucesorSearchRepository.save(sucesor);

        // Search the sucesor
        restSucesorMockMvc.perform(get("/api/_search/sucesors?query=id:" + sucesor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sucesor.getId().intValue())))
            .andExpect(jsonPath("$.[*].vEstado").value(hasItem(DEFAULT_V_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].vCodpartid").value(hasItem(DEFAULT_V_CODPARTID.toString())))
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
        TestUtil.equalsVerifier(Sucesor.class);
        Sucesor sucesor1 = new Sucesor();
        sucesor1.setId(1L);
        Sucesor sucesor2 = new Sucesor();
        sucesor2.setId(sucesor1.getId());
        assertThat(sucesor1).isEqualTo(sucesor2);
        sucesor2.setId(2L);
        assertThat(sucesor1).isNotEqualTo(sucesor2);
        sucesor1.setId(null);
        assertThat(sucesor1).isNotEqualTo(sucesor2);
    }
}
