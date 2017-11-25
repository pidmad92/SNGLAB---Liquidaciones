package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Motatenofic;
import pe.gob.trabajo.repository.MotatenoficRepository;
import pe.gob.trabajo.repository.search.MotatenoficSearchRepository;
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
 * Test class for the MotatenoficResource REST controller.
 *
 * @see MotatenoficResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class MotatenoficResourceIntTest {

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
    private MotatenoficRepository motatenoficRepository;

    @Autowired
    private MotatenoficSearchRepository motatenoficSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMotatenoficMockMvc;

    private Motatenofic motatenofic;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MotatenoficResource motatenoficResource = new MotatenoficResource(motatenoficRepository, motatenoficSearchRepository);
        this.restMotatenoficMockMvc = MockMvcBuilders.standaloneSetup(motatenoficResource)
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
    public static Motatenofic createEntity(EntityManager em) {
        Motatenofic motatenofic = new Motatenofic()
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return motatenofic;
    }

    @Before
    public void initTest() {
        motatenoficSearchRepository.deleteAll();
        motatenofic = createEntity(em);
    }

    @Test
    @Transactional
    public void createMotatenofic() throws Exception {
        int databaseSizeBeforeCreate = motatenoficRepository.findAll().size();

        // Create the Motatenofic
        restMotatenoficMockMvc.perform(post("/api/motatenofics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motatenofic)))
            .andExpect(status().isCreated());

        // Validate the Motatenofic in the database
        List<Motatenofic> motatenoficList = motatenoficRepository.findAll();
        assertThat(motatenoficList).hasSize(databaseSizeBeforeCreate + 1);
        Motatenofic testMotatenofic = motatenoficList.get(motatenoficList.size() - 1);
        assertThat(testMotatenofic.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testMotatenofic.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testMotatenofic.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testMotatenofic.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testMotatenofic.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testMotatenofic.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testMotatenofic.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Motatenofic in Elasticsearch
        Motatenofic motatenoficEs = motatenoficSearchRepository.findOne(testMotatenofic.getId());
        assertThat(motatenoficEs).isEqualToComparingFieldByField(testMotatenofic);
    }

    @Test
    @Transactional
    public void createMotatenoficWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = motatenoficRepository.findAll().size();

        // Create the Motatenofic with an existing ID
        motatenofic.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMotatenoficMockMvc.perform(post("/api/motatenofics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motatenofic)))
            .andExpect(status().isBadRequest());

        // Validate the Motatenofic in the database
        List<Motatenofic> motatenoficList = motatenoficRepository.findAll();
        assertThat(motatenoficList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = motatenoficRepository.findAll().size();
        // set the field null
        motatenofic.setnUsuareg(null);

        // Create the Motatenofic, which fails.

        restMotatenoficMockMvc.perform(post("/api/motatenofics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motatenofic)))
            .andExpect(status().isBadRequest());

        List<Motatenofic> motatenoficList = motatenoficRepository.findAll();
        assertThat(motatenoficList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = motatenoficRepository.findAll().size();
        // set the field null
        motatenofic.settFecreg(null);

        // Create the Motatenofic, which fails.

        restMotatenoficMockMvc.perform(post("/api/motatenofics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motatenofic)))
            .andExpect(status().isBadRequest());

        List<Motatenofic> motatenoficList = motatenoficRepository.findAll();
        assertThat(motatenoficList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = motatenoficRepository.findAll().size();
        // set the field null
        motatenofic.setnFlgactivo(null);

        // Create the Motatenofic, which fails.

        restMotatenoficMockMvc.perform(post("/api/motatenofics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motatenofic)))
            .andExpect(status().isBadRequest());

        List<Motatenofic> motatenoficList = motatenoficRepository.findAll();
        assertThat(motatenoficList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = motatenoficRepository.findAll().size();
        // set the field null
        motatenofic.setnSedereg(null);

        // Create the Motatenofic, which fails.

        restMotatenoficMockMvc.perform(post("/api/motatenofics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motatenofic)))
            .andExpect(status().isBadRequest());

        List<Motatenofic> motatenoficList = motatenoficRepository.findAll();
        assertThat(motatenoficList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMotatenofics() throws Exception {
        // Initialize the database
        motatenoficRepository.saveAndFlush(motatenofic);

        // Get all the motatenoficList
        restMotatenoficMockMvc.perform(get("/api/motatenofics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motatenofic.getId().intValue())))
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
    public void getMotatenofic() throws Exception {
        // Initialize the database
        motatenoficRepository.saveAndFlush(motatenofic);

        // Get the motatenofic
        restMotatenoficMockMvc.perform(get("/api/motatenofics/{id}", motatenofic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(motatenofic.getId().intValue()))
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
    public void getNonExistingMotatenofic() throws Exception {
        // Get the motatenofic
        restMotatenoficMockMvc.perform(get("/api/motatenofics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMotatenofic() throws Exception {
        // Initialize the database
        motatenoficRepository.saveAndFlush(motatenofic);
        motatenoficSearchRepository.save(motatenofic);
        int databaseSizeBeforeUpdate = motatenoficRepository.findAll().size();

        // Update the motatenofic
        Motatenofic updatedMotatenofic = motatenoficRepository.findOne(motatenofic.getId());
        updatedMotatenofic
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restMotatenoficMockMvc.perform(put("/api/motatenofics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMotatenofic)))
            .andExpect(status().isOk());

        // Validate the Motatenofic in the database
        List<Motatenofic> motatenoficList = motatenoficRepository.findAll();
        assertThat(motatenoficList).hasSize(databaseSizeBeforeUpdate);
        Motatenofic testMotatenofic = motatenoficList.get(motatenoficList.size() - 1);
        assertThat(testMotatenofic.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testMotatenofic.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testMotatenofic.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testMotatenofic.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testMotatenofic.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testMotatenofic.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testMotatenofic.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Motatenofic in Elasticsearch
        Motatenofic motatenoficEs = motatenoficSearchRepository.findOne(testMotatenofic.getId());
        assertThat(motatenoficEs).isEqualToComparingFieldByField(testMotatenofic);
    }

    @Test
    @Transactional
    public void updateNonExistingMotatenofic() throws Exception {
        int databaseSizeBeforeUpdate = motatenoficRepository.findAll().size();

        // Create the Motatenofic

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMotatenoficMockMvc.perform(put("/api/motatenofics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motatenofic)))
            .andExpect(status().isCreated());

        // Validate the Motatenofic in the database
        List<Motatenofic> motatenoficList = motatenoficRepository.findAll();
        assertThat(motatenoficList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMotatenofic() throws Exception {
        // Initialize the database
        motatenoficRepository.saveAndFlush(motatenofic);
        motatenoficSearchRepository.save(motatenofic);
        int databaseSizeBeforeDelete = motatenoficRepository.findAll().size();

        // Get the motatenofic
        restMotatenoficMockMvc.perform(delete("/api/motatenofics/{id}", motatenofic.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean motatenoficExistsInEs = motatenoficSearchRepository.exists(motatenofic.getId());
        assertThat(motatenoficExistsInEs).isFalse();

        // Validate the database is empty
        List<Motatenofic> motatenoficList = motatenoficRepository.findAll();
        assertThat(motatenoficList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMotatenofic() throws Exception {
        // Initialize the database
        motatenoficRepository.saveAndFlush(motatenofic);
        motatenoficSearchRepository.save(motatenofic);

        // Search the motatenofic
        restMotatenoficMockMvc.perform(get("/api/_search/motatenofics?query=id:" + motatenofic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motatenofic.getId().intValue())))
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
        TestUtil.equalsVerifier(Motatenofic.class);
        Motatenofic motatenofic1 = new Motatenofic();
        motatenofic1.setId(1L);
        Motatenofic motatenofic2 = new Motatenofic();
        motatenofic2.setId(motatenofic1.getId());
        assertThat(motatenofic1).isEqualTo(motatenofic2);
        motatenofic2.setId(2L);
        assertThat(motatenofic1).isNotEqualTo(motatenofic2);
        motatenofic1.setId(null);
        assertThat(motatenofic1).isNotEqualTo(motatenofic2);
    }
}
