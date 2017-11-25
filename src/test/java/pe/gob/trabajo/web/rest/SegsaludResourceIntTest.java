package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Segsalud;
import pe.gob.trabajo.repository.SegsaludRepository;
import pe.gob.trabajo.repository.search.SegsaludSearchRepository;
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
 * Test class for the SegsaludResource REST controller.
 *
 * @see SegsaludResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class SegsaludResourceIntTest {

    private static final String DEFAULT_V_SEGSAL = "AAAAAAAAAA";
    private static final String UPDATED_V_SEGSAL = "BBBBBBBBBB";

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
    private SegsaludRepository segsaludRepository;

    @Autowired
    private SegsaludSearchRepository segsaludSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSegsaludMockMvc;

    private Segsalud segsalud;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SegsaludResource segsaludResource = new SegsaludResource(segsaludRepository, segsaludSearchRepository);
        this.restSegsaludMockMvc = MockMvcBuilders.standaloneSetup(segsaludResource)
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
    public static Segsalud createEntity(EntityManager em) {
        Segsalud segsalud = new Segsalud()
            .vSegsal(DEFAULT_V_SEGSAL)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return segsalud;
    }

    @Before
    public void initTest() {
        segsaludSearchRepository.deleteAll();
        segsalud = createEntity(em);
    }

    @Test
    @Transactional
    public void createSegsalud() throws Exception {
        int databaseSizeBeforeCreate = segsaludRepository.findAll().size();

        // Create the Segsalud
        restSegsaludMockMvc.perform(post("/api/segsaluds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(segsalud)))
            .andExpect(status().isCreated());

        // Validate the Segsalud in the database
        List<Segsalud> segsaludList = segsaludRepository.findAll();
        assertThat(segsaludList).hasSize(databaseSizeBeforeCreate + 1);
        Segsalud testSegsalud = segsaludList.get(segsaludList.size() - 1);
        assertThat(testSegsalud.getvSegsal()).isEqualTo(DEFAULT_V_SEGSAL);
        assertThat(testSegsalud.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testSegsalud.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testSegsalud.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testSegsalud.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testSegsalud.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testSegsalud.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testSegsalud.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Segsalud in Elasticsearch
        Segsalud segsaludEs = segsaludSearchRepository.findOne(testSegsalud.getId());
        assertThat(segsaludEs).isEqualToComparingFieldByField(testSegsalud);
    }

    @Test
    @Transactional
    public void createSegsaludWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = segsaludRepository.findAll().size();

        // Create the Segsalud with an existing ID
        segsalud.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSegsaludMockMvc.perform(post("/api/segsaluds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(segsalud)))
            .andExpect(status().isBadRequest());

        // Validate the Segsalud in the database
        List<Segsalud> segsaludList = segsaludRepository.findAll();
        assertThat(segsaludList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvSegsalIsRequired() throws Exception {
        int databaseSizeBeforeTest = segsaludRepository.findAll().size();
        // set the field null
        segsalud.setvSegsal(null);

        // Create the Segsalud, which fails.

        restSegsaludMockMvc.perform(post("/api/segsaluds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(segsalud)))
            .andExpect(status().isBadRequest());

        List<Segsalud> segsaludList = segsaludRepository.findAll();
        assertThat(segsaludList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = segsaludRepository.findAll().size();
        // set the field null
        segsalud.setnUsuareg(null);

        // Create the Segsalud, which fails.

        restSegsaludMockMvc.perform(post("/api/segsaluds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(segsalud)))
            .andExpect(status().isBadRequest());

        List<Segsalud> segsaludList = segsaludRepository.findAll();
        assertThat(segsaludList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = segsaludRepository.findAll().size();
        // set the field null
        segsalud.settFecreg(null);

        // Create the Segsalud, which fails.

        restSegsaludMockMvc.perform(post("/api/segsaluds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(segsalud)))
            .andExpect(status().isBadRequest());

        List<Segsalud> segsaludList = segsaludRepository.findAll();
        assertThat(segsaludList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = segsaludRepository.findAll().size();
        // set the field null
        segsalud.setnFlgactivo(null);

        // Create the Segsalud, which fails.

        restSegsaludMockMvc.perform(post("/api/segsaluds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(segsalud)))
            .andExpect(status().isBadRequest());

        List<Segsalud> segsaludList = segsaludRepository.findAll();
        assertThat(segsaludList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = segsaludRepository.findAll().size();
        // set the field null
        segsalud.setnSedereg(null);

        // Create the Segsalud, which fails.

        restSegsaludMockMvc.perform(post("/api/segsaluds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(segsalud)))
            .andExpect(status().isBadRequest());

        List<Segsalud> segsaludList = segsaludRepository.findAll();
        assertThat(segsaludList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSegsaluds() throws Exception {
        // Initialize the database
        segsaludRepository.saveAndFlush(segsalud);

        // Get all the segsaludList
        restSegsaludMockMvc.perform(get("/api/segsaluds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(segsalud.getId().intValue())))
            .andExpect(jsonPath("$.[*].vSegsal").value(hasItem(DEFAULT_V_SEGSAL.toString())))
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
    public void getSegsalud() throws Exception {
        // Initialize the database
        segsaludRepository.saveAndFlush(segsalud);

        // Get the segsalud
        restSegsaludMockMvc.perform(get("/api/segsaluds/{id}", segsalud.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(segsalud.getId().intValue()))
            .andExpect(jsonPath("$.vSegsal").value(DEFAULT_V_SEGSAL.toString()))
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
    public void getNonExistingSegsalud() throws Exception {
        // Get the segsalud
        restSegsaludMockMvc.perform(get("/api/segsaluds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSegsalud() throws Exception {
        // Initialize the database
        segsaludRepository.saveAndFlush(segsalud);
        segsaludSearchRepository.save(segsalud);
        int databaseSizeBeforeUpdate = segsaludRepository.findAll().size();

        // Update the segsalud
        Segsalud updatedSegsalud = segsaludRepository.findOne(segsalud.getId());
        updatedSegsalud
            .vSegsal(UPDATED_V_SEGSAL)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restSegsaludMockMvc.perform(put("/api/segsaluds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSegsalud)))
            .andExpect(status().isOk());

        // Validate the Segsalud in the database
        List<Segsalud> segsaludList = segsaludRepository.findAll();
        assertThat(segsaludList).hasSize(databaseSizeBeforeUpdate);
        Segsalud testSegsalud = segsaludList.get(segsaludList.size() - 1);
        assertThat(testSegsalud.getvSegsal()).isEqualTo(UPDATED_V_SEGSAL);
        assertThat(testSegsalud.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testSegsalud.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testSegsalud.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testSegsalud.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testSegsalud.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testSegsalud.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testSegsalud.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Segsalud in Elasticsearch
        Segsalud segsaludEs = segsaludSearchRepository.findOne(testSegsalud.getId());
        assertThat(segsaludEs).isEqualToComparingFieldByField(testSegsalud);
    }

    @Test
    @Transactional
    public void updateNonExistingSegsalud() throws Exception {
        int databaseSizeBeforeUpdate = segsaludRepository.findAll().size();

        // Create the Segsalud

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSegsaludMockMvc.perform(put("/api/segsaluds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(segsalud)))
            .andExpect(status().isCreated());

        // Validate the Segsalud in the database
        List<Segsalud> segsaludList = segsaludRepository.findAll();
        assertThat(segsaludList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSegsalud() throws Exception {
        // Initialize the database
        segsaludRepository.saveAndFlush(segsalud);
        segsaludSearchRepository.save(segsalud);
        int databaseSizeBeforeDelete = segsaludRepository.findAll().size();

        // Get the segsalud
        restSegsaludMockMvc.perform(delete("/api/segsaluds/{id}", segsalud.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean segsaludExistsInEs = segsaludSearchRepository.exists(segsalud.getId());
        assertThat(segsaludExistsInEs).isFalse();

        // Validate the database is empty
        List<Segsalud> segsaludList = segsaludRepository.findAll();
        assertThat(segsaludList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSegsalud() throws Exception {
        // Initialize the database
        segsaludRepository.saveAndFlush(segsalud);
        segsaludSearchRepository.save(segsalud);

        // Search the segsalud
        restSegsaludMockMvc.perform(get("/api/_search/segsaluds?query=id:" + segsalud.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(segsalud.getId().intValue())))
            .andExpect(jsonPath("$.[*].vSegsal").value(hasItem(DEFAULT_V_SEGSAL.toString())))
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
        TestUtil.equalsVerifier(Segsalud.class);
        Segsalud segsalud1 = new Segsalud();
        segsalud1.setId(1L);
        Segsalud segsalud2 = new Segsalud();
        segsalud2.setId(segsalud1.getId());
        assertThat(segsalud1).isEqualTo(segsalud2);
        segsalud2.setId(2L);
        assertThat(segsalud1).isNotEqualTo(segsalud2);
        segsalud1.setId(null);
        assertThat(segsalud1).isNotEqualTo(segsalud2);
    }
}
