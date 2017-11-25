package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Interesperi;
import pe.gob.trabajo.repository.InteresperiRepository;
import pe.gob.trabajo.repository.search.InteresperiSearchRepository;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InteresperiResource REST controller.
 *
 * @see InteresperiResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class InteresperiResourceIntTest {

    private static final BigDecimal DEFAULT_N_VALINTPER = new BigDecimal(1);
    private static final BigDecimal UPDATED_N_VALINTPER = new BigDecimal(2);

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
    private InteresperiRepository interesperiRepository;

    @Autowired
    private InteresperiSearchRepository interesperiSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInteresperiMockMvc;

    private Interesperi interesperi;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InteresperiResource interesperiResource = new InteresperiResource(interesperiRepository, interesperiSearchRepository);
        this.restInteresperiMockMvc = MockMvcBuilders.standaloneSetup(interesperiResource)
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
    public static Interesperi createEntity(EntityManager em) {
        Interesperi interesperi = new Interesperi()
            .nValintper(DEFAULT_N_VALINTPER)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return interesperi;
    }

    @Before
    public void initTest() {
        interesperiSearchRepository.deleteAll();
        interesperi = createEntity(em);
    }

    @Test
    @Transactional
    public void createInteresperi() throws Exception {
        int databaseSizeBeforeCreate = interesperiRepository.findAll().size();

        // Create the Interesperi
        restInteresperiMockMvc.perform(post("/api/interesperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interesperi)))
            .andExpect(status().isCreated());

        // Validate the Interesperi in the database
        List<Interesperi> interesperiList = interesperiRepository.findAll();
        assertThat(interesperiList).hasSize(databaseSizeBeforeCreate + 1);
        Interesperi testInteresperi = interesperiList.get(interesperiList.size() - 1);
        assertThat(testInteresperi.getnValintper()).isEqualTo(DEFAULT_N_VALINTPER);
        assertThat(testInteresperi.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testInteresperi.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testInteresperi.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testInteresperi.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testInteresperi.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testInteresperi.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testInteresperi.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Interesperi in Elasticsearch
        Interesperi interesperiEs = interesperiSearchRepository.findOne(testInteresperi.getId());
        assertThat(interesperiEs).isEqualToComparingFieldByField(testInteresperi);
    }

    @Test
    @Transactional
    public void createInteresperiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = interesperiRepository.findAll().size();

        // Create the Interesperi with an existing ID
        interesperi.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInteresperiMockMvc.perform(post("/api/interesperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interesperi)))
            .andExpect(status().isBadRequest());

        // Validate the Interesperi in the database
        List<Interesperi> interesperiList = interesperiRepository.findAll();
        assertThat(interesperiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = interesperiRepository.findAll().size();
        // set the field null
        interesperi.setnUsuareg(null);

        // Create the Interesperi, which fails.

        restInteresperiMockMvc.perform(post("/api/interesperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interesperi)))
            .andExpect(status().isBadRequest());

        List<Interesperi> interesperiList = interesperiRepository.findAll();
        assertThat(interesperiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = interesperiRepository.findAll().size();
        // set the field null
        interesperi.settFecreg(null);

        // Create the Interesperi, which fails.

        restInteresperiMockMvc.perform(post("/api/interesperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interesperi)))
            .andExpect(status().isBadRequest());

        List<Interesperi> interesperiList = interesperiRepository.findAll();
        assertThat(interesperiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = interesperiRepository.findAll().size();
        // set the field null
        interesperi.setnFlgactivo(null);

        // Create the Interesperi, which fails.

        restInteresperiMockMvc.perform(post("/api/interesperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interesperi)))
            .andExpect(status().isBadRequest());

        List<Interesperi> interesperiList = interesperiRepository.findAll();
        assertThat(interesperiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = interesperiRepository.findAll().size();
        // set the field null
        interesperi.setnSedereg(null);

        // Create the Interesperi, which fails.

        restInteresperiMockMvc.perform(post("/api/interesperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interesperi)))
            .andExpect(status().isBadRequest());

        List<Interesperi> interesperiList = interesperiRepository.findAll();
        assertThat(interesperiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInteresperis() throws Exception {
        // Initialize the database
        interesperiRepository.saveAndFlush(interesperi);

        // Get all the interesperiList
        restInteresperiMockMvc.perform(get("/api/interesperis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interesperi.getId().intValue())))
            .andExpect(jsonPath("$.[*].nValintper").value(hasItem(DEFAULT_N_VALINTPER.intValue())))
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
    public void getInteresperi() throws Exception {
        // Initialize the database
        interesperiRepository.saveAndFlush(interesperi);

        // Get the interesperi
        restInteresperiMockMvc.perform(get("/api/interesperis/{id}", interesperi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(interesperi.getId().intValue()))
            .andExpect(jsonPath("$.nValintper").value(DEFAULT_N_VALINTPER.intValue()))
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
    public void getNonExistingInteresperi() throws Exception {
        // Get the interesperi
        restInteresperiMockMvc.perform(get("/api/interesperis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInteresperi() throws Exception {
        // Initialize the database
        interesperiRepository.saveAndFlush(interesperi);
        interesperiSearchRepository.save(interesperi);
        int databaseSizeBeforeUpdate = interesperiRepository.findAll().size();

        // Update the interesperi
        Interesperi updatedInteresperi = interesperiRepository.findOne(interesperi.getId());
        updatedInteresperi
            .nValintper(UPDATED_N_VALINTPER)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restInteresperiMockMvc.perform(put("/api/interesperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInteresperi)))
            .andExpect(status().isOk());

        // Validate the Interesperi in the database
        List<Interesperi> interesperiList = interesperiRepository.findAll();
        assertThat(interesperiList).hasSize(databaseSizeBeforeUpdate);
        Interesperi testInteresperi = interesperiList.get(interesperiList.size() - 1);
        assertThat(testInteresperi.getnValintper()).isEqualTo(UPDATED_N_VALINTPER);
        assertThat(testInteresperi.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testInteresperi.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testInteresperi.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testInteresperi.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testInteresperi.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testInteresperi.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testInteresperi.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Interesperi in Elasticsearch
        Interesperi interesperiEs = interesperiSearchRepository.findOne(testInteresperi.getId());
        assertThat(interesperiEs).isEqualToComparingFieldByField(testInteresperi);
    }

    @Test
    @Transactional
    public void updateNonExistingInteresperi() throws Exception {
        int databaseSizeBeforeUpdate = interesperiRepository.findAll().size();

        // Create the Interesperi

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInteresperiMockMvc.perform(put("/api/interesperis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interesperi)))
            .andExpect(status().isCreated());

        // Validate the Interesperi in the database
        List<Interesperi> interesperiList = interesperiRepository.findAll();
        assertThat(interesperiList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInteresperi() throws Exception {
        // Initialize the database
        interesperiRepository.saveAndFlush(interesperi);
        interesperiSearchRepository.save(interesperi);
        int databaseSizeBeforeDelete = interesperiRepository.findAll().size();

        // Get the interesperi
        restInteresperiMockMvc.perform(delete("/api/interesperis/{id}", interesperi.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean interesperiExistsInEs = interesperiSearchRepository.exists(interesperi.getId());
        assertThat(interesperiExistsInEs).isFalse();

        // Validate the database is empty
        List<Interesperi> interesperiList = interesperiRepository.findAll();
        assertThat(interesperiList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInteresperi() throws Exception {
        // Initialize the database
        interesperiRepository.saveAndFlush(interesperi);
        interesperiSearchRepository.save(interesperi);

        // Search the interesperi
        restInteresperiMockMvc.perform(get("/api/_search/interesperis?query=id:" + interesperi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interesperi.getId().intValue())))
            .andExpect(jsonPath("$.[*].nValintper").value(hasItem(DEFAULT_N_VALINTPER.intValue())))
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
        TestUtil.equalsVerifier(Interesperi.class);
        Interesperi interesperi1 = new Interesperi();
        interesperi1.setId(1L);
        Interesperi interesperi2 = new Interesperi();
        interesperi2.setId(interesperi1.getId());
        assertThat(interesperi1).isEqualTo(interesperi2);
        interesperi2.setId(2L);
        assertThat(interesperi1).isNotEqualTo(interesperi2);
        interesperi1.setId(null);
        assertThat(interesperi1).isNotEqualTo(interesperi2);
    }
}
