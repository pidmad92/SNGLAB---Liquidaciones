package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Bancos;
import pe.gob.trabajo.repository.BancosRepository;
import pe.gob.trabajo.repository.search.BancosSearchRepository;
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

import static pe.gob.trabajo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BancosResource REST controller.
 *
 * @see BancosResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class BancosResourceIntTest {

    private static final String DEFAULT_V_NOMBANCO = "AAAAAAAAAA";
    private static final String UPDATED_V_NOMBANCO = "BBBBBBBBBB";

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
    private BancosRepository bancosRepository;

    @Autowired
    private BancosSearchRepository bancosSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBancosMockMvc;

    private Bancos bancos;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BancosResource bancosResource = new BancosResource(bancosRepository, bancosSearchRepository);
        this.restBancosMockMvc = MockMvcBuilders.standaloneSetup(bancosResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bancos createEntity(EntityManager em) {
        Bancos bancos = new Bancos()
            .vNombanco(DEFAULT_V_NOMBANCO)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return bancos;
    }

    @Before
    public void initTest() {
        bancosSearchRepository.deleteAll();
        bancos = createEntity(em);
    }

    @Test
    @Transactional
    public void createBancos() throws Exception {
        int databaseSizeBeforeCreate = bancosRepository.findAll().size();

        // Create the Bancos
        restBancosMockMvc.perform(post("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bancos)))
            .andExpect(status().isCreated());

        // Validate the Bancos in the database
        List<Bancos> bancosList = bancosRepository.findAll();
        assertThat(bancosList).hasSize(databaseSizeBeforeCreate + 1);
        Bancos testBancos = bancosList.get(bancosList.size() - 1);
        assertThat(testBancos.getvNombanco()).isEqualTo(DEFAULT_V_NOMBANCO);
        assertThat(testBancos.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testBancos.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testBancos.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testBancos.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testBancos.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testBancos.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testBancos.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Bancos in Elasticsearch
        Bancos bancosEs = bancosSearchRepository.findOne(testBancos.getId());
        assertThat(bancosEs).isEqualToComparingFieldByField(testBancos);
    }

    @Test
    @Transactional
    public void createBancosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bancosRepository.findAll().size();

        // Create the Bancos with an existing ID
        bancos.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBancosMockMvc.perform(post("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bancos)))
            .andExpect(status().isBadRequest());

        // Validate the Bancos in the database
        List<Bancos> bancosList = bancosRepository.findAll();
        assertThat(bancosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvNombancoIsRequired() throws Exception {
        int databaseSizeBeforeTest = bancosRepository.findAll().size();
        // set the field null
        bancos.setvNombanco(null);

        // Create the Bancos, which fails.

        restBancosMockMvc.perform(post("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bancos)))
            .andExpect(status().isBadRequest());

        List<Bancos> bancosList = bancosRepository.findAll();
        assertThat(bancosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = bancosRepository.findAll().size();
        // set the field null
        bancos.setnUsuareg(null);

        // Create the Bancos, which fails.

        restBancosMockMvc.perform(post("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bancos)))
            .andExpect(status().isBadRequest());

        List<Bancos> bancosList = bancosRepository.findAll();
        assertThat(bancosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = bancosRepository.findAll().size();
        // set the field null
        bancos.settFecreg(null);

        // Create the Bancos, which fails.

        restBancosMockMvc.perform(post("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bancos)))
            .andExpect(status().isBadRequest());

        List<Bancos> bancosList = bancosRepository.findAll();
        assertThat(bancosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = bancosRepository.findAll().size();
        // set the field null
        bancos.setnFlgactivo(null);

        // Create the Bancos, which fails.

        restBancosMockMvc.perform(post("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bancos)))
            .andExpect(status().isBadRequest());

        List<Bancos> bancosList = bancosRepository.findAll();
        assertThat(bancosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = bancosRepository.findAll().size();
        // set the field null
        bancos.setnSedereg(null);

        // Create the Bancos, which fails.

        restBancosMockMvc.perform(post("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bancos)))
            .andExpect(status().isBadRequest());

        List<Bancos> bancosList = bancosRepository.findAll();
        assertThat(bancosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBancos() throws Exception {
        // Initialize the database
        bancosRepository.saveAndFlush(bancos);

        // Get all the bancosList
        restBancosMockMvc.perform(get("/api/bancos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bancos.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNombanco").value(hasItem(DEFAULT_V_NOMBANCO.toString())))
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
    public void getBancos() throws Exception {
        // Initialize the database
        bancosRepository.saveAndFlush(bancos);

        // Get the bancos
        restBancosMockMvc.perform(get("/api/bancos/{id}", bancos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bancos.getId().intValue()))
            .andExpect(jsonPath("$.vNombanco").value(DEFAULT_V_NOMBANCO.toString()))
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
    public void getNonExistingBancos() throws Exception {
        // Get the bancos
        restBancosMockMvc.perform(get("/api/bancos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBancos() throws Exception {
        // Initialize the database
        bancosRepository.saveAndFlush(bancos);
        bancosSearchRepository.save(bancos);
        int databaseSizeBeforeUpdate = bancosRepository.findAll().size();

        // Update the bancos
        Bancos updatedBancos = bancosRepository.findOne(bancos.getId());
        updatedBancos
            .vNombanco(UPDATED_V_NOMBANCO)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restBancosMockMvc.perform(put("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBancos)))
            .andExpect(status().isOk());

        // Validate the Bancos in the database
        List<Bancos> bancosList = bancosRepository.findAll();
        assertThat(bancosList).hasSize(databaseSizeBeforeUpdate);
        Bancos testBancos = bancosList.get(bancosList.size() - 1);
        assertThat(testBancos.getvNombanco()).isEqualTo(UPDATED_V_NOMBANCO);
        assertThat(testBancos.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testBancos.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testBancos.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testBancos.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testBancos.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testBancos.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testBancos.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Bancos in Elasticsearch
        Bancos bancosEs = bancosSearchRepository.findOne(testBancos.getId());
        assertThat(bancosEs).isEqualToComparingFieldByField(testBancos);
    }

    @Test
    @Transactional
    public void updateNonExistingBancos() throws Exception {
        int databaseSizeBeforeUpdate = bancosRepository.findAll().size();

        // Create the Bancos

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBancosMockMvc.perform(put("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bancos)))
            .andExpect(status().isCreated());

        // Validate the Bancos in the database
        List<Bancos> bancosList = bancosRepository.findAll();
        assertThat(bancosList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBancos() throws Exception {
        // Initialize the database
        bancosRepository.saveAndFlush(bancos);
        bancosSearchRepository.save(bancos);
        int databaseSizeBeforeDelete = bancosRepository.findAll().size();

        // Get the bancos
        restBancosMockMvc.perform(delete("/api/bancos/{id}", bancos.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bancosExistsInEs = bancosSearchRepository.exists(bancos.getId());
        assertThat(bancosExistsInEs).isFalse();

        // Validate the database is empty
        List<Bancos> bancosList = bancosRepository.findAll();
        assertThat(bancosList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBancos() throws Exception {
        // Initialize the database
        bancosRepository.saveAndFlush(bancos);
        bancosSearchRepository.save(bancos);

        // Search the bancos
        restBancosMockMvc.perform(get("/api/_search/bancos?query=id:" + bancos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bancos.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNombanco").value(hasItem(DEFAULT_V_NOMBANCO.toString())))
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
        TestUtil.equalsVerifier(Bancos.class);
        Bancos bancos1 = new Bancos();
        bancos1.setId(1L);
        Bancos bancos2 = new Bancos();
        bancos2.setId(bancos1.getId());
        assertThat(bancos1).isEqualTo(bancos2);
        bancos2.setId(2L);
        assertThat(bancos1).isNotEqualTo(bancos2);
        bancos1.setId(null);
        assertThat(bancos1).isNotEqualTo(bancos2);
    }
}
