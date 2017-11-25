package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Motateselec;
import pe.gob.trabajo.repository.MotateselecRepository;
import pe.gob.trabajo.repository.search.MotateselecSearchRepository;
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
 * Test class for the MotateselecResource REST controller.
 *
 * @see MotateselecResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class MotateselecResourceIntTest {

    private static final String DEFAULT_V_OBSMOSEAT = "AAAAAAAAAA";
    private static final String UPDATED_V_OBSMOSEAT = "BBBBBBBBBB";

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
    private MotateselecRepository motateselecRepository;

    @Autowired
    private MotateselecSearchRepository motateselecSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMotateselecMockMvc;

    private Motateselec motateselec;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MotateselecResource motateselecResource = new MotateselecResource(motateselecRepository, motateselecSearchRepository);
        this.restMotateselecMockMvc = MockMvcBuilders.standaloneSetup(motateselecResource)
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
    public static Motateselec createEntity(EntityManager em) {
        Motateselec motateselec = new Motateselec()
            .vObsmoseat(DEFAULT_V_OBSMOSEAT)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return motateselec;
    }

    @Before
    public void initTest() {
        motateselecSearchRepository.deleteAll();
        motateselec = createEntity(em);
    }

    @Test
    @Transactional
    public void createMotateselec() throws Exception {
        int databaseSizeBeforeCreate = motateselecRepository.findAll().size();

        // Create the Motateselec
        restMotateselecMockMvc.perform(post("/api/motateselecs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motateselec)))
            .andExpect(status().isCreated());

        // Validate the Motateselec in the database
        List<Motateselec> motateselecList = motateselecRepository.findAll();
        assertThat(motateselecList).hasSize(databaseSizeBeforeCreate + 1);
        Motateselec testMotateselec = motateselecList.get(motateselecList.size() - 1);
        assertThat(testMotateselec.getvObsmoseat()).isEqualTo(DEFAULT_V_OBSMOSEAT);
        assertThat(testMotateselec.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testMotateselec.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testMotateselec.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testMotateselec.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testMotateselec.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testMotateselec.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testMotateselec.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Motateselec in Elasticsearch
        Motateselec motateselecEs = motateselecSearchRepository.findOne(testMotateselec.getId());
        assertThat(motateselecEs).isEqualToComparingFieldByField(testMotateselec);
    }

    @Test
    @Transactional
    public void createMotateselecWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = motateselecRepository.findAll().size();

        // Create the Motateselec with an existing ID
        motateselec.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMotateselecMockMvc.perform(post("/api/motateselecs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motateselec)))
            .andExpect(status().isBadRequest());

        // Validate the Motateselec in the database
        List<Motateselec> motateselecList = motateselecRepository.findAll();
        assertThat(motateselecList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = motateselecRepository.findAll().size();
        // set the field null
        motateselec.setnUsuareg(null);

        // Create the Motateselec, which fails.

        restMotateselecMockMvc.perform(post("/api/motateselecs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motateselec)))
            .andExpect(status().isBadRequest());

        List<Motateselec> motateselecList = motateselecRepository.findAll();
        assertThat(motateselecList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = motateselecRepository.findAll().size();
        // set the field null
        motateselec.settFecreg(null);

        // Create the Motateselec, which fails.

        restMotateselecMockMvc.perform(post("/api/motateselecs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motateselec)))
            .andExpect(status().isBadRequest());

        List<Motateselec> motateselecList = motateselecRepository.findAll();
        assertThat(motateselecList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = motateselecRepository.findAll().size();
        // set the field null
        motateselec.setnFlgactivo(null);

        // Create the Motateselec, which fails.

        restMotateselecMockMvc.perform(post("/api/motateselecs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motateselec)))
            .andExpect(status().isBadRequest());

        List<Motateselec> motateselecList = motateselecRepository.findAll();
        assertThat(motateselecList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = motateselecRepository.findAll().size();
        // set the field null
        motateselec.setnSedereg(null);

        // Create the Motateselec, which fails.

        restMotateselecMockMvc.perform(post("/api/motateselecs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motateselec)))
            .andExpect(status().isBadRequest());

        List<Motateselec> motateselecList = motateselecRepository.findAll();
        assertThat(motateselecList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMotateselecs() throws Exception {
        // Initialize the database
        motateselecRepository.saveAndFlush(motateselec);

        // Get all the motateselecList
        restMotateselecMockMvc.perform(get("/api/motateselecs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motateselec.getId().intValue())))
            .andExpect(jsonPath("$.[*].vObsmoseat").value(hasItem(DEFAULT_V_OBSMOSEAT.toString())))
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
    public void getMotateselec() throws Exception {
        // Initialize the database
        motateselecRepository.saveAndFlush(motateselec);

        // Get the motateselec
        restMotateselecMockMvc.perform(get("/api/motateselecs/{id}", motateselec.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(motateselec.getId().intValue()))
            .andExpect(jsonPath("$.vObsmoseat").value(DEFAULT_V_OBSMOSEAT.toString()))
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
    public void getNonExistingMotateselec() throws Exception {
        // Get the motateselec
        restMotateselecMockMvc.perform(get("/api/motateselecs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMotateselec() throws Exception {
        // Initialize the database
        motateselecRepository.saveAndFlush(motateselec);
        motateselecSearchRepository.save(motateselec);
        int databaseSizeBeforeUpdate = motateselecRepository.findAll().size();

        // Update the motateselec
        Motateselec updatedMotateselec = motateselecRepository.findOne(motateselec.getId());
        updatedMotateselec
            .vObsmoseat(UPDATED_V_OBSMOSEAT)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restMotateselecMockMvc.perform(put("/api/motateselecs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMotateselec)))
            .andExpect(status().isOk());

        // Validate the Motateselec in the database
        List<Motateselec> motateselecList = motateselecRepository.findAll();
        assertThat(motateselecList).hasSize(databaseSizeBeforeUpdate);
        Motateselec testMotateselec = motateselecList.get(motateselecList.size() - 1);
        assertThat(testMotateselec.getvObsmoseat()).isEqualTo(UPDATED_V_OBSMOSEAT);
        assertThat(testMotateselec.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testMotateselec.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testMotateselec.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testMotateselec.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testMotateselec.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testMotateselec.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testMotateselec.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Motateselec in Elasticsearch
        Motateselec motateselecEs = motateselecSearchRepository.findOne(testMotateselec.getId());
        assertThat(motateselecEs).isEqualToComparingFieldByField(testMotateselec);
    }

    @Test
    @Transactional
    public void updateNonExistingMotateselec() throws Exception {
        int databaseSizeBeforeUpdate = motateselecRepository.findAll().size();

        // Create the Motateselec

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMotateselecMockMvc.perform(put("/api/motateselecs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motateselec)))
            .andExpect(status().isCreated());

        // Validate the Motateselec in the database
        List<Motateselec> motateselecList = motateselecRepository.findAll();
        assertThat(motateselecList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMotateselec() throws Exception {
        // Initialize the database
        motateselecRepository.saveAndFlush(motateselec);
        motateselecSearchRepository.save(motateselec);
        int databaseSizeBeforeDelete = motateselecRepository.findAll().size();

        // Get the motateselec
        restMotateselecMockMvc.perform(delete("/api/motateselecs/{id}", motateselec.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean motateselecExistsInEs = motateselecSearchRepository.exists(motateselec.getId());
        assertThat(motateselecExistsInEs).isFalse();

        // Validate the database is empty
        List<Motateselec> motateselecList = motateselecRepository.findAll();
        assertThat(motateselecList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMotateselec() throws Exception {
        // Initialize the database
        motateselecRepository.saveAndFlush(motateselec);
        motateselecSearchRepository.save(motateselec);

        // Search the motateselec
        restMotateselecMockMvc.perform(get("/api/_search/motateselecs?query=id:" + motateselec.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motateselec.getId().intValue())))
            .andExpect(jsonPath("$.[*].vObsmoseat").value(hasItem(DEFAULT_V_OBSMOSEAT.toString())))
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
        TestUtil.equalsVerifier(Motateselec.class);
        Motateselec motateselec1 = new Motateselec();
        motateselec1.setId(1L);
        Motateselec motateselec2 = new Motateselec();
        motateselec2.setId(motateselec1.getId());
        assertThat(motateselec1).isEqualTo(motateselec2);
        motateselec2.setId(2L);
        assertThat(motateselec1).isNotEqualTo(motateselec2);
        motateselec1.setId(null);
        assertThat(motateselec1).isNotEqualTo(motateselec2);
    }
}
