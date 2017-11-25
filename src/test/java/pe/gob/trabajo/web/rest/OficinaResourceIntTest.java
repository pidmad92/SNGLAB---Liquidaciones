package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Oficina;
import pe.gob.trabajo.repository.OficinaRepository;
import pe.gob.trabajo.repository.search.OficinaSearchRepository;
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
 * Test class for the OficinaResource REST controller.
 *
 * @see OficinaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class OficinaResourceIntTest {

    private static final String DEFAULT_V_DESOFIC = "AAAAAAAAAA";
    private static final String UPDATED_V_DESOFIC = "BBBBBBBBBB";

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
    private OficinaRepository oficinaRepository;

    @Autowired
    private OficinaSearchRepository oficinaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOficinaMockMvc;

    private Oficina oficina;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OficinaResource oficinaResource = new OficinaResource(oficinaRepository, oficinaSearchRepository);
        this.restOficinaMockMvc = MockMvcBuilders.standaloneSetup(oficinaResource)
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
    public static Oficina createEntity(EntityManager em) {
        Oficina oficina = new Oficina()
            .vDesofic(DEFAULT_V_DESOFIC)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return oficina;
    }

    @Before
    public void initTest() {
        oficinaSearchRepository.deleteAll();
        oficina = createEntity(em);
    }

    @Test
    @Transactional
    public void createOficina() throws Exception {
        int databaseSizeBeforeCreate = oficinaRepository.findAll().size();

        // Create the Oficina
        restOficinaMockMvc.perform(post("/api/oficinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(oficina)))
            .andExpect(status().isCreated());

        // Validate the Oficina in the database
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeCreate + 1);
        Oficina testOficina = oficinaList.get(oficinaList.size() - 1);
        assertThat(testOficina.getvDesofic()).isEqualTo(DEFAULT_V_DESOFIC);
        assertThat(testOficina.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testOficina.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testOficina.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testOficina.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testOficina.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testOficina.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testOficina.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Oficina in Elasticsearch
        Oficina oficinaEs = oficinaSearchRepository.findOne(testOficina.getId());
        assertThat(oficinaEs).isEqualToComparingFieldByField(testOficina);
    }

    @Test
    @Transactional
    public void createOficinaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = oficinaRepository.findAll().size();

        // Create the Oficina with an existing ID
        oficina.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOficinaMockMvc.perform(post("/api/oficinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(oficina)))
            .andExpect(status().isBadRequest());

        // Validate the Oficina in the database
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDesoficIsRequired() throws Exception {
        int databaseSizeBeforeTest = oficinaRepository.findAll().size();
        // set the field null
        oficina.setvDesofic(null);

        // Create the Oficina, which fails.

        restOficinaMockMvc.perform(post("/api/oficinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(oficina)))
            .andExpect(status().isBadRequest());

        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = oficinaRepository.findAll().size();
        // set the field null
        oficina.setnUsuareg(null);

        // Create the Oficina, which fails.

        restOficinaMockMvc.perform(post("/api/oficinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(oficina)))
            .andExpect(status().isBadRequest());

        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = oficinaRepository.findAll().size();
        // set the field null
        oficina.settFecreg(null);

        // Create the Oficina, which fails.

        restOficinaMockMvc.perform(post("/api/oficinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(oficina)))
            .andExpect(status().isBadRequest());

        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = oficinaRepository.findAll().size();
        // set the field null
        oficina.setnFlgactivo(null);

        // Create the Oficina, which fails.

        restOficinaMockMvc.perform(post("/api/oficinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(oficina)))
            .andExpect(status().isBadRequest());

        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = oficinaRepository.findAll().size();
        // set the field null
        oficina.setnSedereg(null);

        // Create the Oficina, which fails.

        restOficinaMockMvc.perform(post("/api/oficinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(oficina)))
            .andExpect(status().isBadRequest());

        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOficinas() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get all the oficinaList
        restOficinaMockMvc.perform(get("/api/oficinas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oficina.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesofic").value(hasItem(DEFAULT_V_DESOFIC.toString())))
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
    public void getOficina() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get the oficina
        restOficinaMockMvc.perform(get("/api/oficinas/{id}", oficina.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(oficina.getId().intValue()))
            .andExpect(jsonPath("$.vDesofic").value(DEFAULT_V_DESOFIC.toString()))
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
    public void getNonExistingOficina() throws Exception {
        // Get the oficina
        restOficinaMockMvc.perform(get("/api/oficinas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOficina() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);
        oficinaSearchRepository.save(oficina);
        int databaseSizeBeforeUpdate = oficinaRepository.findAll().size();

        // Update the oficina
        Oficina updatedOficina = oficinaRepository.findOne(oficina.getId());
        updatedOficina
            .vDesofic(UPDATED_V_DESOFIC)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restOficinaMockMvc.perform(put("/api/oficinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOficina)))
            .andExpect(status().isOk());

        // Validate the Oficina in the database
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeUpdate);
        Oficina testOficina = oficinaList.get(oficinaList.size() - 1);
        assertThat(testOficina.getvDesofic()).isEqualTo(UPDATED_V_DESOFIC);
        assertThat(testOficina.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testOficina.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testOficina.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testOficina.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testOficina.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testOficina.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testOficina.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Oficina in Elasticsearch
        Oficina oficinaEs = oficinaSearchRepository.findOne(testOficina.getId());
        assertThat(oficinaEs).isEqualToComparingFieldByField(testOficina);
    }

    @Test
    @Transactional
    public void updateNonExistingOficina() throws Exception {
        int databaseSizeBeforeUpdate = oficinaRepository.findAll().size();

        // Create the Oficina

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOficinaMockMvc.perform(put("/api/oficinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(oficina)))
            .andExpect(status().isCreated());

        // Validate the Oficina in the database
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOficina() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);
        oficinaSearchRepository.save(oficina);
        int databaseSizeBeforeDelete = oficinaRepository.findAll().size();

        // Get the oficina
        restOficinaMockMvc.perform(delete("/api/oficinas/{id}", oficina.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean oficinaExistsInEs = oficinaSearchRepository.exists(oficina.getId());
        assertThat(oficinaExistsInEs).isFalse();

        // Validate the database is empty
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOficina() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);
        oficinaSearchRepository.save(oficina);

        // Search the oficina
        restOficinaMockMvc.perform(get("/api/_search/oficinas?query=id:" + oficina.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oficina.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesofic").value(hasItem(DEFAULT_V_DESOFIC.toString())))
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
        TestUtil.equalsVerifier(Oficina.class);
        Oficina oficina1 = new Oficina();
        oficina1.setId(1L);
        Oficina oficina2 = new Oficina();
        oficina2.setId(oficina1.getId());
        assertThat(oficina1).isEqualTo(oficina2);
        oficina2.setId(2L);
        assertThat(oficina1).isNotEqualTo(oficina2);
        oficina1.setId(null);
        assertThat(oficina1).isNotEqualTo(oficina2);
    }
}
