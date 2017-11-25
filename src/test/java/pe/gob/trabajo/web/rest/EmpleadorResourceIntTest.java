package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Empleador;
import pe.gob.trabajo.repository.EmpleadorRepository;
import pe.gob.trabajo.repository.search.EmpleadorSearchRepository;
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
 * Test class for the EmpleadorResource REST controller.
 *
 * @see EmpleadorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class EmpleadorResourceIntTest {

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
    private EmpleadorRepository empleadorRepository;

    @Autowired
    private EmpleadorSearchRepository empleadorSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmpleadorMockMvc;

    private Empleador empleador;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmpleadorResource empleadorResource = new EmpleadorResource(empleadorRepository, empleadorSearchRepository);
        this.restEmpleadorMockMvc = MockMvcBuilders.standaloneSetup(empleadorResource)
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
    public static Empleador createEntity(EntityManager em) {
        Empleador empleador = new Empleador()
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return empleador;
    }

    @Before
    public void initTest() {
        empleadorSearchRepository.deleteAll();
        empleador = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmpleador() throws Exception {
        int databaseSizeBeforeCreate = empleadorRepository.findAll().size();

        // Create the Empleador
        restEmpleadorMockMvc.perform(post("/api/empleadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empleador)))
            .andExpect(status().isCreated());

        // Validate the Empleador in the database
        List<Empleador> empleadorList = empleadorRepository.findAll();
        assertThat(empleadorList).hasSize(databaseSizeBeforeCreate + 1);
        Empleador testEmpleador = empleadorList.get(empleadorList.size() - 1);
        assertThat(testEmpleador.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testEmpleador.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testEmpleador.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testEmpleador.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testEmpleador.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testEmpleador.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testEmpleador.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Empleador in Elasticsearch
        Empleador empleadorEs = empleadorSearchRepository.findOne(testEmpleador.getId());
        assertThat(empleadorEs).isEqualToComparingFieldByField(testEmpleador);
    }

    @Test
    @Transactional
    public void createEmpleadorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = empleadorRepository.findAll().size();

        // Create the Empleador with an existing ID
        empleador.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpleadorMockMvc.perform(post("/api/empleadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empleador)))
            .andExpect(status().isBadRequest());

        // Validate the Empleador in the database
        List<Empleador> empleadorList = empleadorRepository.findAll();
        assertThat(empleadorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = empleadorRepository.findAll().size();
        // set the field null
        empleador.setnUsuareg(null);

        // Create the Empleador, which fails.

        restEmpleadorMockMvc.perform(post("/api/empleadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empleador)))
            .andExpect(status().isBadRequest());

        List<Empleador> empleadorList = empleadorRepository.findAll();
        assertThat(empleadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = empleadorRepository.findAll().size();
        // set the field null
        empleador.settFecreg(null);

        // Create the Empleador, which fails.

        restEmpleadorMockMvc.perform(post("/api/empleadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empleador)))
            .andExpect(status().isBadRequest());

        List<Empleador> empleadorList = empleadorRepository.findAll();
        assertThat(empleadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = empleadorRepository.findAll().size();
        // set the field null
        empleador.setnFlgactivo(null);

        // Create the Empleador, which fails.

        restEmpleadorMockMvc.perform(post("/api/empleadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empleador)))
            .andExpect(status().isBadRequest());

        List<Empleador> empleadorList = empleadorRepository.findAll();
        assertThat(empleadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = empleadorRepository.findAll().size();
        // set the field null
        empleador.setnSedereg(null);

        // Create the Empleador, which fails.

        restEmpleadorMockMvc.perform(post("/api/empleadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empleador)))
            .andExpect(status().isBadRequest());

        List<Empleador> empleadorList = empleadorRepository.findAll();
        assertThat(empleadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmpleadors() throws Exception {
        // Initialize the database
        empleadorRepository.saveAndFlush(empleador);

        // Get all the empleadorList
        restEmpleadorMockMvc.perform(get("/api/empleadors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empleador.getId().intValue())))
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
    public void getEmpleador() throws Exception {
        // Initialize the database
        empleadorRepository.saveAndFlush(empleador);

        // Get the empleador
        restEmpleadorMockMvc.perform(get("/api/empleadors/{id}", empleador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(empleador.getId().intValue()))
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
    public void getNonExistingEmpleador() throws Exception {
        // Get the empleador
        restEmpleadorMockMvc.perform(get("/api/empleadors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmpleador() throws Exception {
        // Initialize the database
        empleadorRepository.saveAndFlush(empleador);
        empleadorSearchRepository.save(empleador);
        int databaseSizeBeforeUpdate = empleadorRepository.findAll().size();

        // Update the empleador
        Empleador updatedEmpleador = empleadorRepository.findOne(empleador.getId());
        updatedEmpleador
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restEmpleadorMockMvc.perform(put("/api/empleadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmpleador)))
            .andExpect(status().isOk());

        // Validate the Empleador in the database
        List<Empleador> empleadorList = empleadorRepository.findAll();
        assertThat(empleadorList).hasSize(databaseSizeBeforeUpdate);
        Empleador testEmpleador = empleadorList.get(empleadorList.size() - 1);
        assertThat(testEmpleador.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testEmpleador.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testEmpleador.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testEmpleador.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testEmpleador.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testEmpleador.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testEmpleador.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Empleador in Elasticsearch
        Empleador empleadorEs = empleadorSearchRepository.findOne(testEmpleador.getId());
        assertThat(empleadorEs).isEqualToComparingFieldByField(testEmpleador);
    }

    @Test
    @Transactional
    public void updateNonExistingEmpleador() throws Exception {
        int databaseSizeBeforeUpdate = empleadorRepository.findAll().size();

        // Create the Empleador

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmpleadorMockMvc.perform(put("/api/empleadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empleador)))
            .andExpect(status().isCreated());

        // Validate the Empleador in the database
        List<Empleador> empleadorList = empleadorRepository.findAll();
        assertThat(empleadorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmpleador() throws Exception {
        // Initialize the database
        empleadorRepository.saveAndFlush(empleador);
        empleadorSearchRepository.save(empleador);
        int databaseSizeBeforeDelete = empleadorRepository.findAll().size();

        // Get the empleador
        restEmpleadorMockMvc.perform(delete("/api/empleadors/{id}", empleador.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean empleadorExistsInEs = empleadorSearchRepository.exists(empleador.getId());
        assertThat(empleadorExistsInEs).isFalse();

        // Validate the database is empty
        List<Empleador> empleadorList = empleadorRepository.findAll();
        assertThat(empleadorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEmpleador() throws Exception {
        // Initialize the database
        empleadorRepository.saveAndFlush(empleador);
        empleadorSearchRepository.save(empleador);

        // Search the empleador
        restEmpleadorMockMvc.perform(get("/api/_search/empleadors?query=id:" + empleador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empleador.getId().intValue())))
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
        TestUtil.equalsVerifier(Empleador.class);
        Empleador empleador1 = new Empleador();
        empleador1.setId(1L);
        Empleador empleador2 = new Empleador();
        empleador2.setId(empleador1.getId());
        assertThat(empleador1).isEqualTo(empleador2);
        empleador2.setId(2L);
        assertThat(empleador1).isNotEqualTo(empleador2);
        empleador1.setId(null);
        assertThat(empleador1).isNotEqualTo(empleador2);
    }
}
