package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Accionadop;
import pe.gob.trabajo.repository.AccionadopRepository;
import pe.gob.trabajo.repository.search.AccionadopSearchRepository;
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
 * Test class for the AccionadopResource REST controller.
 *
 * @see AccionadopResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class AccionadopResourceIntTest {

    private static final String DEFAULT_V_DESACCDOP = "AAAAAAAAAA";
    private static final String UPDATED_V_DESACCDOP = "BBBBBBBBBB";

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
    private AccionadopRepository accionadopRepository;

    @Autowired
    private AccionadopSearchRepository accionadopSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccionadopMockMvc;

    private Accionadop accionadop;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AccionadopResource accionadopResource = new AccionadopResource(accionadopRepository, accionadopSearchRepository);
        this.restAccionadopMockMvc = MockMvcBuilders.standaloneSetup(accionadopResource)
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
    public static Accionadop createEntity(EntityManager em) {
        Accionadop accionadop = new Accionadop()
            .vDesaccdop(DEFAULT_V_DESACCDOP)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return accionadop;
    }

    @Before
    public void initTest() {
        accionadopSearchRepository.deleteAll();
        accionadop = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccionadop() throws Exception {
        int databaseSizeBeforeCreate = accionadopRepository.findAll().size();

        // Create the Accionadop
        restAccionadopMockMvc.perform(post("/api/accionadops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accionadop)))
            .andExpect(status().isCreated());

        // Validate the Accionadop in the database
        List<Accionadop> accionadopList = accionadopRepository.findAll();
        assertThat(accionadopList).hasSize(databaseSizeBeforeCreate + 1);
        Accionadop testAccionadop = accionadopList.get(accionadopList.size() - 1);
        assertThat(testAccionadop.getvDesaccdop()).isEqualTo(DEFAULT_V_DESACCDOP);
        assertThat(testAccionadop.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testAccionadop.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testAccionadop.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testAccionadop.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testAccionadop.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testAccionadop.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testAccionadop.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Accionadop in Elasticsearch
        Accionadop accionadopEs = accionadopSearchRepository.findOne(testAccionadop.getId());
        assertThat(accionadopEs).isEqualToComparingFieldByField(testAccionadop);
    }

    @Test
    @Transactional
    public void createAccionadopWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accionadopRepository.findAll().size();

        // Create the Accionadop with an existing ID
        accionadop.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccionadopMockMvc.perform(post("/api/accionadops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accionadop)))
            .andExpect(status().isBadRequest());

        // Validate the Accionadop in the database
        List<Accionadop> accionadopList = accionadopRepository.findAll();
        assertThat(accionadopList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDesaccdopIsRequired() throws Exception {
        int databaseSizeBeforeTest = accionadopRepository.findAll().size();
        // set the field null
        accionadop.setvDesaccdop(null);

        // Create the Accionadop, which fails.

        restAccionadopMockMvc.perform(post("/api/accionadops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accionadop)))
            .andExpect(status().isBadRequest());

        List<Accionadop> accionadopList = accionadopRepository.findAll();
        assertThat(accionadopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = accionadopRepository.findAll().size();
        // set the field null
        accionadop.setnUsuareg(null);

        // Create the Accionadop, which fails.

        restAccionadopMockMvc.perform(post("/api/accionadops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accionadop)))
            .andExpect(status().isBadRequest());

        List<Accionadop> accionadopList = accionadopRepository.findAll();
        assertThat(accionadopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = accionadopRepository.findAll().size();
        // set the field null
        accionadop.settFecreg(null);

        // Create the Accionadop, which fails.

        restAccionadopMockMvc.perform(post("/api/accionadops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accionadop)))
            .andExpect(status().isBadRequest());

        List<Accionadop> accionadopList = accionadopRepository.findAll();
        assertThat(accionadopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = accionadopRepository.findAll().size();
        // set the field null
        accionadop.setnFlgactivo(null);

        // Create the Accionadop, which fails.

        restAccionadopMockMvc.perform(post("/api/accionadops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accionadop)))
            .andExpect(status().isBadRequest());

        List<Accionadop> accionadopList = accionadopRepository.findAll();
        assertThat(accionadopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = accionadopRepository.findAll().size();
        // set the field null
        accionadop.setnSedereg(null);

        // Create the Accionadop, which fails.

        restAccionadopMockMvc.perform(post("/api/accionadops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accionadop)))
            .andExpect(status().isBadRequest());

        List<Accionadop> accionadopList = accionadopRepository.findAll();
        assertThat(accionadopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccionadops() throws Exception {
        // Initialize the database
        accionadopRepository.saveAndFlush(accionadop);

        // Get all the accionadopList
        restAccionadopMockMvc.perform(get("/api/accionadops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accionadop.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesaccdop").value(hasItem(DEFAULT_V_DESACCDOP.toString())))
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
    public void getAccionadop() throws Exception {
        // Initialize the database
        accionadopRepository.saveAndFlush(accionadop);

        // Get the accionadop
        restAccionadopMockMvc.perform(get("/api/accionadops/{id}", accionadop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accionadop.getId().intValue()))
            .andExpect(jsonPath("$.vDesaccdop").value(DEFAULT_V_DESACCDOP.toString()))
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
    public void getNonExistingAccionadop() throws Exception {
        // Get the accionadop
        restAccionadopMockMvc.perform(get("/api/accionadops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccionadop() throws Exception {
        // Initialize the database
        accionadopRepository.saveAndFlush(accionadop);
        accionadopSearchRepository.save(accionadop);
        int databaseSizeBeforeUpdate = accionadopRepository.findAll().size();

        // Update the accionadop
        Accionadop updatedAccionadop = accionadopRepository.findOne(accionadop.getId());
        updatedAccionadop
            .vDesaccdop(UPDATED_V_DESACCDOP)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restAccionadopMockMvc.perform(put("/api/accionadops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccionadop)))
            .andExpect(status().isOk());

        // Validate the Accionadop in the database
        List<Accionadop> accionadopList = accionadopRepository.findAll();
        assertThat(accionadopList).hasSize(databaseSizeBeforeUpdate);
        Accionadop testAccionadop = accionadopList.get(accionadopList.size() - 1);
        assertThat(testAccionadop.getvDesaccdop()).isEqualTo(UPDATED_V_DESACCDOP);
        assertThat(testAccionadop.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testAccionadop.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testAccionadop.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testAccionadop.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testAccionadop.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testAccionadop.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testAccionadop.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Accionadop in Elasticsearch
        Accionadop accionadopEs = accionadopSearchRepository.findOne(testAccionadop.getId());
        assertThat(accionadopEs).isEqualToComparingFieldByField(testAccionadop);
    }

    @Test
    @Transactional
    public void updateNonExistingAccionadop() throws Exception {
        int databaseSizeBeforeUpdate = accionadopRepository.findAll().size();

        // Create the Accionadop

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccionadopMockMvc.perform(put("/api/accionadops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accionadop)))
            .andExpect(status().isCreated());

        // Validate the Accionadop in the database
        List<Accionadop> accionadopList = accionadopRepository.findAll();
        assertThat(accionadopList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccionadop() throws Exception {
        // Initialize the database
        accionadopRepository.saveAndFlush(accionadop);
        accionadopSearchRepository.save(accionadop);
        int databaseSizeBeforeDelete = accionadopRepository.findAll().size();

        // Get the accionadop
        restAccionadopMockMvc.perform(delete("/api/accionadops/{id}", accionadop.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean accionadopExistsInEs = accionadopSearchRepository.exists(accionadop.getId());
        assertThat(accionadopExistsInEs).isFalse();

        // Validate the database is empty
        List<Accionadop> accionadopList = accionadopRepository.findAll();
        assertThat(accionadopList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAccionadop() throws Exception {
        // Initialize the database
        accionadopRepository.saveAndFlush(accionadop);
        accionadopSearchRepository.save(accionadop);

        // Search the accionadop
        restAccionadopMockMvc.perform(get("/api/_search/accionadops?query=id:" + accionadop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accionadop.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesaccdop").value(hasItem(DEFAULT_V_DESACCDOP.toString())))
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
        TestUtil.equalsVerifier(Accionadop.class);
        Accionadop accionadop1 = new Accionadop();
        accionadop1.setId(1L);
        Accionadop accionadop2 = new Accionadop();
        accionadop2.setId(accionadop1.getId());
        assertThat(accionadop1).isEqualTo(accionadop2);
        accionadop2.setId(2L);
        assertThat(accionadop1).isNotEqualTo(accionadop2);
        accionadop1.setId(null);
        assertThat(accionadop1).isNotEqualTo(accionadop2);
    }
}
