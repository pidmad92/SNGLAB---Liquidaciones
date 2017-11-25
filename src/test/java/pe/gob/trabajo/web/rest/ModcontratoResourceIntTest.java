package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Modcontrato;
import pe.gob.trabajo.repository.ModcontratoRepository;
import pe.gob.trabajo.repository.search.ModcontratoSearchRepository;
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
 * Test class for the ModcontratoResource REST controller.
 *
 * @see ModcontratoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class ModcontratoResourceIntTest {

    private static final String DEFAULT_V_DESMODCON = "AAAAAAAAAA";
    private static final String UPDATED_V_DESMODCON = "BBBBBBBBBB";

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
    private ModcontratoRepository modcontratoRepository;

    @Autowired
    private ModcontratoSearchRepository modcontratoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restModcontratoMockMvc;

    private Modcontrato modcontrato;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ModcontratoResource modcontratoResource = new ModcontratoResource(modcontratoRepository, modcontratoSearchRepository);
        this.restModcontratoMockMvc = MockMvcBuilders.standaloneSetup(modcontratoResource)
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
    public static Modcontrato createEntity(EntityManager em) {
        Modcontrato modcontrato = new Modcontrato()
            .vDesmodcon(DEFAULT_V_DESMODCON)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return modcontrato;
    }

    @Before
    public void initTest() {
        modcontratoSearchRepository.deleteAll();
        modcontrato = createEntity(em);
    }

    @Test
    @Transactional
    public void createModcontrato() throws Exception {
        int databaseSizeBeforeCreate = modcontratoRepository.findAll().size();

        // Create the Modcontrato
        restModcontratoMockMvc.perform(post("/api/modcontratoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modcontrato)))
            .andExpect(status().isCreated());

        // Validate the Modcontrato in the database
        List<Modcontrato> modcontratoList = modcontratoRepository.findAll();
        assertThat(modcontratoList).hasSize(databaseSizeBeforeCreate + 1);
        Modcontrato testModcontrato = modcontratoList.get(modcontratoList.size() - 1);
        assertThat(testModcontrato.getvDesmodcon()).isEqualTo(DEFAULT_V_DESMODCON);
        assertThat(testModcontrato.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testModcontrato.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testModcontrato.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testModcontrato.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testModcontrato.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testModcontrato.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testModcontrato.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Modcontrato in Elasticsearch
        Modcontrato modcontratoEs = modcontratoSearchRepository.findOne(testModcontrato.getId());
        assertThat(modcontratoEs).isEqualToComparingFieldByField(testModcontrato);
    }

    @Test
    @Transactional
    public void createModcontratoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modcontratoRepository.findAll().size();

        // Create the Modcontrato with an existing ID
        modcontrato.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModcontratoMockMvc.perform(post("/api/modcontratoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modcontrato)))
            .andExpect(status().isBadRequest());

        // Validate the Modcontrato in the database
        List<Modcontrato> modcontratoList = modcontratoRepository.findAll();
        assertThat(modcontratoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDesmodconIsRequired() throws Exception {
        int databaseSizeBeforeTest = modcontratoRepository.findAll().size();
        // set the field null
        modcontrato.setvDesmodcon(null);

        // Create the Modcontrato, which fails.

        restModcontratoMockMvc.perform(post("/api/modcontratoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modcontrato)))
            .andExpect(status().isBadRequest());

        List<Modcontrato> modcontratoList = modcontratoRepository.findAll();
        assertThat(modcontratoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = modcontratoRepository.findAll().size();
        // set the field null
        modcontrato.setnUsuareg(null);

        // Create the Modcontrato, which fails.

        restModcontratoMockMvc.perform(post("/api/modcontratoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modcontrato)))
            .andExpect(status().isBadRequest());

        List<Modcontrato> modcontratoList = modcontratoRepository.findAll();
        assertThat(modcontratoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = modcontratoRepository.findAll().size();
        // set the field null
        modcontrato.settFecreg(null);

        // Create the Modcontrato, which fails.

        restModcontratoMockMvc.perform(post("/api/modcontratoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modcontrato)))
            .andExpect(status().isBadRequest());

        List<Modcontrato> modcontratoList = modcontratoRepository.findAll();
        assertThat(modcontratoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = modcontratoRepository.findAll().size();
        // set the field null
        modcontrato.setnFlgactivo(null);

        // Create the Modcontrato, which fails.

        restModcontratoMockMvc.perform(post("/api/modcontratoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modcontrato)))
            .andExpect(status().isBadRequest());

        List<Modcontrato> modcontratoList = modcontratoRepository.findAll();
        assertThat(modcontratoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = modcontratoRepository.findAll().size();
        // set the field null
        modcontrato.setnSedereg(null);

        // Create the Modcontrato, which fails.

        restModcontratoMockMvc.perform(post("/api/modcontratoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modcontrato)))
            .andExpect(status().isBadRequest());

        List<Modcontrato> modcontratoList = modcontratoRepository.findAll();
        assertThat(modcontratoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllModcontratoes() throws Exception {
        // Initialize the database
        modcontratoRepository.saveAndFlush(modcontrato);

        // Get all the modcontratoList
        restModcontratoMockMvc.perform(get("/api/modcontratoes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modcontrato.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesmodcon").value(hasItem(DEFAULT_V_DESMODCON.toString())))
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
    public void getModcontrato() throws Exception {
        // Initialize the database
        modcontratoRepository.saveAndFlush(modcontrato);

        // Get the modcontrato
        restModcontratoMockMvc.perform(get("/api/modcontratoes/{id}", modcontrato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(modcontrato.getId().intValue()))
            .andExpect(jsonPath("$.vDesmodcon").value(DEFAULT_V_DESMODCON.toString()))
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
    public void getNonExistingModcontrato() throws Exception {
        // Get the modcontrato
        restModcontratoMockMvc.perform(get("/api/modcontratoes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModcontrato() throws Exception {
        // Initialize the database
        modcontratoRepository.saveAndFlush(modcontrato);
        modcontratoSearchRepository.save(modcontrato);
        int databaseSizeBeforeUpdate = modcontratoRepository.findAll().size();

        // Update the modcontrato
        Modcontrato updatedModcontrato = modcontratoRepository.findOne(modcontrato.getId());
        updatedModcontrato
            .vDesmodcon(UPDATED_V_DESMODCON)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restModcontratoMockMvc.perform(put("/api/modcontratoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedModcontrato)))
            .andExpect(status().isOk());

        // Validate the Modcontrato in the database
        List<Modcontrato> modcontratoList = modcontratoRepository.findAll();
        assertThat(modcontratoList).hasSize(databaseSizeBeforeUpdate);
        Modcontrato testModcontrato = modcontratoList.get(modcontratoList.size() - 1);
        assertThat(testModcontrato.getvDesmodcon()).isEqualTo(UPDATED_V_DESMODCON);
        assertThat(testModcontrato.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testModcontrato.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testModcontrato.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testModcontrato.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testModcontrato.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testModcontrato.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testModcontrato.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Modcontrato in Elasticsearch
        Modcontrato modcontratoEs = modcontratoSearchRepository.findOne(testModcontrato.getId());
        assertThat(modcontratoEs).isEqualToComparingFieldByField(testModcontrato);
    }

    @Test
    @Transactional
    public void updateNonExistingModcontrato() throws Exception {
        int databaseSizeBeforeUpdate = modcontratoRepository.findAll().size();

        // Create the Modcontrato

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restModcontratoMockMvc.perform(put("/api/modcontratoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modcontrato)))
            .andExpect(status().isCreated());

        // Validate the Modcontrato in the database
        List<Modcontrato> modcontratoList = modcontratoRepository.findAll();
        assertThat(modcontratoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteModcontrato() throws Exception {
        // Initialize the database
        modcontratoRepository.saveAndFlush(modcontrato);
        modcontratoSearchRepository.save(modcontrato);
        int databaseSizeBeforeDelete = modcontratoRepository.findAll().size();

        // Get the modcontrato
        restModcontratoMockMvc.perform(delete("/api/modcontratoes/{id}", modcontrato.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean modcontratoExistsInEs = modcontratoSearchRepository.exists(modcontrato.getId());
        assertThat(modcontratoExistsInEs).isFalse();

        // Validate the database is empty
        List<Modcontrato> modcontratoList = modcontratoRepository.findAll();
        assertThat(modcontratoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchModcontrato() throws Exception {
        // Initialize the database
        modcontratoRepository.saveAndFlush(modcontrato);
        modcontratoSearchRepository.save(modcontrato);

        // Search the modcontrato
        restModcontratoMockMvc.perform(get("/api/_search/modcontratoes?query=id:" + modcontrato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modcontrato.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesmodcon").value(hasItem(DEFAULT_V_DESMODCON.toString())))
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
        TestUtil.equalsVerifier(Modcontrato.class);
        Modcontrato modcontrato1 = new Modcontrato();
        modcontrato1.setId(1L);
        Modcontrato modcontrato2 = new Modcontrato();
        modcontrato2.setId(modcontrato1.getId());
        assertThat(modcontrato1).isEqualTo(modcontrato2);
        modcontrato2.setId(2L);
        assertThat(modcontrato1).isNotEqualTo(modcontrato2);
        modcontrato1.setId(null);
        assertThat(modcontrato1).isNotEqualTo(modcontrato2);
    }
}
