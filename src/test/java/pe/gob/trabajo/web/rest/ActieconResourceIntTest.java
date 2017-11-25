package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Actiecon;
import pe.gob.trabajo.repository.ActieconRepository;
import pe.gob.trabajo.repository.search.ActieconSearchRepository;
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
 * Test class for the ActieconResource REST controller.
 *
 * @see ActieconResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class ActieconResourceIntTest {

    private static final String DEFAULT_V_CIUUACECO = "AAAAA";
    private static final String UPDATED_V_CIUUACECO = "BBBBB";

    private static final String DEFAULT_V_DESACTECO = "AAAAAAAAAA";
    private static final String UPDATED_V_DESACTECO = "BBBBBBBBBB";

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
    private ActieconRepository actieconRepository;

    @Autowired
    private ActieconSearchRepository actieconSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActieconMockMvc;

    private Actiecon actiecon;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActieconResource actieconResource = new ActieconResource(actieconRepository, actieconSearchRepository);
        this.restActieconMockMvc = MockMvcBuilders.standaloneSetup(actieconResource)
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
    public static Actiecon createEntity(EntityManager em) {
        Actiecon actiecon = new Actiecon()
            .vCiuuaceco(DEFAULT_V_CIUUACECO)
            .vDesacteco(DEFAULT_V_DESACTECO)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return actiecon;
    }

    @Before
    public void initTest() {
        actieconSearchRepository.deleteAll();
        actiecon = createEntity(em);
    }

    @Test
    @Transactional
    public void createActiecon() throws Exception {
        int databaseSizeBeforeCreate = actieconRepository.findAll().size();

        // Create the Actiecon
        restActieconMockMvc.perform(post("/api/actiecons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actiecon)))
            .andExpect(status().isCreated());

        // Validate the Actiecon in the database
        List<Actiecon> actieconList = actieconRepository.findAll();
        assertThat(actieconList).hasSize(databaseSizeBeforeCreate + 1);
        Actiecon testActiecon = actieconList.get(actieconList.size() - 1);
        assertThat(testActiecon.getvCiuuaceco()).isEqualTo(DEFAULT_V_CIUUACECO);
        assertThat(testActiecon.getvDesacteco()).isEqualTo(DEFAULT_V_DESACTECO);
        assertThat(testActiecon.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testActiecon.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testActiecon.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testActiecon.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testActiecon.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testActiecon.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testActiecon.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Actiecon in Elasticsearch
        Actiecon actieconEs = actieconSearchRepository.findOne(testActiecon.getId());
        assertThat(actieconEs).isEqualToComparingFieldByField(testActiecon);
    }

    @Test
    @Transactional
    public void createActieconWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = actieconRepository.findAll().size();

        // Create the Actiecon with an existing ID
        actiecon.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActieconMockMvc.perform(post("/api/actiecons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actiecon)))
            .andExpect(status().isBadRequest());

        // Validate the Actiecon in the database
        List<Actiecon> actieconList = actieconRepository.findAll();
        assertThat(actieconList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvCiuuacecoIsRequired() throws Exception {
        int databaseSizeBeforeTest = actieconRepository.findAll().size();
        // set the field null
        actiecon.setvCiuuaceco(null);

        // Create the Actiecon, which fails.

        restActieconMockMvc.perform(post("/api/actiecons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actiecon)))
            .andExpect(status().isBadRequest());

        List<Actiecon> actieconList = actieconRepository.findAll();
        assertThat(actieconList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkvDesactecoIsRequired() throws Exception {
        int databaseSizeBeforeTest = actieconRepository.findAll().size();
        // set the field null
        actiecon.setvDesacteco(null);

        // Create the Actiecon, which fails.

        restActieconMockMvc.perform(post("/api/actiecons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actiecon)))
            .andExpect(status().isBadRequest());

        List<Actiecon> actieconList = actieconRepository.findAll();
        assertThat(actieconList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = actieconRepository.findAll().size();
        // set the field null
        actiecon.setnUsuareg(null);

        // Create the Actiecon, which fails.

        restActieconMockMvc.perform(post("/api/actiecons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actiecon)))
            .andExpect(status().isBadRequest());

        List<Actiecon> actieconList = actieconRepository.findAll();
        assertThat(actieconList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = actieconRepository.findAll().size();
        // set the field null
        actiecon.settFecreg(null);

        // Create the Actiecon, which fails.

        restActieconMockMvc.perform(post("/api/actiecons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actiecon)))
            .andExpect(status().isBadRequest());

        List<Actiecon> actieconList = actieconRepository.findAll();
        assertThat(actieconList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = actieconRepository.findAll().size();
        // set the field null
        actiecon.setnFlgactivo(null);

        // Create the Actiecon, which fails.

        restActieconMockMvc.perform(post("/api/actiecons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actiecon)))
            .andExpect(status().isBadRequest());

        List<Actiecon> actieconList = actieconRepository.findAll();
        assertThat(actieconList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = actieconRepository.findAll().size();
        // set the field null
        actiecon.setnSedereg(null);

        // Create the Actiecon, which fails.

        restActieconMockMvc.perform(post("/api/actiecons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actiecon)))
            .andExpect(status().isBadRequest());

        List<Actiecon> actieconList = actieconRepository.findAll();
        assertThat(actieconList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActiecons() throws Exception {
        // Initialize the database
        actieconRepository.saveAndFlush(actiecon);

        // Get all the actieconList
        restActieconMockMvc.perform(get("/api/actiecons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actiecon.getId().intValue())))
            .andExpect(jsonPath("$.[*].vCiuuaceco").value(hasItem(DEFAULT_V_CIUUACECO.toString())))
            .andExpect(jsonPath("$.[*].vDesacteco").value(hasItem(DEFAULT_V_DESACTECO.toString())))
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
    public void getActiecon() throws Exception {
        // Initialize the database
        actieconRepository.saveAndFlush(actiecon);

        // Get the actiecon
        restActieconMockMvc.perform(get("/api/actiecons/{id}", actiecon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(actiecon.getId().intValue()))
            .andExpect(jsonPath("$.vCiuuaceco").value(DEFAULT_V_CIUUACECO.toString()))
            .andExpect(jsonPath("$.vDesacteco").value(DEFAULT_V_DESACTECO.toString()))
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
    public void getNonExistingActiecon() throws Exception {
        // Get the actiecon
        restActieconMockMvc.perform(get("/api/actiecons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActiecon() throws Exception {
        // Initialize the database
        actieconRepository.saveAndFlush(actiecon);
        actieconSearchRepository.save(actiecon);
        int databaseSizeBeforeUpdate = actieconRepository.findAll().size();

        // Update the actiecon
        Actiecon updatedActiecon = actieconRepository.findOne(actiecon.getId());
        updatedActiecon
            .vCiuuaceco(UPDATED_V_CIUUACECO)
            .vDesacteco(UPDATED_V_DESACTECO)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restActieconMockMvc.perform(put("/api/actiecons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedActiecon)))
            .andExpect(status().isOk());

        // Validate the Actiecon in the database
        List<Actiecon> actieconList = actieconRepository.findAll();
        assertThat(actieconList).hasSize(databaseSizeBeforeUpdate);
        Actiecon testActiecon = actieconList.get(actieconList.size() - 1);
        assertThat(testActiecon.getvCiuuaceco()).isEqualTo(UPDATED_V_CIUUACECO);
        assertThat(testActiecon.getvDesacteco()).isEqualTo(UPDATED_V_DESACTECO);
        assertThat(testActiecon.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testActiecon.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testActiecon.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testActiecon.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testActiecon.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testActiecon.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testActiecon.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Actiecon in Elasticsearch
        Actiecon actieconEs = actieconSearchRepository.findOne(testActiecon.getId());
        assertThat(actieconEs).isEqualToComparingFieldByField(testActiecon);
    }

    @Test
    @Transactional
    public void updateNonExistingActiecon() throws Exception {
        int databaseSizeBeforeUpdate = actieconRepository.findAll().size();

        // Create the Actiecon

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restActieconMockMvc.perform(put("/api/actiecons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actiecon)))
            .andExpect(status().isCreated());

        // Validate the Actiecon in the database
        List<Actiecon> actieconList = actieconRepository.findAll();
        assertThat(actieconList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteActiecon() throws Exception {
        // Initialize the database
        actieconRepository.saveAndFlush(actiecon);
        actieconSearchRepository.save(actiecon);
        int databaseSizeBeforeDelete = actieconRepository.findAll().size();

        // Get the actiecon
        restActieconMockMvc.perform(delete("/api/actiecons/{id}", actiecon.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean actieconExistsInEs = actieconSearchRepository.exists(actiecon.getId());
        assertThat(actieconExistsInEs).isFalse();

        // Validate the database is empty
        List<Actiecon> actieconList = actieconRepository.findAll();
        assertThat(actieconList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchActiecon() throws Exception {
        // Initialize the database
        actieconRepository.saveAndFlush(actiecon);
        actieconSearchRepository.save(actiecon);

        // Search the actiecon
        restActieconMockMvc.perform(get("/api/_search/actiecons?query=id:" + actiecon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actiecon.getId().intValue())))
            .andExpect(jsonPath("$.[*].vCiuuaceco").value(hasItem(DEFAULT_V_CIUUACECO.toString())))
            .andExpect(jsonPath("$.[*].vDesacteco").value(hasItem(DEFAULT_V_DESACTECO.toString())))
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
        TestUtil.equalsVerifier(Actiecon.class);
        Actiecon actiecon1 = new Actiecon();
        actiecon1.setId(1L);
        Actiecon actiecon2 = new Actiecon();
        actiecon2.setId(actiecon1.getId());
        assertThat(actiecon1).isEqualTo(actiecon2);
        actiecon2.setId(2L);
        assertThat(actiecon1).isNotEqualTo(actiecon2);
        actiecon1.setId(null);
        assertThat(actiecon1).isNotEqualTo(actiecon2);
    }
}
