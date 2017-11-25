package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Trabajador;
import pe.gob.trabajo.repository.TrabajadorRepository;
import pe.gob.trabajo.repository.search.TrabajadorSearchRepository;
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
 * Test class for the TrabajadorResource REST controller.
 *
 * @see TrabajadorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class TrabajadorResourceIntTest {

    private static final Boolean DEFAULT_N_FLGSUCES = false;
    private static final Boolean UPDATED_N_FLGSUCES = true;

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
    private TrabajadorRepository trabajadorRepository;

    @Autowired
    private TrabajadorSearchRepository trabajadorSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTrabajadorMockMvc;

    private Trabajador trabajador;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrabajadorResource trabajadorResource = new TrabajadorResource(trabajadorRepository, trabajadorSearchRepository);
        this.restTrabajadorMockMvc = MockMvcBuilders.standaloneSetup(trabajadorResource)
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
    public static Trabajador createEntity(EntityManager em) {
        Trabajador trabajador = new Trabajador()
            .nFlgsuces(DEFAULT_N_FLGSUCES)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return trabajador;
    }

    @Before
    public void initTest() {
        trabajadorSearchRepository.deleteAll();
        trabajador = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrabajador() throws Exception {
        int databaseSizeBeforeCreate = trabajadorRepository.findAll().size();

        // Create the Trabajador
        restTrabajadorMockMvc.perform(post("/api/trabajadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajador)))
            .andExpect(status().isCreated());

        // Validate the Trabajador in the database
        List<Trabajador> trabajadorList = trabajadorRepository.findAll();
        assertThat(trabajadorList).hasSize(databaseSizeBeforeCreate + 1);
        Trabajador testTrabajador = trabajadorList.get(trabajadorList.size() - 1);
        assertThat(testTrabajador.isnFlgsuces()).isEqualTo(DEFAULT_N_FLGSUCES);
        assertThat(testTrabajador.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testTrabajador.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testTrabajador.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testTrabajador.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testTrabajador.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testTrabajador.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testTrabajador.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Trabajador in Elasticsearch
        Trabajador trabajadorEs = trabajadorSearchRepository.findOne(testTrabajador.getId());
        assertThat(trabajadorEs).isEqualToComparingFieldByField(testTrabajador);
    }

    @Test
    @Transactional
    public void createTrabajadorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trabajadorRepository.findAll().size();

        // Create the Trabajador with an existing ID
        trabajador.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrabajadorMockMvc.perform(post("/api/trabajadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajador)))
            .andExpect(status().isBadRequest());

        // Validate the Trabajador in the database
        List<Trabajador> trabajadorList = trabajadorRepository.findAll();
        assertThat(trabajadorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknFlgsucesIsRequired() throws Exception {
        int databaseSizeBeforeTest = trabajadorRepository.findAll().size();
        // set the field null
        trabajador.setnFlgsuces(null);

        // Create the Trabajador, which fails.

        restTrabajadorMockMvc.perform(post("/api/trabajadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajador)))
            .andExpect(status().isBadRequest());

        List<Trabajador> trabajadorList = trabajadorRepository.findAll();
        assertThat(trabajadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = trabajadorRepository.findAll().size();
        // set the field null
        trabajador.setnUsuareg(null);

        // Create the Trabajador, which fails.

        restTrabajadorMockMvc.perform(post("/api/trabajadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajador)))
            .andExpect(status().isBadRequest());

        List<Trabajador> trabajadorList = trabajadorRepository.findAll();
        assertThat(trabajadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = trabajadorRepository.findAll().size();
        // set the field null
        trabajador.settFecreg(null);

        // Create the Trabajador, which fails.

        restTrabajadorMockMvc.perform(post("/api/trabajadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajador)))
            .andExpect(status().isBadRequest());

        List<Trabajador> trabajadorList = trabajadorRepository.findAll();
        assertThat(trabajadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = trabajadorRepository.findAll().size();
        // set the field null
        trabajador.setnFlgactivo(null);

        // Create the Trabajador, which fails.

        restTrabajadorMockMvc.perform(post("/api/trabajadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajador)))
            .andExpect(status().isBadRequest());

        List<Trabajador> trabajadorList = trabajadorRepository.findAll();
        assertThat(trabajadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = trabajadorRepository.findAll().size();
        // set the field null
        trabajador.setnSedereg(null);

        // Create the Trabajador, which fails.

        restTrabajadorMockMvc.perform(post("/api/trabajadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajador)))
            .andExpect(status().isBadRequest());

        List<Trabajador> trabajadorList = trabajadorRepository.findAll();
        assertThat(trabajadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrabajadors() throws Exception {
        // Initialize the database
        trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList
        restTrabajadorMockMvc.perform(get("/api/trabajadors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trabajador.getId().intValue())))
            .andExpect(jsonPath("$.[*].nFlgsuces").value(hasItem(DEFAULT_N_FLGSUCES.booleanValue())))
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
    public void getTrabajador() throws Exception {
        // Initialize the database
        trabajadorRepository.saveAndFlush(trabajador);

        // Get the trabajador
        restTrabajadorMockMvc.perform(get("/api/trabajadors/{id}", trabajador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trabajador.getId().intValue()))
            .andExpect(jsonPath("$.nFlgsuces").value(DEFAULT_N_FLGSUCES.booleanValue()))
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
    public void getNonExistingTrabajador() throws Exception {
        // Get the trabajador
        restTrabajadorMockMvc.perform(get("/api/trabajadors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrabajador() throws Exception {
        // Initialize the database
        trabajadorRepository.saveAndFlush(trabajador);
        trabajadorSearchRepository.save(trabajador);
        int databaseSizeBeforeUpdate = trabajadorRepository.findAll().size();

        // Update the trabajador
        Trabajador updatedTrabajador = trabajadorRepository.findOne(trabajador.getId());
        updatedTrabajador
            .nFlgsuces(UPDATED_N_FLGSUCES)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restTrabajadorMockMvc.perform(put("/api/trabajadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrabajador)))
            .andExpect(status().isOk());

        // Validate the Trabajador in the database
        List<Trabajador> trabajadorList = trabajadorRepository.findAll();
        assertThat(trabajadorList).hasSize(databaseSizeBeforeUpdate);
        Trabajador testTrabajador = trabajadorList.get(trabajadorList.size() - 1);
        assertThat(testTrabajador.isnFlgsuces()).isEqualTo(UPDATED_N_FLGSUCES);
        assertThat(testTrabajador.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testTrabajador.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testTrabajador.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testTrabajador.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testTrabajador.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testTrabajador.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testTrabajador.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Trabajador in Elasticsearch
        Trabajador trabajadorEs = trabajadorSearchRepository.findOne(testTrabajador.getId());
        assertThat(trabajadorEs).isEqualToComparingFieldByField(testTrabajador);
    }

    @Test
    @Transactional
    public void updateNonExistingTrabajador() throws Exception {
        int databaseSizeBeforeUpdate = trabajadorRepository.findAll().size();

        // Create the Trabajador

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTrabajadorMockMvc.perform(put("/api/trabajadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajador)))
            .andExpect(status().isCreated());

        // Validate the Trabajador in the database
        List<Trabajador> trabajadorList = trabajadorRepository.findAll();
        assertThat(trabajadorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTrabajador() throws Exception {
        // Initialize the database
        trabajadorRepository.saveAndFlush(trabajador);
        trabajadorSearchRepository.save(trabajador);
        int databaseSizeBeforeDelete = trabajadorRepository.findAll().size();

        // Get the trabajador
        restTrabajadorMockMvc.perform(delete("/api/trabajadors/{id}", trabajador.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean trabajadorExistsInEs = trabajadorSearchRepository.exists(trabajador.getId());
        assertThat(trabajadorExistsInEs).isFalse();

        // Validate the database is empty
        List<Trabajador> trabajadorList = trabajadorRepository.findAll();
        assertThat(trabajadorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTrabajador() throws Exception {
        // Initialize the database
        trabajadorRepository.saveAndFlush(trabajador);
        trabajadorSearchRepository.save(trabajador);

        // Search the trabajador
        restTrabajadorMockMvc.perform(get("/api/_search/trabajadors?query=id:" + trabajador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trabajador.getId().intValue())))
            .andExpect(jsonPath("$.[*].nFlgsuces").value(hasItem(DEFAULT_N_FLGSUCES.booleanValue())))
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
        TestUtil.equalsVerifier(Trabajador.class);
        Trabajador trabajador1 = new Trabajador();
        trabajador1.setId(1L);
        Trabajador trabajador2 = new Trabajador();
        trabajador2.setId(trabajador1.getId());
        assertThat(trabajador1).isEqualTo(trabajador2);
        trabajador2.setId(2L);
        assertThat(trabajador1).isNotEqualTo(trabajador2);
        trabajador1.setId(null);
        assertThat(trabajador1).isNotEqualTo(trabajador2);
    }
}
