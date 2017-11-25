package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Atencion;
import pe.gob.trabajo.repository.AtencionRepository;
import pe.gob.trabajo.repository.search.AtencionSearchRepository;
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
 * Test class for the AtencionResource REST controller.
 *
 * @see AtencionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class AtencionResourceIntTest {

    private static final String DEFAULT_V_OBSATENCI = "AAAAAAAAAA";
    private static final String UPDATED_V_OBSATENCI = "BBBBBBBBBB";

    private static final Boolean DEFAULT_N_FLGEMBARA = false;
    private static final Boolean UPDATED_N_FLGEMBARA = true;

    private static final String DEFAULT_V_ESTADO = "A";
    private static final String UPDATED_V_ESTADO = "B";

    private static final String DEFAULT_V_NUMTICKET = "AAAAAAAAAA";
    private static final String UPDATED_V_NUMTICKET = "BBBBBBBBBB";

    private static final Integer DEFAULT_N_CODTREPRE = 1;
    private static final Integer UPDATED_N_CODTREPRE = 2;

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
    private AtencionRepository atencionRepository;

    @Autowired
    private AtencionSearchRepository atencionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAtencionMockMvc;

    private Atencion atencion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AtencionResource atencionResource = new AtencionResource(atencionRepository, atencionSearchRepository);
        this.restAtencionMockMvc = MockMvcBuilders.standaloneSetup(atencionResource)
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
    public static Atencion createEntity(EntityManager em) {
        Atencion atencion = new Atencion()
            .vObsatenci(DEFAULT_V_OBSATENCI)
            .nFlgembara(DEFAULT_N_FLGEMBARA)
            .vEstado(DEFAULT_V_ESTADO)
            .vNumticket(DEFAULT_V_NUMTICKET)
            .nCodtrepre(DEFAULT_N_CODTREPRE)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return atencion;
    }

    @Before
    public void initTest() {
        atencionSearchRepository.deleteAll();
        atencion = createEntity(em);
    }

    @Test
    @Transactional
    public void createAtencion() throws Exception {
        int databaseSizeBeforeCreate = atencionRepository.findAll().size();

        // Create the Atencion
        restAtencionMockMvc.perform(post("/api/atencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(atencion)))
            .andExpect(status().isCreated());

        // Validate the Atencion in the database
        List<Atencion> atencionList = atencionRepository.findAll();
        assertThat(atencionList).hasSize(databaseSizeBeforeCreate + 1);
        Atencion testAtencion = atencionList.get(atencionList.size() - 1);
        assertThat(testAtencion.getvObsatenci()).isEqualTo(DEFAULT_V_OBSATENCI);
        assertThat(testAtencion.isnFlgembara()).isEqualTo(DEFAULT_N_FLGEMBARA);
        assertThat(testAtencion.getvEstado()).isEqualTo(DEFAULT_V_ESTADO);
        assertThat(testAtencion.getvNumticket()).isEqualTo(DEFAULT_V_NUMTICKET);
        assertThat(testAtencion.getnCodtrepre()).isEqualTo(DEFAULT_N_CODTREPRE);
        assertThat(testAtencion.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testAtencion.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testAtencion.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testAtencion.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testAtencion.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testAtencion.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testAtencion.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Atencion in Elasticsearch
        Atencion atencionEs = atencionSearchRepository.findOne(testAtencion.getId());
        assertThat(atencionEs).isEqualToComparingFieldByField(testAtencion);
    }

    @Test
    @Transactional
    public void createAtencionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = atencionRepository.findAll().size();

        // Create the Atencion with an existing ID
        atencion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAtencionMockMvc.perform(post("/api/atencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(atencion)))
            .andExpect(status().isBadRequest());

        // Validate the Atencion in the database
        List<Atencion> atencionList = atencionRepository.findAll();
        assertThat(atencionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknFlgembaraIsRequired() throws Exception {
        int databaseSizeBeforeTest = atencionRepository.findAll().size();
        // set the field null
        atencion.setnFlgembara(null);

        // Create the Atencion, which fails.

        restAtencionMockMvc.perform(post("/api/atencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(atencion)))
            .andExpect(status().isBadRequest());

        List<Atencion> atencionList = atencionRepository.findAll();
        assertThat(atencionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkvEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = atencionRepository.findAll().size();
        // set the field null
        atencion.setvEstado(null);

        // Create the Atencion, which fails.

        restAtencionMockMvc.perform(post("/api/atencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(atencion)))
            .andExpect(status().isBadRequest());

        List<Atencion> atencionList = atencionRepository.findAll();
        assertThat(atencionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkvNumticketIsRequired() throws Exception {
        int databaseSizeBeforeTest = atencionRepository.findAll().size();
        // set the field null
        atencion.setvNumticket(null);

        // Create the Atencion, which fails.

        restAtencionMockMvc.perform(post("/api/atencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(atencion)))
            .andExpect(status().isBadRequest());

        List<Atencion> atencionList = atencionRepository.findAll();
        assertThat(atencionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = atencionRepository.findAll().size();
        // set the field null
        atencion.setnUsuareg(null);

        // Create the Atencion, which fails.

        restAtencionMockMvc.perform(post("/api/atencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(atencion)))
            .andExpect(status().isBadRequest());

        List<Atencion> atencionList = atencionRepository.findAll();
        assertThat(atencionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = atencionRepository.findAll().size();
        // set the field null
        atencion.settFecreg(null);

        // Create the Atencion, which fails.

        restAtencionMockMvc.perform(post("/api/atencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(atencion)))
            .andExpect(status().isBadRequest());

        List<Atencion> atencionList = atencionRepository.findAll();
        assertThat(atencionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = atencionRepository.findAll().size();
        // set the field null
        atencion.setnFlgactivo(null);

        // Create the Atencion, which fails.

        restAtencionMockMvc.perform(post("/api/atencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(atencion)))
            .andExpect(status().isBadRequest());

        List<Atencion> atencionList = atencionRepository.findAll();
        assertThat(atencionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = atencionRepository.findAll().size();
        // set the field null
        atencion.setnSedereg(null);

        // Create the Atencion, which fails.

        restAtencionMockMvc.perform(post("/api/atencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(atencion)))
            .andExpect(status().isBadRequest());

        List<Atencion> atencionList = atencionRepository.findAll();
        assertThat(atencionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAtencions() throws Exception {
        // Initialize the database
        atencionRepository.saveAndFlush(atencion);

        // Get all the atencionList
        restAtencionMockMvc.perform(get("/api/atencions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(atencion.getId().intValue())))
            .andExpect(jsonPath("$.[*].vObsatenci").value(hasItem(DEFAULT_V_OBSATENCI.toString())))
            .andExpect(jsonPath("$.[*].nFlgembara").value(hasItem(DEFAULT_N_FLGEMBARA.booleanValue())))
            .andExpect(jsonPath("$.[*].vEstado").value(hasItem(DEFAULT_V_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].vNumticket").value(hasItem(DEFAULT_V_NUMTICKET.toString())))
            .andExpect(jsonPath("$.[*].nCodtrepre").value(hasItem(DEFAULT_N_CODTREPRE)))
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
    public void getAtencion() throws Exception {
        // Initialize the database
        atencionRepository.saveAndFlush(atencion);

        // Get the atencion
        restAtencionMockMvc.perform(get("/api/atencions/{id}", atencion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(atencion.getId().intValue()))
            .andExpect(jsonPath("$.vObsatenci").value(DEFAULT_V_OBSATENCI.toString()))
            .andExpect(jsonPath("$.nFlgembara").value(DEFAULT_N_FLGEMBARA.booleanValue()))
            .andExpect(jsonPath("$.vEstado").value(DEFAULT_V_ESTADO.toString()))
            .andExpect(jsonPath("$.vNumticket").value(DEFAULT_V_NUMTICKET.toString()))
            .andExpect(jsonPath("$.nCodtrepre").value(DEFAULT_N_CODTREPRE))
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
    public void getNonExistingAtencion() throws Exception {
        // Get the atencion
        restAtencionMockMvc.perform(get("/api/atencions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAtencion() throws Exception {
        // Initialize the database
        atencionRepository.saveAndFlush(atencion);
        atencionSearchRepository.save(atencion);
        int databaseSizeBeforeUpdate = atencionRepository.findAll().size();

        // Update the atencion
        Atencion updatedAtencion = atencionRepository.findOne(atencion.getId());
        updatedAtencion
            .vObsatenci(UPDATED_V_OBSATENCI)
            .nFlgembara(UPDATED_N_FLGEMBARA)
            .vEstado(UPDATED_V_ESTADO)
            .vNumticket(UPDATED_V_NUMTICKET)
            .nCodtrepre(UPDATED_N_CODTREPRE)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restAtencionMockMvc.perform(put("/api/atencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAtencion)))
            .andExpect(status().isOk());

        // Validate the Atencion in the database
        List<Atencion> atencionList = atencionRepository.findAll();
        assertThat(atencionList).hasSize(databaseSizeBeforeUpdate);
        Atencion testAtencion = atencionList.get(atencionList.size() - 1);
        assertThat(testAtencion.getvObsatenci()).isEqualTo(UPDATED_V_OBSATENCI);
        assertThat(testAtencion.isnFlgembara()).isEqualTo(UPDATED_N_FLGEMBARA);
        assertThat(testAtencion.getvEstado()).isEqualTo(UPDATED_V_ESTADO);
        assertThat(testAtencion.getvNumticket()).isEqualTo(UPDATED_V_NUMTICKET);
        assertThat(testAtencion.getnCodtrepre()).isEqualTo(UPDATED_N_CODTREPRE);
        assertThat(testAtencion.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testAtencion.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testAtencion.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testAtencion.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testAtencion.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testAtencion.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testAtencion.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Atencion in Elasticsearch
        Atencion atencionEs = atencionSearchRepository.findOne(testAtencion.getId());
        assertThat(atencionEs).isEqualToComparingFieldByField(testAtencion);
    }

    @Test
    @Transactional
    public void updateNonExistingAtencion() throws Exception {
        int databaseSizeBeforeUpdate = atencionRepository.findAll().size();

        // Create the Atencion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAtencionMockMvc.perform(put("/api/atencions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(atencion)))
            .andExpect(status().isCreated());

        // Validate the Atencion in the database
        List<Atencion> atencionList = atencionRepository.findAll();
        assertThat(atencionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAtencion() throws Exception {
        // Initialize the database
        atencionRepository.saveAndFlush(atencion);
        atencionSearchRepository.save(atencion);
        int databaseSizeBeforeDelete = atencionRepository.findAll().size();

        // Get the atencion
        restAtencionMockMvc.perform(delete("/api/atencions/{id}", atencion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean atencionExistsInEs = atencionSearchRepository.exists(atencion.getId());
        assertThat(atencionExistsInEs).isFalse();

        // Validate the database is empty
        List<Atencion> atencionList = atencionRepository.findAll();
        assertThat(atencionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAtencion() throws Exception {
        // Initialize the database
        atencionRepository.saveAndFlush(atencion);
        atencionSearchRepository.save(atencion);

        // Search the atencion
        restAtencionMockMvc.perform(get("/api/_search/atencions?query=id:" + atencion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(atencion.getId().intValue())))
            .andExpect(jsonPath("$.[*].vObsatenci").value(hasItem(DEFAULT_V_OBSATENCI.toString())))
            .andExpect(jsonPath("$.[*].nFlgembara").value(hasItem(DEFAULT_N_FLGEMBARA.booleanValue())))
            .andExpect(jsonPath("$.[*].vEstado").value(hasItem(DEFAULT_V_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].vNumticket").value(hasItem(DEFAULT_V_NUMTICKET.toString())))
            .andExpect(jsonPath("$.[*].nCodtrepre").value(hasItem(DEFAULT_N_CODTREPRE)))
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
        TestUtil.equalsVerifier(Atencion.class);
        Atencion atencion1 = new Atencion();
        atencion1.setId(1L);
        Atencion atencion2 = new Atencion();
        atencion2.setId(atencion1.getId());
        assertThat(atencion1).isEqualTo(atencion2);
        atencion2.setId(2L);
        assertThat(atencion1).isNotEqualTo(atencion2);
        atencion1.setId(null);
        assertThat(atencion1).isNotEqualTo(atencion2);
    }
}
